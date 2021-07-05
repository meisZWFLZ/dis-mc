package com.discordJava;

import com.discordJava.classes.Client;
import com.discordJava.classes.IntentEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || !args[0].toLowerCase().equals("true")) {
            DiscordBot Bot = new DiscordBot(Client.tokenFilter(System.getenv("token")));
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
        Arrays.stream(IntentEvent.values()).forEach(x -> {
            String[] classes = Arrays.stream(x.getClasses()).map(y -> {
                String[] a = y.getClass().getName().split("\\.");
                return a[a.length - 1];
            }).toList().toArray(new String[]{});
            String[] names = x.getNames();
            int i = 0;
            for (String name : names)
                events.put(name, classes[i++]);
        });

        int i = 0;
        String string = "";
        String str;
        for (Map.Entry<String, String> event : events.entrySet()) {
            str = "case \"" + event.getKey() + "\" -> new EventHandler<>(this, " + event.getValue() + ".class);";
            System.out.print(str);
            if(++i % 2 == 0) System.out.print("\n");
            else for (int j = 0; j < (100 - str.length()); j++) System.out.print(" ");
        }
        System.out.println();

//        AtomicInteger z = new AtomicInteger();
//        Arrays.stream(Client.IntentEvent.EVENT_ARRAY).map(x -> x.getClass().getName().substring(x.getClass().getName().lastIndexOf(".") + 1)).forEach(x -> System.out.print("new " + x + "(), " + ((z.incrementAndGet() % 6 == 0) ? "\n" : "")));

//        Arrays.stream(Client.IntentEvent.values()).map(Enum::name).map(x -> "\"" + x + "\", ").forEach(System.out::println);

//        for (String event : events.toArray(new String[]{}))
//            System.out.println(""event);
    }

}
