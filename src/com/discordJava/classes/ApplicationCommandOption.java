package com.discordJava.classes;

public class ApplicationCommandOption {
    Integer type;
    String name;
    String description;
    Boolean required;
    ApplicationCommandOptionChoice[] choices;
    ApplicationCommandOption[] options;
}
