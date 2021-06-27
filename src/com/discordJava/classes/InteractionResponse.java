package com.discordJava.classes;

public class InteractionResponse extends DiscordSuperClass {
    public Integer type;
    public InteractionApplicationCommandCallbackData data;
    public Client client;

    public InteractionResponse(Client client) {
        this.type = null;
        this.data = null;
    }
}
