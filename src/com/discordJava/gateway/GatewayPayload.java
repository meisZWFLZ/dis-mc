package com.discordJava.gateway;

import com.google.gson.Gson;

public abstract class GatewayPayload {
    public Integer op;
    public String d;
    public Integer s;
    public String t;

    public GatewayPayload(Integer op, String d, Integer s, String t) {
        this.op = op;
        this.d = d;
        this.s = s;
        this.t = t;
    }
    public GatewayPayload(Integer op, String d) {
        this.op = op;
        this.d = d;
        this.s = null;
        this.t = null;
    }
    public GatewayPayload(String d, Integer s, String t) {
        this.op = 0;
        this.d = d;
        this.s = s;
        this.t = t;
    }

    public ClientWebsocket.Frame getFrame(Integer op) {
        String payloadStr = new Gson().toJson(this);
        return new ClientWebsocket.Frame(true, (byte) 1, true, payloadStr.length(), payloadStr);
    }
}
