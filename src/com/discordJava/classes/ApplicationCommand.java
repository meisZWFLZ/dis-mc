package com.discordJava.classes;

public class ApplicationCommand extends DiscordSuperClass {
    String id;
    String application_id;
    String guild_id;
    String name;
    String description;
    ApplicationCommandOption[] options;
    Boolean default_permissions;
}
