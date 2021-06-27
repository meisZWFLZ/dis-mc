package com.discordJava.slashCommands;

import com.discordJava.classes.ApplicationCommand;
import com.discordJava.classes.Client;
import com.discordJava.classes.Interaction;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface SlashCommand {

    void execute(Input input);

    String getId();

    String getGuild();

    String getName();

    default ApplicationCommand getCmd(Client client) throws URISyntaxException, IOException, InterruptedException {
        return new Gson().fromJson(HttpClient.newHttpClient().send(HttpRequest.newBuilder(new URI("https://discord.com/api/v8/applications/824688812247482448/" + (this.getGuild() == null ? "" : "guilds/" + this.getGuild()) + "commands/" + this.getId())).header("Authorization", "Bot " + client.token).GET().build(), HttpResponse.BodyHandlers.ofString()).body(), ApplicationCommand.class);
    }

    class Input {
        public Client client;
        public Interaction interaction;

        public Input(Client client, Interaction interaction) {
            this.client = client;
            this.interaction = interaction;
        }
    }
}
