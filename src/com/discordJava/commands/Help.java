package com.discordJava.commands;

import com.discordJava.classes.Embed;
import com.discordJava.classes.Message;

public class Help extends Command {
    static String[] aliases = new String[]{
            "h",
            "noob",
            "bad"
    };
    static String name = "help";
    static String description = "get some help, noob!";
    static String usage = "!help";
    static String examples = "!help";
    static Boolean dmSupport = true;
    static Integer[] args = new Integer[] {2};

    public void execute(Input input) {
        try {
            Message.newBuilder(input.client).embeds(new Embed[]{new Embed("Help", "input.args[1]")}).channel_id(input.msg.channel_id).build().send();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Help() {
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
