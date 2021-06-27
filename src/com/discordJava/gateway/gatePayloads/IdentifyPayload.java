package com.discordJava.gateway.gatePayloads;

import com.discordJava.gateway.GatewayPayload;

public class IdentifyPayload extends GatewayPayload {
    public IdentifyPayload(String token, Integer intents, Properties properties) {
        super(2, "{\"token\":\"" + token + "\",\"intents\":" + intents + "{" + "\"$os\":\"" + properties.os + "\",\"$browser\":\"" + properties.browser + "\",\"$device\":\"" + properties.device + "\"}}");
    }

    public class Properties {
        public String os;
        public String browser;
        public String device;

        Properties(String os, String browser, String device) {
            this.os = os;
            this.browser = browser;
            this.device = device;
        }
    }

//    private class IdentifyData extends payloadData {
//        String token;
//        Integer intents;
//        Properties properties;
//        IdentifyData(String token, Integer intents, Properties properties) {
//           this.token = token;
//           this.intents = intents;
//           this.properties = properties;
//        }
//    }
}