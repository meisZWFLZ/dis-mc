package com.discordJava.gateway;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

public class ClientWebsocket implements Runnable {
    public static class Frame {
        boolean fin;
        String payload;
        byte op;
        long payloadLen;
        boolean mask;

        public Frame(boolean fin, byte op, boolean mask, long payloadLen, String payload) {
            this.fin = fin;
            this.op = op;
            this.payloadLen = payloadLen;
            this.payload = payload;
            this.mask = mask;
        }

        public Frame() {
            this.fin = true;
            this.op = (byte) 1;
            this.payloadLen = 0;
            this.payload = "";
            this.mask = false;
        }

        public byte[] construct() {
            byte[] header = new byte[2];

            //mask
            int maskKey = 0;
            byte[] maskBytes = new byte[4];
            String payload = this.payload;

            if (this.mask) {
                int i = 0;
                for (byte ignored : maskBytes)
                    maskBytes[i++] = (byte) Math.floor(Math.random() * Byte.MAX_VALUE);

                i = 0;
                int j = 0;

//                System.out.println(Arrays.toString(maskBytes));

                StringBuilder sb = new StringBuilder();
                for (byte b : payload.getBytes()) {
                    j = i++ % 4;
                    sb.append(new String(new byte[]{(byte) (b ^ maskBytes[j])}, StandardCharsets.UTF_8));
                }
                payload = sb.toString();
            }

//            System.out.printf("payload: %s\n", payload );

            int length;
//          length = (this.payloadLen > 125 ? ((this.payloadLen > ((1 << 15) - 1)) ? 127 : 126) : this.payloadLen);
            if (payload.length() > 125) {
                if (payload.length() > ((1 << 15) - 1))
                    length = 127;
                else length = 126;
            } else length = payload.length();

            header[0] = (byte) (this.fin ? 0b10000000 : 0);
            header[0] = (byte) (header[0] + this.op);
            header[1] = (byte) (this.mask ? 0b10000000 : 0);
            header[1] = (byte) (header[1] + length);

            //extra length?
            long extraLen = 0;
            int extLenLen = 0;
            if (length > 125) {
                extraLen = payload.length();
                extLenLen = ((length == 126) ? 2 : 8);
            }
            long finalLen = extraLen != 0 ? extraLen : length;

            byte[] frame = new byte[(int) (2 + payload.length() + extLenLen + (this.mask ? 4 : 0))];

            int i = 0;
            frame[i++] = header[0];
            frame[i++] = header[1];

            if (extraLen != 0)
                for (int j = extLenLen; j > 0; j--)
                    frame[i++] = (byte) (finalLen >> ((j - 1) * 8));
            if (this.mask)
                for (int j = 0; j < 4; j++)
                    frame[i++] = maskBytes[j];
            for (int j = 0; j < finalLen; j++)
                frame[i++] = payload.getBytes()[j];
//            print all of the bytes for debugging
//            for (byte b : frame) System.out.print(b + " ");


            try {
//                System.out.println("constructed payload:");
                InputStream is = new ByteArrayInputStream(frame);
                Frame.parse(is, 0);
            } catch (Exception e) {
                System.out.println(e);
            }

            return frame;
        }

        //parse the frame
        public static Frame parse(InputStream is, int firstHeaderByte) throws Exception {
            //get the header data

            byte[] header = new byte[2];
            is.read(header, firstHeaderByte != 0 ? 1 : 0, firstHeaderByte != 0 ? 1 : 2);
            if (firstHeaderByte != 0) header[0] = (byte) firstHeaderByte;

            //split up header
            final boolean fin = (header[0] >> 7) != 0;
            final byte opcode = (byte) (header[0] & 0b1111);
            final boolean control = opcode > 7;
            final boolean mask = (header[1] & 0b10000000) != 0;
            long payload_len = (header[1] & 0b1111111);

            //extra length
            byte[] b;
            if (payload_len > 125) {
                is.read(b = new byte[(payload_len == 126) ? 2 : 8]);
                payload_len = b.length == 2 ? ByteBuffer.wrap(b).getShort() : ByteBuffer.wrap(b).getLong();
            }

            //get payload
            String payload;
            if (mask) {
                byte[] maskBytes;
                is.read(maskBytes = new byte[4]);
//                String payload = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 0, j; i < payload_len; i++) {
                    j = i % 4;
                    sb.append(new String(new byte[]{(byte) (is.read() ^ maskBytes[j])}, StandardCharsets.UTF_8));
                }
                payload = sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                if (!control) {
                    is.read(b = new byte[(int) Math.floor((double) payload_len / 2)]);
                    sb.append(new String(b, StandardCharsets.UTF_8));
                    is.read(b = new byte[(int) Math.ceil((double) payload_len / 2)]);
                    sb.append(new String(b, StandardCharsets.UTF_8));
                } else {
                    is.read(b = new byte[2]);
                    sb.append((Short) ByteBuffer.wrap(b).getShort());
                }
                payload = sb.toString();
            }

//            System.out.println("fin: " + fin + "\nop: " + opcode + "\ncontrol?: " + control + "\nmask: " + mask + "\npayload length: " + payload_len + "\npayload: " + payload + "\nheader: " + header[0] + " " + header[1] + "\n");

            return new Frame(fin, opcode, mask, payload_len, payload);
        }
    }

    static final SSLSocketFactory SSL_FACTORY = (SSLSocketFactory) SSLSocketFactory.getDefault();
    static final String[] CIPHER_SUITES = new String[]{
            // "TLS_AES_256_GCM_SHA384",
            "TLS_AES_128_GCM_SHA256",
    };

    Thread thread;
    SSLSocket socket;
    InputStream socketInputStream;
    OutputStream socketOutputStream;
    Scanner sc;
    PrintWriter pw;
    public String endpoint;

    private HashMap<Byte, ArrayList<Function<Frame, Object>>> eventList = new HashMap<>();
    private boolean sending = false;

    private volatile Frame wafle;
    private ThreadGroup frameHandlers;

    /**
     * <code> A client's websocket connection to server </code>
     *
     * @param hostname domain name, server side of websocket, example: example.com
     * @param port     not really sure, example: 443
     * @param endpoint where you want to connect host at, still not too sure
     */

    public ClientWebsocket(String hostname, int port, String endpoint) throws IOException {
        this.thread = new Thread(null, this, "Thread - Websocket");
        this.socket = (SSLSocket) SSL_FACTORY.createSocket(hostname, port);
        this.socketInputStream = this.socket.getInputStream();
        this.socketOutputStream = this.socket.getOutputStream();
        this.sc = new Scanner(this.socketInputStream, StandardCharsets.UTF_8).useDelimiter("");
        this.pw = new PrintWriter(socketOutputStream);
        this.endpoint = endpoint;
        this.frameHandlers = new ThreadGroup("ClientWebsocket - FrameHandlers");
        this.thread.start();

        // Enable the cipher suites
        this.socket.setEnabledProtocols(new String[]{"TLSv1.1", "TLSv1.2", "TLSv1.3"});
        this.socket.setEnabledCipherSuites(CIPHER_SUITES);
    }

    /**
     * <code> Send message to websocket server </code>
     *
     * @param message What to send
     */

    public boolean send(byte[] message) {
        while (this.sending) Thread.onSpinWait();
        this.sending = true;
        try {
            Thread.sleep(50L);
            this.socketOutputStream.write(message);
            this.socketOutputStream.flush();
        } catch (IOException | InterruptedException e) {
            this.sending = false;
            return false;
        }
        this.sending = false;
        return true;
    }

    private ArrayList<Function<Frame, Object>> addListener(byte op, Function<Frame, Object> listener, int position) {
        ArrayList<Function<Frame, Object>> listeners = eventList.get(op);
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.ensureCapacity(listeners.size() + 1);
        listeners.add((position < 0 | listeners.size() < position) ? listeners.size() : position, listener);
        return eventList.put(op, listeners);
    }

    public ArrayList<Function<Frame, Object>> on(byte op, Function<Frame, Object> listener) {
        return this.addListener(op, listener, -1);
    }

    public Object close(Frame frame) {
        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void run() {
        // Start TLS Handshake
        try {
            this.socket.startHandshake();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String key = ranBase64Str();

        StringBuilder sb = new StringBuilder();
        sb.append(String.join("\r\n", new String[]{
                "GET " + endpoint + " HTTP/1.1",
                "Host: gateway.discord.gg",
                "Sec-WebSocket-Version: 13",
                "Sec-WebSocket-Key: " + key,
                "Connection: Upgrade",
                "Upgrade: websocket"
        })).append("\r\n\r\n");

        try {
            pw.write(sb.toString());
            pw.flush();
        } catch (Exception e) {
            System.out.println("Failed to send handshake!");
            System.exit(-1);
        }


        this.on((byte) 8, this::close);
        sc.useDelimiter("\r\n");

        Long handledFrames = 0L;

        //let upgrade response finish
        while (true) if (sc.next().length() == 0) break;

        Integer wafle;
        Thread frameHandler;

        try {
            while (!socket.isClosed()) {
                try {
                    while (true)
                        if ((wafle = !socket.isClosed() ? socket.getInputStream().read() : -1) != -1) break;
                } catch (Exception e) {
                    System.out.println("error originated at new frame checker loop");
                    throw e;
                }

                if (socket.isClosed()) break;

                this.wafle = Frame.parse(this.socketInputStream, wafle);

                frameHandler = new Thread(this.frameHandlers, this::frameHandler, handledFrames.toString());
                frameHandler.start();
                handledFrames++;


//                Frame frame;
//                ArrayList<Function<Frame, Object>> listeners;
//
//                try {
//                    //parse frame
//                    frame = Frame.parse(socketInputStream, wafle);
//                    listeners = eventList.get(frame.op);
//                } catch (Exception e) {
//                    System.out.println("error originated at frame parser");
//                    throw e;
//                }
//
//                try {
//                    //check
//                    if (listeners != null && !listeners.isEmpty()) {
//                        //loop through event listeners for frame's op
//                        for (Function<Frame, Object> listener : listeners) {
//                            try {
//                                listener.apply(frame);
//                            } catch (Exception e) {
//                                System.out.println("frame listener for op " + frame.op + " caused error: " + e);
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    System.out.println("error originated at frame handler");
//                    throw e;
//                }


            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Client websocket event listener obstructed");
        }
    }

    private void frameHandler() {
        try {
            Frame frame = this.wafle;

            //parse frame
            if (frame.op == 8) {
                this.socket.close();
                return;
            }
//            this.thread.notify();
            ArrayList<Function<Frame, Object>> listeners = this.eventList.get(frame.op);

            //check
            if (listeners != null && !listeners.isEmpty()) {
                //loop through event listeners for frame's op
                for (Function<Frame, Object> listener : listeners) {
                    try {
                        listener.apply(frame);
                    } catch (Exception e) {
                        System.out.println("frame listener for op " + frame.op + " caused error: " + e);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Frame handler encountered error: " + e);
            System.exit(-1);
        }
    }

    public static String ranBase64Str() {
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        return Base64.getEncoder().encodeToString(array);
    }
}
