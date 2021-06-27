package com.discordJava.commands;

import com.discordJava.classes.Embed;
import com.discordJava.classes.Message;

public class Help implements Command {
    static String[] aliases = new String[]{
            "h",
            "noob",
            "bad"
    };
    public static String name = "help";
    public static String description = "get some help, noob!";
    public static String usage = "!help";
    public static String examples = "!help";
    public static Boolean dmSupport = true;
    public static Integer[] args = new Integer[] {2};

    public void execute(Input input) {
        try {
            Message.newBuilder(input.client).embeds(new Embed[]{new Embed("Help", "input.args[1]")}).channel_id(input.msg.channel_id).build().send();
        } catch (Exception e) {
            System.out.println(e);
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
