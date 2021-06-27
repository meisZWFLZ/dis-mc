package com.discordJava.commands;

import com.discordJava.classes.Client;
import com.discordJava.classes.Message;

public interface Command {

    void execute(Input input);

    String[] getAliases();
    String getName();
    String getDescription();
    String getUsage();
    String getExamples();
    Boolean getDmSupport();
    Integer[] getArgs();

    class Input {
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
