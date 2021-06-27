package com.discordJava.classes;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Interaction extends DiscordSuperClass {
    public String id;
    public String application_id;
    public Integer type;
    public ApplicationCommandInteractionData data;
    public String guild_id;
    public String channel_id;
    public GuildMember member;
    public User user;
    public String token;
    public Integer version;
    public Message message;
    public Client client;

    public HttpResponse ack() throws URISyntaxException, IOException, InterruptedException {
        InteractionResponse res = new InteractionResponse(this.client);
        res.type = 5;
        return HttpClient.newHttpClient().send(
                HttpRequest.newBuilder()
                        .uri(new URI("https://discord.com/api/v8/interactions/" + this.id + "/" + this.token + "/callback"))
                        .headers("Authorization", "Bot " + this.client.token, "Content-type", "application/json; charset=UTF-8")
                        .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(res, res.getClass())))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }
}
