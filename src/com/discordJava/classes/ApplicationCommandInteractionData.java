package com.discordJava.classes;

public class ApplicationCommandInteractionData extends DiscordSuperClass {
    public String id;
    public String name;
    public ApplicationCommandInteractionDataResolved resolved;
    public ApplicationCommandInteractionDataOption[] options;
    public String custom_id;
    public Integer component_type;
}
