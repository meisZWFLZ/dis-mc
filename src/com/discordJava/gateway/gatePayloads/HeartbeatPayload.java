package com.discordJava.gateway.gatePayloads;

import com.discordJava.gateway.GatewayPayload;

public class HeartbeatPayload extends GatewayPayload {
    public HeartbeatPayload(Integer sequence) {
        super(1, sequence.toString());
    }
}
