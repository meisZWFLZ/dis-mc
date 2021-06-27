package com.discordJava.gateway;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import javax.net.ssl.SSLSocket;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class DiscordGateway extends ClientWebsocket implements Runnable {
    private byte v;
    private static final String baseURL = "https://discord.com/api/";
    private static final String encoding = "json";
    private static final String[] intentList = new String[]{
            "GUILDS",
            "GUILD_MEMBERS",
            "GUILD_BANS",
            "GUILD_EMOJIS",
            "GUILD_INTEGRATIONS",
            "GUILD_WEBHOOKS",
            "GUILD_INVITES",
            "GUILD_VOICE_STATES",
            "GUILD_PRESENCES",
            "GUILD_MESSAGES",
            "GUILD_MESSAGE_REACTIONS",
            "GUILD_MESSAGE_TYPING",
            "DIRECT_MESSAGES",
            "DIRECT_MESSAGE_REACTIONS",
            "DIRECT_MESSAGE_TYPING"
    };
    private int intents;
    public final String token;
    public Integer heartbeatInterval;
    private Integer sequence = 0;
    private boolean heartbeatACK = true;
    private String session_id = "";
    private final Thread heartbeater;

    private HashMap<java.io.Serializable, ArrayList<Function<HashMap, Object>>> eventList = new HashMap<java.io.Serializable, ArrayList<Function<HashMap, Object>>>();

    public DiscordGateway(String token, String[] intents, Byte v) throws Exception {
        super(
                "gateway.discord.gg", //may be subject to change
                443,
                "/?v=" + v + "&encoding=" + encoding
        );
        this.v = v;
        this.token = token;

        this.heartbeater = new Thread(null, this::heartbeater, "Discord Gateway Heartbeater Thread");

        this.intents = this.intentFinder(intents);

        super.on((byte) 1, this::listener);
        this.setListeners();
    }

    private ArrayList<Function<HashMap, Object>> addListener(int op, Function<HashMap, Object> listener, int position) {
        ArrayList<Function<HashMap, Object>> listeners = eventList.get(op);
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.ensureCapacity(listeners.size() + 1);
        listeners.add((position < 0 | listeners.size() < position) ? listeners.size() : position, listener);
        return eventList.put(op, listeners);
    }

    public ArrayList<Function<HashMap, Object>> on(int op, Function<HashMap, Object> listener) {
        return this.addListener(op, listener, -1);
    }

    private ArrayList<Function<HashMap, Object>> addListener(String dispatch, Function<HashMap, Object> listener, int position) {
        ArrayList<Function<HashMap, Object>> listeners = eventList.get(dispatch);
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.ensureCapacity(listeners.size() + 1);
        listeners.add((position < 0 | listeners.size() < position) ? listeners.size() : position, listener);
        return eventList.put(dispatch, listeners);
    }

    public ArrayList<Function<HashMap, Object>> on(String dispatch, Function<HashMap, Object> listener) {
        return this.addListener(dispatch.toUpperCase(), listener, -1);
    }

    public Object listener(Frame frame) {
        HashMap payload = new Gson().fromJson(frame.payload, HashMap.class);
        System.out.println(frame.payload);

        ArrayList<Function<HashMap, Object>> listeners = this.eventList.get(((Double) payload.get("op")).intValue());
        if (listeners != null && !listeners.isEmpty())
            for (Function<HashMap, Object> listener : listeners)
                try {
                    listener.apply(payload);
                } catch (Exception e) {
                    System.out.println("DISCORD GATEWAY OP 1 FRAME LISTENER ENCOUNTERED ERROR DUE TO LISTENER OF DISCORD PAYLOAD OP " + ((Double) payload.get("op")).intValue() + ": " + e);
                }

        return null;
    }

    //op: 9
    private Object invalidSessionListener(HashMap payload) {
        try {
            System.out.println("INVALID SESSION!!!");
//            this.resume();
        } catch (Exception e) {
            System.out.println("RESUME: " + e);
        }
        return null;
    }

    //op: 0
    private Object dispatchListener(HashMap payload) {
        this.sequence = ((Double) payload.get("s")).intValue();
        this.session_id = (String) ((LinkedTreeMap) payload.get("d")).get("session_id");

        System.out.println(((String) payload.get("t")).toUpperCase());

        ArrayList<Function<HashMap, Object>> listeners = this.eventList.get(((String) payload.get("t")).toUpperCase());
        if (listeners != null && !listeners.isEmpty())
            for (Function<HashMap, Object> listener : listeners)
                listener.apply(payload);

        return null;
    }

    //op: 1
    private Object heartbeatListener(HashMap payload) {
//        this.gateThread.notify();
        return null;
    }

    //op: 11
    private Object heartbeatACKListener(HashMap payload) {
        this.heartbeatACK = true;
        return null;
    }

    //op: 10
    private Object helloListener(HashMap payload) {
        this.heartbeatInterval = ((Double) ((LinkedTreeMap) payload.get("d")).get("heartbeat_interval")).intValue();

        //identify payload
        HashMap<String, String> idDataProp = new HashMap<String, String>();
        idDataProp.put("$os", "windows");
        idDataProp.put("$browser", "lol");
        idDataProp.put("$device", "lol");

        HashMap<String, java.io.Serializable> idLoadData = new HashMap<String, java.io.Serializable>();
        idLoadData.put("token", this.token);
        idLoadData.put("intents", this.intents);
        idLoadData.put("properties", idDataProp);

        HashMap<String, java.io.Serializable> idPayload = new HashMap<String, java.io.Serializable>();
        idPayload.put("op", 2);
        idPayload.put("d", idLoadData);

        String idLoadStr = new Gson().toJson(idPayload, HashMap.class);
//        System.out.println(idLoadStr);

        Frame idFrame = new Frame(true, (byte) 1, true, idLoadStr.length(), idLoadStr);

        if (!super.send(idFrame.construct()))
            System.out.println("Identify payload failed!");

        //start heartbeating
//        System.out.println(this.heartbeater.getState().name());
        if (this.heartbeater.getState().name().equals("NEW"))
            this.heartbeater.start();

        return null;
    }

    //op: 0, dispatch: ready
    private Object ready(HashMap payload) {
        //set session_id for resume
        this.session_id = (String) ((LinkedTreeMap) payload.get("d")).get("session_id");

        return null;
    }

    private void resume() throws Exception {
        System.out.println("Resuming!");
        this.socket.close();

        HashMap<String, Serializable> data = new HashMap<>();
        data.put("token", this.token);
        data.put("session_id", this.session_id);
        data.put("seq", sequence);

        HashMap<String, Serializable> payload = new HashMap<>();
        payload.put("op", 2);
        payload.put("d", data);

        String payloadStr = new Gson().toJson(payload, HashMap.class);
        Frame frame = new Frame(true, (byte) 1, true, payloadStr.length(), payloadStr);

        try {
            super.thread = new Thread(null, this, "Thread - Websocket");
            super.socket = (SSLSocket) SSL_FACTORY.createSocket("gateway.discord.gg", 443);
            super.socketInputStream = super.socket.getInputStream();
            super.socketOutputStream = super.socket.getOutputStream();
            super.sc = new Scanner(super.socketInputStream, StandardCharsets.UTF_8).useDelimiter("");
            super.pw = new PrintWriter(super.socketOutputStream);

            super.thread.start();

            super.socket.setEnabledProtocols(new String[]{"TLSv1.1", "TLSv1.2", "TLSv1.3"});
            super.socket.setEnabledCipherSuites(CIPHER_SUITES);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Could not restart thread");
        }

        if (!super.send(frame.construct()))
            throw new Exception("RESUME PAYLOAD CONSTRUCTION/DELIVERY FAILED");
    }

    public void heartbeater() {
        HashMap<String, java.io.Serializable> payload = new HashMap();
        payload.put("op", 1);
        payload.put("d", 0);

        String payloadStr;
        Frame frame;

        System.out.println("Starting to heartbeat!");

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            System.out.println("Heartbeat initialization failed");
            System.out.println(e);
        }

        while (true) {
            try {
                if (!this.heartbeatACK) this.resume();
                this.heartbeatACK = false;

                payload.put("d", this.sequence);
                payloadStr = new Gson().toJson(payload, HashMap.class);

                frame = new Frame(true, (byte) 1, true, payloadStr.length(), payloadStr);

                /*if (!*/super.send(frame.construct());/*)*/
//                    throw new Exception("HEARTBEAT PAYLOAD CONSTRUCTION/DELIVERY FAILED");

                System.out.printf("sent heartbeat! d: %d\n", payload.get("d"));

                Thread.sleep((long) this.heartbeatInterval);
            } catch (Exception e) {
                System.out.println(e);
                System.exit(-1);
            }    }

    }

    private void setListeners() {
        this.on("ready", this::ready);
        this.on(1, this::heartbeatListener);
        this.on(9, this::invalidSessionListener);
        this.on(0, this::dispatchListener);
        this.on(10, this::helloListener);
        this.on(11, this::heartbeatACKListener);
    }

    private Integer intentFinder(String[] intents) throws Exception {
        Integer intentInteger = 0;
        List<String> intentList = Arrays.asList(DiscordGateway.intentList);
        for (String intent : intents)
            if (!intentList.contains(intent.toUpperCase())) throw new Exception("Invalid intent: " + intent);
            else intentInteger = intentInteger + (1 << intentList.indexOf(intent.toUpperCase()));
        return intentInteger;
    }
}