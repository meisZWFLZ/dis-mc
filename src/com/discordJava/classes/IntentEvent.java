package com.discordJava.classes;

import com.discordJava.events.*;

import java.util.Arrays;

/**
 * <code> An enumerator used for intents and stuff</code>
 *
 * @author meisWFLZ
 */

public enum IntentEvent {
    GUILDS(new GatewayEvent[]{
            new GuildCreate(),
            new GuildUpdate(),
            new GuildDelete(),
            new GuildRoleCreate(),
            new GuildRoleUpdate(),
            new GuildRoleDelete(),
            new ChannelCreate(),
            new ChannelUpdate(),
            new ChannelDelete(),
            new ChannelPinsUpdate(),
            new ThreadCreate(),
            new ThreadUpdate(),
            new ThreadDelete(),
            new ThreadListSync(),
            new ThreadMemberUpdate(),
            new ThreadMembersUpdate(),
            new StageInstanceCreate(),
            new StageInstanceUpdate(),
            new StageInstanceDelete()
    }),
    GUILD_MEMBERS(new GatewayEvent[]{
            new GuildMemberAdd(),
            new GuildMemberUpdate(),
            new GuildMemberRemove(),
            new ThreadMembersUpdate()
    }),
    GUILD_BANS(new GatewayEvent[]{
            new GuildBanAdd(),
            new GuildBanRemove()
    }),
    GUILD_EMOJIS(new GuildEmojisUpdate()),
    GUILD_INTEGRATIONS(new GatewayEvent[]{
            new GuildIntegrationsUpdate(),
            new IntegrationCreate(),
            new IntegrationUpdate(),
            new IntegrationDelete()
    }),
    GUILD_WEBHOOKS(new WebhooksUpdate()),
    GUILD_INVITES(new GatewayEvent[]{
            new InviteCreate(),
            new InviteDelete()
    }),
    GUILD_VOICE_STATES(new VoiceStateUpdate()),
    GUILD_PRESENCES(new PresenceUpdate()),
    GUILD_MESSAGES(new GatewayEvent[]{
            new MessageCreate(),
            new MessageUpdate(),
            new MessageDelete(),
            new MessageDeleteBulk()
    }),
    GUILD_MESSAGE_REACTIONS(new GatewayEvent[]{
            new MessageReactionAdd(),
            new MessageReactionRemove(),
            new MessageReactionRemoveAll(),
            new MessageReactionRemoveEmoji()
    }),
    GUILD_MESSAGE_TYPING(new TypingStart()),
    DIRECT_MESSAGES(new GatewayEvent[]{
            new MessageCreate(),
            new MessageUpdate(),
            new MessageDelete(),
            new ChannelPinsUpdate()
    }),
    DIRECT_MESSAGE_REACTIONS(new GatewayEvent[]{
            new MessageReactionAdd(),
            new MessageReactionRemove(),
            new MessageReactionRemoveAll(),
            new MessageReactionRemoveEmoji()
    }),
    DIRECT_MESSAGE_TYPING(new TypingStart()),
    PASSTHROUGH(new GatewayEvent[]{
            new Ready(),
            new Resumed(),
            new VoiceServerUpdate(),
            new UserUpdate(),
            new ApplicationCommandCreate(),
            new ApplicationCommandUpdate(),
            new ApplicationCommandCreate(),
            new InteractionCreate()
    });

    public static GatewayEvent[] EVENT_ARRAY = new GatewayEvent[]{
            new GuildCreate(), new GuildUpdate(), new GuildDelete(), new GuildRoleCreate(), new GuildRoleUpdate(), new GuildRoleDelete(),
            new ChannelCreate(), new ChannelUpdate(), new ChannelDelete(), new ChannelPinsUpdate(), new ThreadCreate(), new ThreadUpdate(),
            new ThreadDelete(), new ThreadListSync(), new ThreadMemberUpdate(), new ThreadMembersUpdate(), new StageInstanceCreate(), new StageInstanceUpdate(),
            new StageInstanceDelete(), new GuildMemberAdd(), new GuildMemberUpdate(), new GuildMemberRemove(), new ThreadMembersUpdate(), new GuildBanAdd(),
            new GuildBanRemove(), new GuildEmojisUpdate(), new GuildIntegrationsUpdate(), new IntegrationCreate(), new IntegrationUpdate(), new IntegrationDelete(),
            new WebhooksUpdate(), new InviteCreate(), new InviteDelete(), new VoiceStateUpdate(), new PresenceUpdate(), new MessageCreate(),
            new MessageUpdate(), new MessageDelete(), new MessageDeleteBulk(), new MessageReactionAdd(), new MessageReactionRemove(), new MessageReactionRemoveAll(),
            new MessageReactionRemoveEmoji(), new TypingStart(), new MessageCreate(), new MessageUpdate(), new MessageDelete(), new ChannelPinsUpdate(),
            new MessageReactionAdd(), new MessageReactionRemove(), new MessageReactionRemoveAll(), new MessageReactionRemoveEmoji(), new TypingStart(), new Ready(),
            new Resumed(), new VoiceServerUpdate(), new UserUpdate(), new ApplicationCommandCreate(), new ApplicationCommandUpdate(), new ApplicationCommandCreate(),
            new InteractionCreate()
    };

    public static String[] INTENTS = new String[]{
            "GUILDS", "GUILD_MEMBERS", "GUILD_BANS", "GUILD_EMOJIS",
            "GUILD_INTEGRATIONS", "GUILD_WEBHOOKS", "GUILD_INVITES",
            "GUILD_VOICE_STATES", "GUILD_PRESENCES", "GUILD_MESSAGES",
            "GUILD_MESSAGE_REACTIONS", "GUILD_MESSAGE_TYPING", "DIRECT_MESSAGES",
            "DIRECT_MESSAGE_REACTIONS", "DIRECT_MESSAGE_TYPING", "PASSTHROUGH"
    };

    private final GatewayEvent[] eventList;

    IntentEvent(GatewayEvent event) {
        this.eventList = new GatewayEvent[]{event};
    }

    IntentEvent(GatewayEvent[] events) {
        this.eventList = events;
    }

    public GatewayEvent[] getClasses() {
        return this.eventList;
    }


    public String[] getNames() {
        return Arrays.stream(this.eventList).map(GatewayEvent::getEventName).toList().toArray(new String[]{});
    }
}
