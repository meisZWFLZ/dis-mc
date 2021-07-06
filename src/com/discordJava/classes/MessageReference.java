package com.discordJava.classes;

public class MessageReference {
    public Snowflake message_id;
    public Snowflake channel_id;
    public Snowflake guild_id;
    public Boolean fail_if_not_exists;
    public Client client;

    public static MessageReference.Builder newBuilder(Client client) {
        return new MessageReference.Builder(client);
    }

    public static MessageReference.Builder newBuilder(Message msg) {
        return new MessageReference.Builder(msg.client).message_id(msg.id).channel_id(msg.channel_id).guild_id(msg.guild_id);
    }

    public MessageReference.Builder builder() {
        return new MessageReference.Builder(this);
    }

    public static class Builder extends MessageReference {
        public Builder(Client client) {
            this.client = client;
        }

        public Builder(MessageReference ref) {
            this.message_id = ref.message_id;
            this.channel_id = ref.channel_id;
            this.guild_id = ref.guild_id;
            this.fail_if_not_exists = ref.fail_if_not_exists;
        }

        public Builder message_id(Snowflake message_id) {
            this.message_id = message_id;
            return this;
        }

        public Builder channel_id(Snowflake channel_id) {
            this.channel_id = channel_id;
            return this;
        }

        public Builder guild_id(Snowflake guild_id) {
            this.guild_id = guild_id;
            return this;
        }

        public Builder channel_id(Boolean fail_if_not_exists) {
            this.fail_if_not_exists = fail_if_not_exists;
            return this;
        }

        public MessageReference build() {
            return this;
        }
    }
}
