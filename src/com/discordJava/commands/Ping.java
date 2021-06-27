package com.discordJava.commands;

import com.discordJava.classes.Message;

import java.io.IOException;
import java.net.URISyntaxException;

public class Ping implements Command {
    static String[] aliases = new String[]{
            "pong"
    };
    public static String name = "ping";
    public static String description = "pong";
    public static String usage = "!ping";
    public static String examples = "!ping";
    public static Boolean dmSupport = true;
    public static Integer[] args = new Integer[]{1};

    public void execute(Input input) {
        try {
            Message.newBuilder(input.client).channel_id(input.msg.channel_id).content(input.content.toLowerCase().substring(input.prefix.length(), input.command.length() + input.prefix.length()).equals("ping") ? "***Pong!***" : "***Ping!***").build().send();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String[] getAliases() {return aliases;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getUsage() {return usage;}
    public String getExamples() {return examples;}
    public Boolean getDmSupport() {return dmSupport;}
    public Integer[] getArgs() {return args;}
}
