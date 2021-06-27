package com.discordJava.classes;

public class InteractionApplicationCommandCallbackData extends DiscordSuperClass {
    public Boolean tts;
    public String content;
    public Embed[] embeds;
    public AllowedMentions allowed_mentions;
    public Integer flags;
    public MessageComponent components;

    public InteractionApplicationCommandCallbackData() {
        this.tts = null;
        this.content = null;
        this.embeds = null;
        this.allowed_mentions = null;
        this.flags = null;
        this.components = null;
    }
}
