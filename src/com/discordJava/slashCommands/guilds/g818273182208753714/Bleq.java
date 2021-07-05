package com.discordJava.slashCommands.guilds.g818273182208753714;

import com.discordJava.classes.InteractionApplicationCommandCallbackData;
import com.discordJava.classes.InteractionResponse;
import com.discordJava.slashCommands.SlashCommand;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Bleq implements SlashCommand {
    static String id = "855994009352929290";
    static String guild = "818273182208753714";
    static String name = "Bleq";

    public void execute(SlashCommand.Input input) {
        try {
            InteractionResponse res = new InteractionResponse(input.client);
            res.data = new InteractionApplicationCommandCallbackData();
            res.type = 4;
            res.data.content = (String) input.interaction.data.options[0].value;

            String resStr = new Gson().toJson(res, res.getClass());

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI("https://discord.com/api/v8/interactions/" + input.interaction.id + "/" + input.interaction.token + "/callback"))
                    .headers("Authorization", "Bot " + input.client.token(), "Content-type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(resStr))
                    .build();
            HttpResponse response = client.send(req, HttpResponse.BodyHandlers.ofString());
            String body = (String) response.body();
            System.out.println(body);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getName() {return name;}
    public String getGuild() {return guild;}
    public String getId() {return id;}

//    static class BleqInteraction extends Interaction {
//        BleqInteraction(Client client) {
//            this.
//        }
//    }
}
