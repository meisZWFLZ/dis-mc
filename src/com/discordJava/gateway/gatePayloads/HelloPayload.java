package com.discordJava.gateway.gatePayloads;

import com.discordJava.gateway.GatewayPayload;

public class HelloPayload extends GatewayPayload {
    public HelloPayload(Integer heartbeat_interval) {
        super(10, "{\"heartbeat_interval\":" + heartbeat_interval + "}");
    }
}
