package com.discordJava.classes;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Channel {
    public Snowflake id;
    public Integer type;
    public Snowflake guild_id;
    public Integer position;
    public Overwrite permission_overwrites;
    public String name;
    public String topic;
    public Boolean nsfw;
    public Snowflake last_message_id;
    public Integer bitrate;
    public Integer user_limit;
    public Integer rate_limit_per_user;
    public User[] recipients;
    public String icon;
    public Snowflake owner_id;
    public Snowflake application_id;
    public Snowflake parent_id;
    public Timestamp last_pin_timestamp;
    public String rtc_region;
    public Integer video_quality_mode;
    public Integer message_count;
    public Integer member_count;
    public ThreadMetadata thread_metadata;
    public ThreadMember member;
    public Client client;

    public Message send(Message message) throws URISyntaxException, IOException, InterruptedException {
        HashMap<String, Serializable> formMap = new HashMap<String, Serializable>((Map) message);
        String form = new Gson().toJson(formMap, HashMap.class);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://discord.com/api/v" + this.client.version + "/channels/" + this.id + "/messages"))
                .headers("Authorization", "Bot " + this.client.token(), "Content-type", "application/json; charset=UTF-8")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        return new Gson().fromJson(client.send(req, HttpResponse.BodyHandlers.ofString()).body(), Message.class);
    }
}
