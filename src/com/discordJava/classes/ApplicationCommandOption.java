package com.discordJava.classes;

public class ApplicationCommandOption extends DiscordSuperClass {
    Integer type;
    String name;
    String description;
    Boolean required;
    ApplicationCommandOptionChoice[] choices;
    ApplicationCommandOption[] options;
}
