package com.discordJava;

import com.discordJava.classes.Client;
import com.discordJava.classes.Interaction;
import com.discordJava.classes.Message;
import com.discordJava.commands.Command;
import com.discordJava.events.InteractionCreate;
import com.discordJava.events.MessageCreate;
import com.discordJava.slashCommands.SlashCommand;
import com.discordJava.slashCommands.guilds.g818273182208753714.Bleq;

import java.util.Arrays;
import java.util.List;

public class DiscordBot extends Client {
    private static final String[] intents = new String[]{
            "GUILD_MESSAGES",
            "DIRECT_MESSAGES"
    };
    private static final Byte version = 8;
    private final String prefix = "!";

    private final CommandHandler commandHandler;
    private final SlashHandler slashHandler;

    private static final SlashCommand[] slashList = new SlashCommand[]{
            new Bleq()
    };

    DiscordBot(String token) throws Exception {
        super(token, intents);

        this.commandHandler = new CommandHandler(this.prefix, this);
        this.slashHandler = new SlashHandler(this);

        super.on("MESSAGE_CREATE", this.commandHandler);
        super.on("INTERACTION_CREATE", this.slashHandler);

        super.login();
    }

    public static class SlashHandler implements Client.EventHandler.Listener<InteractionCreate> {
        private final Client client;

        private static final Boolean once = false;
        private static final String name = "SlashHandler";

        SlashHandler(Client client) {
            this.client = client;
        }

        @Override
        public void execute(Client.EventHandler.Input<InteractionCreate> input) {
            Interaction interaction = input.data;
            interaction.client = this.client;

            try {
                interaction.ack();
            } catch (Exception e) {
                System.out.println("interaction acknowledgement failed: " + e);
                return;
            }

            SlashCommand cmd = null;
            try {
                List<SlashCommand> cmdList = Arrays.stream(slashList).filter(x -> x.getId().equals(interaction.data.id)).toList();
                if (cmdList.size() > 0) cmd = cmdList.get(0);
            } catch (Exception e) {
                System.out.println(e);
                return;
            }

            if (cmd == null) return;
            try {
                cmd.execute(new SlashCommand.Input(this.client, interaction));
            } catch (Exception e) {
                System.out.println("slash command created error: " + e);
            }
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static class CommandHandler implements Client.EventHandler.Listener<MessageCreate> {
        private final String prefix;
        private final Client client;

        private static final Boolean once = false;
        private static final String name = "CommandHandler";

        CommandHandler(String prefix, Client client) {
            this.prefix = prefix;
            this.client = client;
        }

        @Override
        public void execute(Client.EventHandler.Input<MessageCreate> input) {
            Message msg = input.data;

            String content = msg.content;
            System.out.printf("message content: \"%s\"\n", content);

            if (!content.startsWith(this.prefix)) return;
            System.out.println("starts with prefix!");

            String[] args = content.substring(this.prefix.length()).split("\s");
            String command = args[0].toLowerCase();
            System.out.printf("command: %s\n", command);

            Command cmd = null;
            try {
                List<Command> cmdList = Arrays.stream(Command.LIST).filter(x -> x.name.equals(command) || (Arrays.stream(x.aliases).toList().contains(command))).toList();
                if (cmdList.size() > 0) cmd = cmdList.get(0);
            } catch (Exception e) {
                System.out.println(e);
                return;
            }

            if (cmd == null) return;
            Integer[] cArgs = cmd.args;
            System.out.println(cArgs[0] != args.length);

            if ((cArgs.length == 1) ? (cArgs[0] == args.length) : ((args.length >= cArgs[0]) && (args.length <= cArgs[1])))
                cmd.execute(new Command.Input(this.prefix, this.client, args, content, command, msg, version));
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
