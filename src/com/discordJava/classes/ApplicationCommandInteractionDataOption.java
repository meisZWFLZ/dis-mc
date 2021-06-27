package com.discordJava.classes;

public class ApplicationCommandInteractionDataOption extends DiscordSuperClass {
    public String name;
    public Integer type;
    public Object value;
    public ApplicationCommandInteractionDataOption[] options;

    public String getType() {
        return type(this.type);
    }

    public static String getType(Integer type) {
        return type(type);
    }

    private static String type(Integer type) {
        return switch (type) {
            case 1 -> "SUB_COMMAND";
            case 2 -> "SUB_COMMAND_GROUP";
            case 3 -> "STRING";
            case 4 -> "INTEGER";
            case 5 -> "BOOLEAN";
            case 6 -> "USER";
            case 7 -> "CHANNEL";
            case 8 -> "ROLE";
            case 9 -> "MENTIONABLE";
            default -> null;
        };
    }
}
