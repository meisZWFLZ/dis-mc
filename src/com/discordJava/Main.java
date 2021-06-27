package com.discordJava;

import com.discordJava.classes.Client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || !args[0].toLowerCase().equals("true")) {
            DiscordBot Bot = new DiscordBot(System.getenv("token"));
//            Thread.sleep(10000);
//            Frame frame = new Frame(true, (byte) 8, true, 0, "");
//            Bot.send(frame.construct());
        } else {
            otherThing();
        }
    }

    static void otherThing() throws URISyntaxException, IOException, InterruptedException {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest req = HttpRequest.newBuilder()
//                .uri(new URI("https://discord.com/api/v8/users/@me"))
//                .headers("Authorization", "Bot " + System.getenv().get("token")/*, "Content-type", "application/json; charset=UTF-8"*/)
////                .POST(HttpRequest.BodyPublishers.ofFile(Path.of("C:\\Users\\atcur\\Documents\\POGrams\\java\\dis-mc\\src\\com\\discordJava\\smth.json")))
//                .GET()
//                .build();
//        HttpResponse response = client.send(req, HttpResponse.BodyHandlers.ofString());
//        String body = (String) response.body();
//        System.out.println(body);

//        System.out.println(new MessageReactionRemoveEmoji().getEventName());

        HashMap<String, String> events = new HashMap<String, String>(50);
        Arrays.stream(Client.IntentEvent.values()).forEach(x -> {
            String[] classes = Arrays.stream(x.getClasses()).map(y -> {
                String[] a = y.getClass().getName().split("\\.");
                return a[a.length - 1];
            }).toList().toArray(new String[]{});
            String[] names = x.getNames();
            int i = 0;
            for(String name : names)
                events.put(name, classes[i++]);
        });

        for(Map.Entry<String, String> event : events.entrySet())
            System.out.println(" case \"" + event.getKey() + "\" -> new EventHandler<" + event.getValue() + ">(this, " + event.getValue() + ".class);");

//        for (String event : events.toArray(new String[]{}))
//            System.out.println(""event);
    }

}
