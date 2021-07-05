package com.discordJava.commands;

//
//public interface Command {
//
//    void execute(Input input);
//
//    String[] getAliases();
//    String getName();
//    String getDescription();
//    String getUsage();
//    String getExamples();
//    Boolean getDmSupport();
//    Integer[] getArgs();
//
//    class Input {
//        public String prefix;
//        public Client client;
//        public String[] args;
//        public String content;
//        public String command;
//        public Message msg;
//        public Byte version;
//
//        public Input(String prefix, Client client, String[] args, String content, String command, Message msg, Byte version) {
//            this.prefix = prefix;
//            this.args = args;
//            this.client = client;
//            this.content = content;
//            this.command = command;
//            this.msg = msg;
//            this.version = version;
//        }
//    }
//}


import com.discordJava.classes.Client;
import com.discordJava.classes.Message;

public abstract class Command {

    public abstract void execute(Input input);

    //list of all commands to be used by bot
    public static final Command[] LIST = new Command[]{
            new Ping(),
            new Help()
    };

    public final String[] aliases;
    public final String name;
    public final String description;
    public final String usage;
    public final String examples;
    public final Boolean dmSupport;
    public final Integer[] args;

    public Command(String[] aliases, String name, String description, String usage, String examples, Boolean dmSupport, Integer[] args) {
        this.aliases = aliases;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.examples = examples;
        this.dmSupport = dmSupport;
        this.args = args;
    }

    public static class Input {
        public String prefix;
        public Client client;
        public String[] args;
        public String content;
        public String command;
        public Message msg;
        public Byte version;

        public Input(String prefix, Client client, String[] args, String content, String command, Message msg, Byte version) {
            this.prefix = prefix;
            this.args = args;
            this.client = client;
            this.content = content;
            this.command = command;
            this.msg = msg;
            this.version = version;
        }
    }
}
