package com.discordJava.classes;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class User extends DiscordSuperClass {
    public String id;
    public String username;
    public String discriminator;
    public String avatar;
    public Boolean bot;
    public Boolean system;
    public Boolean mfa_enabled;
    public String locale;
    public Boolean verified;
    public String email;
    public Integer flags;
    public Integer premium_type;
    public Integer public_flags;
    public Client client;

    public User(String token) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://discord.com/api/v8/users/@me"))
                .headers("Authorization", "Bot " + token)
                .build();
        User user = new Gson().fromJson(HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString()).body(), User.class);
        this.id = user.id;
        this.username = user.username;
        this.discriminator = user.discriminator;
        this.avatar = user.avatar;
        this.bot = user.bot;
        this.system = user.system;
        this.mfa_enabled = user.mfa_enabled;
        this.locale = user.locale;
        this.verified = user.verified;
        this.email = user.email;
        this.flags = user.flags;
        this.public_flags = user.public_flags;
        this.premium_type = user.premium_type;
        this.client = null;
    }

    public enum Flags {
        None,
        DiscordEmployee,
        PartneredServerOwner,
        HypeSquadEvents,
        BugHunterLevel1,
        HouseBravery,
        HouseBrilliance,
        HouseBalance,
        EarlySupporter,
        TeamUser,
        BugHunterLevel2,
        VerifiedBot,
        EarlyVerifiedBotDeveloper,
        DiscordCertifiedModerator
    }

    public enum PremiumType {
        None,
        NitroClassic,
        Nitro
    }
}
