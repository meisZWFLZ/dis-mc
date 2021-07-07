package com.discordJava.classes;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Arrays;

public class Message {
    public Snowflake id;
    public Snowflake channel_id;
    public Snowflake guild_id;
    public User author;
    public GuildMember member;
    public String content;
    public Timestamp timestamp;
    public Timestamp edited_timestamp;
    public Boolean tts;
    public Boolean mention_everyone;
    public User[] mentions;
    public String[] mentions_roles;
    public ChannelMention[] mentions_channels;
    public Attachment[] attachments;
    public Embed[] embeds;
    public Reaction[] reactions;
    public Boolean pinned;
    public Snowflake webhook_id;
    public Integer type;
    public MessageActivity activity;
    public Application application;
    public Snowflake application_id;
    public MessageReference message_reference;
    public Integer flags;
    public Sticker[] stickers;
    public Message referenced_message;
    public MessageInteraction interaction;
    public Channel thread;
    public MessageComponent[] components;
    public Client client;

    public Message() {
    }

    public Message(Client client, Snowflake channelId, Snowflake messageId) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://discord.com/api/v" + client.version + "/channels/" + channelId + "/messages/" + messageId))
                .headers("Authorization", "Bot " + client.token())
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        Message msg = new Gson().fromJson(HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString()).body(), Message.class);
        this.channel_id = msg.channel_id;
        this.id = msg.id;
        this.guild_id = msg.guild_id;
        this.content = msg.content;
        this.activity = msg.activity;
        this.application = msg.application;
        this.application_id = msg.application_id;
        this.attachments = msg.attachments;
        this.author = msg.author;
        this.components = msg.components;
        this.edited_timestamp = msg.edited_timestamp;
        this.embeds = msg.embeds;
        this.flags = msg.flags;
        this.interaction = msg.interaction;
        this.member = msg.member;
        this.mention_everyone = msg.mention_everyone;
        this.mentions = msg.mentions;
        this.mentions_channels = msg.mentions_channels;
        this.mentions_roles = msg.mentions_roles;
        this.message_reference = msg.message_reference;
        this.pinned = msg.pinned;
        this.reactions = msg.reactions;
        this.referenced_message = msg.referenced_message;
        this.stickers = msg.stickers;
        this.thread = msg.thread;
        this.timestamp = msg.timestamp;
        this.tts = msg.tts;
        this.type = msg.type;
        this.webhook_id = msg.webhook_id;

        this.client = client;
    }

    private Message(Message.Builder msg) {
        this.channel_id = msg.channel_id;
        this.id = msg.id;
        this.guild_id = msg.guild_id;
        this.content = msg.content;
        this.activity = msg.activity;
        this.application = msg.application;
        this.application_id = msg.application_id;
        this.attachments = msg.attachments;
        this.author = msg.author;
        this.components = msg.components;
        this.edited_timestamp = msg.edited_timestamp;
        this.embeds = msg.embeds;
        this.flags = msg.flags;
        this.interaction = msg.interaction;
        this.member = msg.member;
        this.mention_everyone = msg.mention_everyone;
        this.mentions = msg.mentions;
        this.mentions_channels = msg.mentions_channels;
        this.mentions_roles = msg.mentions_roles;
        this.message_reference = msg.message_reference;
        this.pinned = msg.pinned;
        this.reactions = msg.reactions;
        this.referenced_message = msg.referenced_message;
        this.stickers = msg.stickers;
        this.thread = msg.thread;
        this.timestamp = msg.timestamp;
        this.tts = msg.tts;
        this.type = msg.type;
        this.webhook_id = msg.webhook_id;
        this.client = msg.client;
    }

    public Message send() throws IOException, InterruptedException, URISyntaxException {
        try {
            String form = Client.GSON.toJson(this, Message.class);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI("https://discord.com/api/v" + this.client.version + "/channels/" + this.channel_id + "/messages"))
                    .headers("Authorization", "Bot " + this.client.token(), "Content-type", "application/json; charset=UTF-8")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();
            return Client.GSON.fromJson(client.send(req, HttpResponse.BodyHandlers.ofString()).body(), Message.class);
        } catch (Exception e) {
            System.out.println("Message failed to send, error: " + e);
            return null;
        }
    }

    public Message reply(Message message) throws URISyntaxException, IOException, InterruptedException {
        String form = new Gson().toJson(message.builder().message_reference(MessageReference.newBuilder(this)).type(Type.REPLY).build(), Message.class);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://discord.com/api/v" + this.client.version + "/channels/" + this.channel_id + "/messages"))
                .headers("Authorization", "Bot " + this.client.token(), "Content-type", "application/json; charset=UTF-8")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        return new Gson().fromJson(client.send(req, HttpResponse.BodyHandlers.ofString()).body(), Message.class);
    }

    public Message reply(String msgContent) throws URISyntaxException, IOException, InterruptedException {
        String form = new Gson().toJson(Message.newBuilder(this.client).content(msgContent).message_reference(MessageReference.newBuilder(this)).type(Type.REPLY).build(), Message.class);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://discord.com/api/v" + this.client.version + "/channels/" + this.channel_id + "/messages"))
                .headers("Authorization", "Bot " + this.client.token(), "Content-type", "application/json; charset=UTF-8")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        return new Gson().fromJson(client.send(req, HttpResponse.BodyHandlers.ofString()).body(), Message.class);
    }

    public Channel channel() throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://discord.com/api/v" + this.client.version + "/channels/" + this.channel_id))
                .headers("Authorization", "Bot " + this.client.token())
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        Channel channel = new Gson().fromJson(client.send(req, HttpResponse.BodyHandlers.ofString()).body(), Channel.class);
        channel.client = this.client;
        return channel;
    }

    public Type type() {
        return Message.Type.values()[this.type];
    }

    public static Message.Builder newBuilder(Client client) {
        return new Message.Builder(client);
    }

    public Message.Builder builder() {
        return new Message.Builder(this);
    }

    public enum Flag {
        CROSSPOSTED, IS_CROSSPOST,
        SUPPRESS_EMBEDS, SOURCE_MESSAGE_DELETED,
        URGENT, HAS_THREAD,
        EPHEMERAL, LOADING;

        private static final Integer[] INT_ARRAY = new Integer[]{1, 2, 4, 8, 16, 32, 64, 128, 256};

        public int getInt(){
            return 1 << Arrays.asList(Flag.values()).indexOf(this);
        }

        public static int getInt(Flag flag) {
            return 1 << Arrays.asList(Flag.values()).indexOf(flag);
        }

        public static int getInt(String flag) {
            return 1 << Arrays.asList(Flag.values()).indexOf(Flag.valueOf(flag));
        }

//        public static Flag[] getFlags(Integer flags) {
//            return Arrays.stream(Flag.values()).filter(x -> );
//        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    public enum Type {
        DEFAULT, RECIPIENT_ADD,
        RECIPIENT_REMOVE, CALL,
        CHANNEL_NAME_CHANGE, CHANNEL_ICON_CHANGE,
        CHANNEL_PINNED_MESSAGE, GUILD_MEMBER_JOIN,
        USER_PREMIUM_GUILD_SUBSCRIPTION, USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_1,
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_2, USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_3,
        CHANNEL_FOLLOW_ADD, GUILD_DISCOVERY_DISQUALIFIED,
        GUILD_DISCOVERY_REQUALIFIED, GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING,
        GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING, THREAD_CREATED,
        REPLY, APPLICATION_COMMAND,
        THREAD_STARTER_MESSAGE, GUILD_INVITE_REMINDER;
    }

    public static class Builder extends Message {

        Builder(Client client) {
            this.client = client;
        }

        Builder(Message msg) {
            this.channel_id = msg.channel_id;
            this.id = msg.id;
            this.guild_id = msg.guild_id;
            this.content = msg.content;
            this.activity = msg.activity;
            this.application = msg.application;
            this.application_id = msg.application_id;
            this.attachments = msg.attachments;
            this.author = msg.author;
            this.components = msg.components;
            this.edited_timestamp = msg.edited_timestamp;
            this.embeds = msg.embeds;
            this.flags = msg.flags;
            this.interaction = msg.interaction;
            this.member = msg.member;
            this.mention_everyone = msg.mention_everyone;
            this.mentions = msg.mentions;
            this.mentions_channels = msg.mentions_channels;
            this.mentions_roles = msg.mentions_roles;
            this.message_reference = msg.message_reference;
            this.pinned = msg.pinned;
            this.reactions = msg.reactions;
            this.referenced_message = msg.referenced_message;
            this.stickers = msg.stickers;
            this.thread = msg.thread;
            this.timestamp = msg.timestamp;
            this.tts = msg.tts;
            this.type = msg.type;
            this.webhook_id = msg.webhook_id;
            this.client = msg.client;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder attachments(Attachment[] attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder author(User author) {
            this.author = author;
            return this;
        }

        public Builder channel_id(String channel_id) {
            this.channel_id = new Snowflake(channel_id);
            return this;
        }

        public Builder channel_id(Snowflake channel_id) {
            this.channel_id = channel_id;
            return this;
        }

        public Builder guild_id(String guild_id) {
            this.guild_id = new Snowflake(guild_id);
            return this;
        }

        public Builder guild_id(Snowflake guild_id) {
            this.guild_id = guild_id;
            return this;
        }

        public Builder id(String id) {
            this.id = new Snowflake(id);
            return this;
        }

        public Builder id(Snowflake id) {
            this.id = id;
            return this;
        }

        public Builder edited_timestamp(Timestamp edited_timestamp) {
            this.edited_timestamp = edited_timestamp;
            return this;
        }

        public Builder member(GuildMember member) {
            this.member = member;
            return this;
        }

        public Builder mention_everyone(Boolean mention_everyone) {
            this.mention_everyone = mention_everyone;
            return this;
        }

        public Builder mentions(User[] mentions) {
            this.mentions = mentions;
            return this;
        }

        public Builder timestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder mentions_channels(ChannelMention[] mentions_channels) {
            this.mentions_channels = mentions_channels;
            return this;
        }

        public Builder embeds(Embed[] embeds) {
            this.embeds = embeds;
            return this;
        }

        public Builder mentions_roles(String[] mentions_roles) {
            this.mentions_roles = mentions_roles;
            return this;
        }

        public Builder tts(Boolean tts) {
            this.tts = tts;
            return this;
        }

        public Builder activity(MessageActivity activity) {
            this.activity = activity;
            return this;
        }

        public Builder application(Application application) {
            this.application = application;
            return this;
        }

        public Builder application_id(String application_id) {
            this.application_id = new Snowflake(application_id);
            return this;
        }

        public Builder application_id(Snowflake application_id) {
            this.application_id = application_id;
            return this;
        }

        public Builder components(MessageComponent[] components) {
            this.components = components;
            return this;
        }

        public Builder flags(Integer flags) {
            this.flags = flags;
            return this;
        }

        public Builder pinned(Boolean pinned) {
            this.pinned = pinned;
            return this;
        }

        public Builder interaction(MessageInteraction interaction) {
            this.interaction = interaction;
            return this;
        }

        public Builder message_reference(MessageReference message_reference) {
            this.message_reference = message_reference;
            return this;
        }

        public Builder reactions(Reaction[] reactions) {
            this.reactions = reactions;
            return this;
        }

        public Builder referenced_message(Message referenced_message) {
            this.referenced_message = referenced_message;
            return this;
        }

        public Builder stickers(Sticker[] stickers) {
            this.stickers = stickers;
            return this;
        }

        public Builder thread(Channel thread) {
            this.thread = thread;
            return this;
        }

        public Builder type(Integer type) {
            this.type = type;
            return this;
        }

        public Builder type(Message.Type type) {
            this.type = Arrays.asList(Message.Type.values()).indexOf(type);
            return this;
        }

        public Builder webhook_id(String webhook_id) {
            this.webhook_id = new Snowflake(webhook_id);
            return this;
        }

        public Builder webhook_id(Snowflake webhook_id) {
            this.webhook_id = webhook_id;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
