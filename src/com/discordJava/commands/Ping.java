package com.discordJava.commands;

import com.discordJava.classes.Message;

import java.io.IOException;
import java.net.URISyntaxException;

public class Ping extends Command {
    static String[] aliases = new String[]{
            "pong"
    };
    static String name = "ping";
    static String description = "pong";
    static String usage = "!ping";
    static String examples = "!ping";
    static Boolean dmSupport = true;
    static Integer[] args = new Integer[]{1};

    public void execute(Input input) {
        try {
            Message.newBuilder(input.client).channel_id(input.msg.channel_id).content(input.content.toLowerCase().substring(input.prefix.length(), input.command.length() + input.prefix.length()).equals("ping") ? "***Pong!***" : "***Ping!***").build().send();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Ping() {
        super(aliases, name, description, usage, examples, dmSupport, args);
    }

//    public String[] getAliases() {return aliases;}
//    public String getName() {return name;}
//    public String getDescription() {return description;}
//    public String getUsage() {return usage;}
//    public String getExamples() {return examples;}
//    public Boolean getDmSupport() {return dmSupport;}
//    public Integer[] getArgs() {return args;}
}
