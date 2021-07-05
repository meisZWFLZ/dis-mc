package com.discordJava.classes;

import com.discordJava.events.*;
import com.discordJava.gateway.DiscordGateway;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class Client {
    public String token;
    private HashMap<String, EventHandler<?>> eventHandlers;
    private User user = null;
    private Guild[] guilds = null;
    private Application application = null;
    private String session_id;
    private String[] intents = new String[]{
            "GUILD_MESSAGES", "DIRECT_MESSAGES"
    };
    private DiscordGateway gateway;
    public Byte version = 8;
    public static final Gson GSON = new GsonBuilder().setExclusionStrategies(new GsonClientExclusionStrategy()).create();
    private final Client client;

    public Client(String[] intents) throws Exception {
        this.intents = intents;
        this.client = this;
        this.eventHandlerSetUp();
    }

    public Client(String token, String[] intents) throws Exception {
        this.intents = intents;
        this.token = token;
        this.client = this;
        this.eventHandlerSetUp();
    }

    public Client(String token) {
        this.token = token;
        this.client = this;
        this.eventHandlerSetUp();
    }

    public void login(String token) throws Exception {
        this.token = token;
        this.gateway = new DiscordGateway(this.token, this.intents, this.version);
        this.gateway.on("READY", this::readyListener);
        this.gateway.on(0, this::dispatchListener);
    }

    public void login() throws Exception {
        if (this.token == null) throw new Error("token must be defined");
        this.gateway = new DiscordGateway(this.token, this.intents, this.version);
        this.gateway.on("READY", this::readyListener);
        this.gateway.on(0, this::dispatchListener);
    }

    public Object dispatchListener(HashMap<?, ?> payload) {
        EventHandler<?> handler = eventHandlers.get((String) payload.get("t"));
        if (handler == null) return null;
        handler.handler(payload);

        return null;
    }

    private Object readyListener(HashMap payload) {
        String payloadStr = GSON.toJson(payload, HashMap.class);
        Ready ready = GSON.fromJson(payloadStr, Ready.class);

        this.user = ready.user;
        this.guilds = ready.guilds;
        this.application = ready.application;
        this.session_id = ready.session_id;

        return null;
    }

    private void eventHandlerSetUp() {
        String[] intents;
        this.eventHandlers = new HashMap<String, EventHandler<?>>();
        if(!Arrays.stream(this.intents).toList().contains("PASSTHROUGH")) {
            ArrayList<String> intentList = new ArrayList<String>(Arrays.asList(this.intents));
            intentList.add("PASSTHROUGH");
            intents = intentList.toArray(new String[]{});
        } else intents = this.intents;
        for (IntentEvent intent : Arrays.stream(IntentEvent.values()).filter(x -> Arrays.stream(intents).toList().contains(x.name())).toList().toArray(new IntentEvent[]{})) {
            for (GatewayEvent event : intent.getClasses()) {
                this.eventHandlers.put(event.getEventName(),
                        switch (event.getEventName()) {
                            case "STAGE_INSTANCE_DELETE" -> new EventHandler<StageInstanceDelete>(this, StageInstanceDelete.class);
                            case "RESUMED" -> new EventHandler<Resumed>(this, Resumed.class);
                            case "MESSAGE_DELETE" -> new EventHandler<MessageDelete>(this, MessageDelete.class);
                            case "INTEGRATION_DELETE" -> new EventHandler<IntegrationDelete>(this, IntegrationDelete.class);
                            case "APPLICATION_COMMAND_CREATE" -> new EventHandler<ApplicationCommandCreate>(this, ApplicationCommandCreate.class);
                            case "GUILD_BAN_ADD" -> new EventHandler<GuildBanAdd>(this, GuildBanAdd.class);
                            case "THREAD_DELETE" -> new EventHandler<ThreadDelete>(this, ThreadDelete.class);
                            case "CHANNEL_CREATE" -> new EventHandler<ChannelCreate>(this, ChannelCreate.class);
                            case "INTEGRATION_UPDATE" -> new EventHandler<IntegrationUpdate>(this, IntegrationUpdate.class);
                            case "STAGE_INSTANCE_UPDATE" -> new EventHandler<StageInstanceUpdate>(this, StageInstanceUpdate.class);
                            case "READY" -> new EventHandler<Ready>(this, Ready.class);
                            case "INVITE_CREATE" -> new EventHandler<InviteCreate>(this, InviteCreate.class);
                            case "CHANNEL_PINS_UPDATE" -> new EventHandler<ChannelPinsUpdate>(this, ChannelPinsUpdate.class);
                            case "THREAD_MEMBERS_UPDATE" -> new EventHandler<ThreadMembersUpdate>(this, ThreadMembersUpdate.class);
                            case "THREAD_UPDATE" -> new EventHandler<ThreadUpdate>(this, ThreadUpdate.class);
                            case "PRESENCE_UPDATE" -> new EventHandler<PresenceUpdate>(this, PresenceUpdate.class);
                            case "MESSAGE_REACTION_REMOVE_ALL" -> new EventHandler<MessageReactionRemoveAll>(this, MessageReactionRemoveAll.class);
                            case "MESSAGE_CREATE" -> new EventHandler<MessageCreate>(this, MessageCreate.class);
                            case "GUILD_ROLE_DELETE" -> new EventHandler<GuildRoleDelete>(this, GuildRoleDelete.class);
                            case "VOICE_STATE_UPDATE" -> new EventHandler<VoiceStateUpdate>(this, VoiceStateUpdate.class);
                            case "INTERACTION_CREATE" -> new EventHandler<InteractionCreate>(this, InteractionCreate.class);
                            case "MESSAGE_REACTION_REMOVE_EMOJI" -> new EventHandler<MessageReactionRemoveEmoji>(this, MessageReactionRemoveEmoji.class);
                            case "GUILD_MEMBER_REMOVE" -> new EventHandler<GuildMemberRemove>(this, GuildMemberRemove.class);
                            case "STAGE_INSTANCE_CREATE" -> new EventHandler<StageInstanceCreate>(this, StageInstanceCreate.class);
                            case "GUILD_DELETE" -> new EventHandler<GuildDelete>(this, GuildDelete.class);
                            case "MESSAGE_UPDATE" -> new EventHandler<MessageUpdate>(this, MessageUpdate.class);
                            case "GUILD_MEMBER_UPDATE" -> new EventHandler<GuildMemberUpdate>(this, GuildMemberUpdate.class);
                            case "CHANNEL_DELETE" -> new EventHandler<ChannelDelete>(this, ChannelDelete.class);
                            case "APPLICATION_COMMAND_UPDATE" -> new EventHandler<ApplicationCommandUpdate>(this, ApplicationCommandUpdate.class);
                            case "GUILD_CREATE" -> new EventHandler<GuildCreate>(this, GuildCreate.class);
                            case "GUILD_MEMBER_ADD" -> new EventHandler<>(this, GuildMemberAdd.class);
                            case "GUILD_INTEGRATIONS_UPDATE" -> new EventHandler<GuildIntegrationsUpdate>(this, GuildIntegrationsUpdate.class);
                            case "THREAD_LIST_SYNC" -> new EventHandler<ThreadListSync>(this, ThreadListSync.class);
                            case "THREAD_CREATE" -> new EventHandler<ThreadCreate>(this, ThreadCreate.class);
                            case "GUILD_ROLE_UPDATE" -> new EventHandler<GuildRoleUpdate>(this, GuildRoleUpdate.class);
                            case "THREAD_MEMBER_UPDATE" -> new EventHandler<ThreadMemberUpdate>(this, ThreadMemberUpdate.class);
                            case "MESSAGE_DELETE_BULK" -> new EventHandler<MessageDeleteBulk>(this, MessageDeleteBulk.class);
                            case "INTEGRATION_CREATE" -> new EventHandler<IntegrationCreate>(this, IntegrationCreate.class);
                            case "WEBHOOKS_UPDATE" -> new EventHandler<WebhooksUpdate>(this, WebhooksUpdate.class);
                            case "USER_UPDATE" -> new EventHandler<UserUpdate>(this, UserUpdate.class);
                            case "MESSAGE_REACTION_ADD" -> new EventHandler<MessageReactionAdd>(this, MessageReactionAdd.class);
                            case "INVITE_DELETE" -> new EventHandler<InviteDelete>(this, InviteDelete.class);
                            case "GUILD_EMOJIS_UPDATE" -> new EventHandler<GuildEmojisUpdate>(this, GuildEmojisUpdate.class);
                            case "TYPING_START" -> new EventHandler<TypingStart>(this, TypingStart.class);
                            case "VOICE_SERVER_UPDATE" -> new EventHandler<VoiceServerUpdate>(this, VoiceServerUpdate.class);
                            case "CHANNEL_UPDATE" -> new EventHandler<ChannelUpdate>(this, ChannelUpdate.class);
                            case "GUILD_BAN_REMOVE" -> new EventHandler<GuildBanRemove>(this, GuildBanRemove.class);
                            case "GUILD_UPDATE" -> new EventHandler<GuildUpdate>(this, GuildUpdate.class);
                            case "GUILD_ROLE_CREATE" -> new EventHandler<GuildRoleCreate>(this, GuildRoleCreate.class);
                            case "MESSAGE_REACTION_REMOVE" -> new EventHandler<MessageReactionRemove>(this, MessageReactionRemove.class);
                            default -> null;
                        });
            }
        }
    }

    public <E extends GatewayEvent> EventHandler<E> on(String eventName, EventHandler.Listener<E> listener) throws IOException {
        return this.addListener(eventName, -1, listener);
    }

    public <E extends GatewayEvent> EventHandler<E> addListenerAt(String eventName, Integer index, EventHandler.Listener<E> listener) throws IOException {
        return this.addListener(eventName, index, listener);
    }

    public <E extends GatewayEvent> EventHandler<E> prependListener(String eventName, EventHandler.Listener<E> listener) throws IOException {
        return this.addListener(eventName, 0, listener);
    }

    private <E extends GatewayEvent> EventHandler<E> addListener(String eventName, Integer i, EventHandler.Listener<E> listener) throws IOException {
        String name = eventName.toUpperCase().replaceAll("\\s", "_");
        if (!this.eventHandlers.containsKey(name))
            if (Arrays.stream(IntentEvent.getEventArray()).map(GatewayEvent::getEventName).toList().contains(name.toUpperCase()))
                throw new IOException(
                        "\"" + eventName.toUpperCase() + "\" was not included in intents, to do so add the \"" +
                                Arrays.stream(IntentEvent.values()).filter(x -> Arrays.stream(x.getNames()).toList().contains(name)).toList().toArray(new IntentEvent[]{})[0].name() +
                                "\" to your intents next time you initialize this client class."
                );
            else throw new IOException("\"" + eventName.toUpperCase() + "\" was an invalid event name");
        EventHandler<E> eventHandler = (EventHandler<E>) this.eventHandlers.get(name);
        int size = eventHandler.listeners.size();
        int index = i < 0 ? size : i;
        if (size < index) throw new IOException("index " + index + " was out of bounds for size of " + size);
        eventHandler.listeners.add(index, listener);
        this.eventHandlers.put(name, eventHandler);
        return eventHandler;
    }

    public <E extends GatewayEvent> EventHandler<E> off(String eventName, EventHandler.Listener<E> listener) throws IOException {
        return this.removeListener(eventName, null, listener);
    }

    public <E extends GatewayEvent> EventHandler<E> off(String eventName, Integer index) throws IOException {
        return this.removeListener(eventName, index, null);
    }

    public <E extends GatewayEvent> EventHandler<E> off(String eventName) throws IOException {
        return this.removeListener(eventName, -1, null);
    }

    private <E extends GatewayEvent> EventHandler<E> removeListener(String eventName, Integer i, EventHandler.Listener<E> listener) throws IOException {
        String name = eventName.toUpperCase().replaceAll("\\s", "_");
        if (!this.eventHandlers.containsKey(name))
            if (Arrays.stream(IntentEvent.getEventArray()).map(GatewayEvent::getEventName).toList().contains(name.toUpperCase()))
                throw new IOException(
                        "\"" + eventName.toUpperCase() + "\" was not included in intents, to do so add the \"" +
                                Arrays.stream(IntentEvent.values()).filter(x -> Arrays.stream(x.getNames()).toList().contains(name)).toList().toArray(new IntentEvent[]{})[0].name() +
                                "\" to your intents next time you initialize this client class."
                );
            else throw new IOException("\"" + eventName.toUpperCase() + "\" was an invalid event name");
        EventHandler<E> eventHandler = (EventHandler<E>) this.eventHandlers.get(name);
        if (listener != null) if (i < 0) eventHandler.listeners.remove(eventHandler.listeners.size());
        else if (eventHandler.listeners.size() < i)
            throw new IOException("index " + i + " was out of bounds for size of " + eventHandler.listeners.size());
        else eventHandler.listeners.remove((int) i);
        else eventHandler.listeners.remove(listener);

        this.eventHandlers.put(name, eventHandler);
        return eventHandler;
    }

    public HashMap<String, EventHandler<?>> getHandlers() {
        return this.eventHandlers;
    }

    private static class GsonClientExclusionStrategy implements ExclusionStrategy{
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(Client.class);
        }
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz.equals(Client.class);
        }
    }

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

        public static GatewayEvent[] getEventArray() {
            ArrayList<GatewayEvent> output = new ArrayList<GatewayEvent>();
            Arrays.stream(IntentEvent.values()).forEach(x -> output.addAll(Arrays.stream(x.getClasses()).toList()));
            return output.toArray(new GatewayEvent[]{});
        }

        public String[] getNames() {
            return Arrays.stream(this.eventList).map(GatewayEvent::getEventName).toList().toArray(new String[]{});
        }
    }

    public static class EventHandler<E extends GatewayEvent> {
        ArrayList<Listener<E>> listeners = new ArrayList<Listener<E>>();
        public Client client;
        public Class<E> clazz;

        public EventHandler(Client client, Class<E> clazz) {
            this.clazz = clazz;
            this.client = client;
        }

        public EventHandler(Client client, Class<E> clazz, ArrayList<Listener<E>> listeners) {
            this.clazz = clazz;
            this.client = client;
            this.listeners = listeners;
        }

        public Object handler(HashMap payload) {
            E event = Client.GSON.fromJson(Client.GSON.toJson(payload.get("d"), LinkedTreeMap.class), this.clazz);

            final EventHandler.Input<E> input = new EventHandler.Input<E>(event, this.client);

            for (Listener<E> listener : listeners) {
                try {
                    if (listener.test(input)) {
                        listener.execute(input);
                        if (listener.getOnce()) this.listeners.remove(listener);
                    }
                } catch (Exception e) {
                    System.out.println(listener.getName() + " caused the error: " + e);
                    this.listeners.remove(listener);
                }
            }
            return null;
        }

        public static class Input<E> {
            public E data;
            public Client client;

            public Input(E data, Client client) {
                this.data = data;
                this.client = client;
            }
        }

        public interface Listener<E> {
            default Boolean test(Input<E> input) {
                return true;
            }

            void execute(Input<E> input);

            default Boolean getOnce() {
                return false;
            }

            String getName();

            class Builder<E> implements Listener<E> {
                private Function<Input<E>, Boolean> tester;
                private Function<Input<E>, Object> executor;
                private String name;
                private Boolean once;

                public Builder(Function<Input<E>, Boolean> tester, Function<Input<E>, Object> executor, String name) {
                    this.tester = tester;
                    this.executor = executor;
                    this.name = name;
                    this.once = false;
                }

                public Builder(Function<Input<E>, Boolean> tester, Function<Input<E>, Object> executor, String name, Boolean once) {
                    this.tester = tester;
                    this.executor = executor;
                    this.name = name;
                    this.once = once;
                }

                public Builder() {
                    this.tester = null;
                    this.executor = null;
                    this.name = null;
                    this.once = false;
                }

                public Builder tester(Function<Input<E>, Boolean> tester) {
                    this.tester = tester;
                    return this;
                }

                public Builder executor(Function<Input<E>, Object> executor) {
                    this.executor = executor;
                    return this;
                }

                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                public Builder once(Boolean once) {
                    this.once = once;
                    return this;
                }

                @Override
                public Boolean test(Input<E> input) {
                    return this.tester.apply(input);
                }

                @Override
                public void execute(Input<E> input) {
                    this.executor.apply(input);
                }

                @Override
                public Boolean getOnce() {
                    return this.once;
                }

                @Override
                public String getName() {
                    return this.name;
                }
            }
        }
    }
}
