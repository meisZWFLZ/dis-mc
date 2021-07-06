package com.discordJava.classes;

import com.discordJava.annotations.GsonIgnore;
import com.discordJava.events.*;
import com.discordJava.gateway.DiscordGateway;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * <code> Class for all your bot making needs probably! </code>
 */
@GsonIgnore
public class Client {
    private String token;
    private HashMap<String, EventHandler<?>> eventHandlers;
    public User user;
    public Guild[] guilds;
    public Application application;
    private String session_id;
    private String[] intents = new String[]{
            "GUILD_MESSAGES", "DIRECT_MESSAGES"
    };
    private DiscordGateway gateway;
    public Byte version = 8;
    public static final Gson GSON = new GsonBuilder().setExclusionStrategies(new GsonIgnore.Strategy()).create();
    public Client client;

    /**
     * <code>Constructor for client</code>
     *
     * @param intents an array of case-insensitive intent strings.
     * @param token   Your bot token, that you get from
     * @see IntentEvent
     * @see Client#tokenFilter
     */

    public Client(String token, String[] intents) throws IOException {
        this.intents = intents;
        this.token = this.tokenFilter(token);
        this.client = this;
        this.eventHandlerSetUp();
    }

    /**
     * <code>Constructor for client</code>
     *
     * @param token Your bot token, that you get from
     * @see Client#tokenFilter(String)
     */

    public Client(String token) throws IOException {
        this.token = this.tokenFilter(token);
        this.client = this;
        this.eventHandlerSetUp();
    }

    /**
     * <code> Initiate the Discord Gateway! (And other stuff!!!)
     *
     * <p>
     * Opens the gateway portal, initializing the EventHandler and beginning your bot's journey through the Discord Gateway Hyperspace.
     * </code>
     */

    public void login() throws Exception {
        if (this.token == null) throw new Error("token must be defined");
        this.gateway = new DiscordGateway(this.token, this.intents, this.version);
        this.gateway.on("READY", this::readyListener);
        this.gateway.on(0, this::dispatchListener);
    }

    /**
     * <code> Listens to incoming dispatches from discord gateway and directs them to the EventHandlers! </code>
     *
     * @param payload A json string turned HashMap by the all powerful GSON
     */

    private Object dispatchListener(HashMap<?, ?> payload) {
        EventHandler<?> handler = eventHandlers.get((String) payload.get("t"));
        if (handler == null) return null;
        handler.handler(payload);

        return null;
    }

    /**
     * <code> Readies the client and stores some data for later use. </code>
     *
     * @param payload A json string turned HashMap by the all powerful GSON
     */

    private Object readyListener(HashMap payload) {
        String payloadStr = GSON.toJson(payload, HashMap.class);
        Ready ready = GSON.fromJson(payloadStr, Ready.class);

        this.user = ready.user;
        this.guilds = ready.guilds;
        this.application = ready.application;
        this.session_id = ready.session_id;
        this.client = this;

        return null;
    }

    /**
     * <code> Handles the EventHandler setup by handling the EventHandlers to be handled</code>
     */

    private void eventHandlerSetUp() {
        String[] intents;
        this.eventHandlers = new HashMap<String, EventHandler<?>>();
        if (!Arrays.stream(this.intents).toList().contains("PASSTHROUGH")) {
            ArrayList<String> intentList = new ArrayList<String>(Arrays.asList(this.intents));
            intentList.add("PASSTHROUGH");
            intents = intentList.toArray(new String[]{});
        } else intents = this.intents;
        Arrays.stream(IntentEvent.values()).filter(x -> Arrays.stream(intents).toList().contains(x.name())).forEach(intent -> {
            Arrays.stream(intent.getClasses()).map(GatewayEvent::getEventName).forEach(name -> {
                this.eventHandlers.put(name,
                        switch (name) {
                            case "STAGE_INSTANCE_DELETE" -> new EventHandler<>(this, StageInstanceDelete.class);
                            case "RESUMED" -> new EventHandler<>(this, Resumed.class);
                            case "MESSAGE_DELETE" -> new EventHandler<>(this, MessageDelete.class);
                            case "INTEGRATION_DELETE" -> new EventHandler<>(this, IntegrationDelete.class);
                            case "APPLICATION_COMMAND_CREATE" -> new EventHandler<>(this, ApplicationCommandCreate.class);
                            case "GUILD_BAN_ADD" -> new EventHandler<>(this, GuildBanAdd.class);
                            case "THREAD_DELETE" -> new EventHandler<>(this, ThreadDelete.class);
                            case "CHANNEL_CREATE" -> new EventHandler<>(this, ChannelCreate.class);
                            case "INTEGRATION_UPDATE" -> new EventHandler<>(this, IntegrationUpdate.class);
                            case "STAGE_INSTANCE_UPDATE" -> new EventHandler<>(this, StageInstanceUpdate.class);
                            case "READY" -> new EventHandler<>(this, Ready.class);
                            case "INVITE_CREATE" -> new EventHandler<>(this, InviteCreate.class);
                            case "CHANNEL_PINS_UPDATE" -> new EventHandler<>(this, ChannelPinsUpdate.class);
                            case "THREAD_MEMBERS_UPDATE" -> new EventHandler<>(this, ThreadMembersUpdate.class);
                            case "THREAD_UPDATE" -> new EventHandler<>(this, ThreadUpdate.class);
                            case "PRESENCE_UPDATE" -> new EventHandler<>(this, PresenceUpdate.class);
                            case "MESSAGE_REACTION_REMOVE_ALL" -> new EventHandler<>(this, MessageReactionRemoveAll.class);
                            case "MESSAGE_CREATE" -> new EventHandler<>(this, MessageCreate.class);
                            case "GUILD_ROLE_DELETE" -> new EventHandler<>(this, GuildRoleDelete.class);
                            case "VOICE_STATE_UPDATE" -> new EventHandler<>(this, VoiceStateUpdate.class);
                            case "INTERACTION_CREATE" -> new EventHandler<>(this, InteractionCreate.class);
                            case "MESSAGE_REACTION_REMOVE_EMOJI" -> new EventHandler<>(this, MessageReactionRemoveEmoji.class);
                            case "GUILD_MEMBER_REMOVE" -> new EventHandler<>(this, GuildMemberRemove.class);
                            case "STAGE_INSTANCE_CREATE" -> new EventHandler<>(this, StageInstanceCreate.class);
                            case "GUILD_DELETE" -> new EventHandler<>(this, GuildDelete.class);
                            case "MESSAGE_UPDATE" -> new EventHandler<>(this, MessageUpdate.class);
                            case "GUILD_MEMBER_UPDATE" -> new EventHandler<>(this, GuildMemberUpdate.class);
                            case "CHANNEL_DELETE" -> new EventHandler<>(this, ChannelDelete.class);
                            case "APPLICATION_COMMAND_UPDATE" -> new EventHandler<>(this, ApplicationCommandUpdate.class);
                            case "GUILD_CREATE" -> new EventHandler<>(this, GuildCreate.class);
                            case "GUILD_MEMBER_ADD" -> new EventHandler<>(this, GuildMemberAdd.class);
                            case "GUILD_INTEGRATIONS_UPDATE" -> new EventHandler<>(this, GuildIntegrationsUpdate.class);
                            case "THREAD_LIST_SYNC" -> new EventHandler<>(this, ThreadListSync.class);
                            case "THREAD_CREATE" -> new EventHandler<>(this, ThreadCreate.class);
                            case "GUILD_ROLE_UPDATE" -> new EventHandler<>(this, GuildRoleUpdate.class);
                            case "THREAD_MEMBER_UPDATE" -> new EventHandler<>(this, ThreadMemberUpdate.class);
                            case "MESSAGE_DELETE_BULK" -> new EventHandler<>(this, MessageDeleteBulk.class);
                            case "INTEGRATION_CREATE" -> new EventHandler<>(this, IntegrationCreate.class);
                            case "WEBHOOKS_UPDATE" -> new EventHandler<>(this, WebhooksUpdate.class);
                            case "USER_UPDATE" -> new EventHandler<>(this, UserUpdate.class);
                            case "MESSAGE_REACTION_ADD" -> new EventHandler<>(this, MessageReactionAdd.class);
                            case "INVITE_DELETE" -> new EventHandler<>(this, InviteDelete.class);
                            case "GUILD_EMOJIS_UPDATE" -> new EventHandler<>(this, GuildEmojisUpdate.class);
                            case "TYPING_START" -> new EventHandler<>(this, TypingStart.class);
                            case "VOICE_SERVER_UPDATE" -> new EventHandler<>(this, VoiceServerUpdate.class);
                            case "CHANNEL_UPDATE" -> new EventHandler<>(this, ChannelUpdate.class);
                            case "GUILD_BAN_REMOVE" -> new EventHandler<>(this, GuildBanRemove.class);
                            case "GUILD_UPDATE" -> new EventHandler<>(this, GuildUpdate.class);
                            case "GUILD_ROLE_CREATE" -> new EventHandler<>(this, GuildRoleCreate.class);
                            case "MESSAGE_REACTION_REMOVE" -> new EventHandler<>(this, MessageReactionRemove.class);
                            default -> null;
                        });
            });
        });
    }


    /**
     * <code> Add a Listener to EventHandler for eventName to the end of the EventHandler's Listener array</code>
     *
     * @param eventName The name of the event you would like the listener to listen for.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param listener  The Listener you would like to add to the EventHandler
     * @param <E>       The GatewayEvent of the Listener and EventHandler
     * @return Returns the resulting EventHandler after having added listener.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    public <E extends GatewayEvent> EventHandler<E> on(String eventName, EventHandler.Listener<E> listener) throws IOException {
        return this.addListener(eventName, -1, listener);
    }

    /**
     * <code> Add a Listener to EventHandler for eventName at index in the EventHandler's Listener array</code>
     *
     * @param eventName The name of the event you would like the listener to listen for.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param index     The index where to place listener in the EventHandler's array.
     *                  Notes: A negative integer will add the Listener at end of array.
     *                  Examples: -10, 6
     * @param listener  The Listener you would like to add to the EventHandler
     * @param <E>       The GatewayEvent of the Listener and EventHandler
     * @return Returns the resulting EventHandler after having added listener.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    public <E extends GatewayEvent> EventHandler<E> on(String eventName, Integer index, EventHandler.Listener<E> listener) throws IOException {
        return this.addListener(eventName, index, listener);
    }

    /**
     * <code> Add a Listener to EventHandler for eventName to the beginning of the EventHandler's Listener array</code>
     *
     * @param eventName The name of the event you would like the listener to listen for.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param listener  The Listener you would like to add to the EventHandler
     * @param <E>       The GatewayEvent of the Listener and EventHandler
     * @return Returns the resulting EventHandler after having added listener.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    public <E extends GatewayEvent> EventHandler<E> prependListener(String eventName, EventHandler.Listener<E> listener) throws IOException {
        return this.addListener(eventName, 0, listener);
    }

    /**
     * <code> internal listener adder</code>
     *
     * @param eventName The name of the event you would like the listener to listen for.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param i         The index where to place listener in the EventHandler's array.
     *                  Notes: A negative integer will add the Listener at end of array.
     *                  Examples: -10, 6
     * @param listener  The Listener you would like to add to the EventHandler
     * @param <E>       The GatewayEvent of the Listener and EventHandler
     * @return Returns the resulting EventHandler after having added listener.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    private <E extends GatewayEvent> EventHandler<E> addListener(String eventName, Integer i, EventHandler.Listener<E> listener) throws IOException {
        String name = eventName.toUpperCase().replaceAll("\\s", "_");
        if (!this.eventHandlers.containsKey(name))
            if (Arrays.stream(IntentEvent.EVENT_ARRAY).map(GatewayEvent::getEventName).toList().contains(name.toUpperCase()))
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

    /**
     * <code> Remove the first occurrence of listener from the array of the EventHandler specified by eventName</code>
     *
     * @param eventName The name of the event you would like to remove the listener from.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param listener  The Listener you would like to remove from the EventHandler
     * @param <E>       The GatewayEvent of the Listener and EventHandler
     * @return Returns the resulting EventHandler after having removed the first occurrence of listener.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    public <E extends GatewayEvent> EventHandler<E> off(String eventName, EventHandler.Listener<E> listener) throws IOException {
        return this.removeListener(eventName, null, listener);
    }

    /**
     * <code> Remove the Listener at index from the array of the EventHandler specified by eventName</code>
     *
     * @param eventName The name of the event you would like to remove the Listener from.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param index     The index of the Listener you would like to remove from the EventHandler
     *                  Notes: A negative integer will the last Listener in the EventHandler's array
     *                  Examples: -1, 24
     * @param <E>       The GatewayEvent of the Listener and EventHandler
     * @return Returns the resulting EventHandler after having removed the Listener at index.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    public <E extends GatewayEvent> EventHandler<E> off(String eventName, Integer index) throws IOException {
        return this.removeListener(eventName, index, null);
    }

    /**
     * <code> Remove the last Listener in the array of the EventHandler specified by eventName</code>
     *
     * @param eventName The name of the event you would like to remove the Listener from.
     *                  Notes: This string is not case sensitive.
     *                  Example: "MESSAGE_CREATE"
     * @param <E>       The GatewayEvent of the EventHandler
     * @return Returns the resulting EventHandler after having removed the Listener at index.
     * @throws IOException thrown upon invalid event name or the event was not included in the Client's intents
     */

    public <E extends GatewayEvent> EventHandler<E> off(String eventName) throws IOException {
        return this.removeListener(eventName, -1, null);
    }

    /**
     * <code> Event Listener Remover</code>
     */

    private <E extends GatewayEvent> EventHandler<E> removeListener(String eventName, Integer i, EventHandler.Listener<E> listener) throws IOException {
        String name = eventName.toUpperCase().replaceAll("\\s", "_");
        if (!this.eventHandlers.containsKey(name))
            if (Arrays.stream(IntentEvent.EVENT_ARRAY).map(GatewayEvent::getEventName).toList().contains(name.toUpperCase()))
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

    /**
     * <code> Simple Discord bot token filter using this regex: [A-Za-z\d]{24}\.[\w-]{6}\.[\w-]{27} </code>
     *
     * @param token The token string to be checked
     * @return Always token
     * @throws InvalidTokenException Thrown upon an invalid token!
     */

    public static String tokenFilter(String token) throws InvalidTokenException {
        if (!Pattern.matches("[A-Za-z\\d]{24}\\.[\\w-]{6}\\.[\\w-]{27}", token))
            throw new InvalidTokenException(token);
        else return token;
    }

    /**
     * <code>Steal the token from the Client class vault!</code>
     */

    public String token() {
        return this.token;
    }

    /**
     * <code>Sneak back in the Client vault of code and replace the token with a new one and quickly snag the already present token!!</code>
     *
     * @param token the token to place in the vault
     * @throws InvalidTokenException what happens when u dumb and don't replace the old token with a valid one!
     * @see #token
     * @see #tokenFilter(String)
     * @see Client.InvalidTokenException
     */

    public String setToken(String token) throws InvalidTokenException {
        String tempToken = this.token;
        this.token = tokenFilter(token);
        return tempToken;
    }

    /**
     * <code>Get the Client's EventHandlers<code/>
     *
     * @return A Hashmap of the Client's EventHandlers
     * @see EventHandler
     */

    public HashMap<String, EventHandler<?>> getHandlers() {
        return this.eventHandlers;
    }

    /**
     * <code> My own exception for being cool and for when I'm dumb and post my token on the internet! </code>
     *
     * @see #tokenFilter(String)
     */

    public static class InvalidTokenException extends IOException {
        public final String token;

        /**
         * <code>
         * Customize your very own InvalidTokenException by adding your own token!
         * <p> Formula: "The inputted token, \"" + [token] + "\", was invalid."
         * </code>
         */

        InvalidTokenException(String token) {
            super("The inputted token, \"" + token + "\", was invalid.");
            this.token = token;
        }

        /**
         * <code>
         * Be unoriginal and lazy with this!
         * <p> Result Thing: "The inputted token was invalid."
         * </code>
         */

        InvalidTokenException() {
            super("The inputted token was invalid.");
            this.token = null;
        }
    }

    /**
     * <code>
     * Pogger Handler of a single event (like "MESSAGE_CREATE" or "CHANNEL_DELETE")
     * <p> The {@link #handler(HashMap)} runs through the {@link #listeners}
     * <p> To edit the handler listeners, simply edit {@link #listeners}
     * </code>
     *
     * @param <E> subclass of {@link GatewayEvent} and the event the handler handles,
     *            <p> this is handled by the {@link Client#eventHandlerSetUp()}
     * @author meisZWFLZ
     */

    public static class EventHandler<E extends GatewayEvent> {
        /**
         * <code>An amazing array of listeners,
         * <p>the {@link #handler(HashMap)} cycles through this list from least to greatest
         * </code>
         */
        ArrayList<Listener<E>> listeners = new ArrayList<Listener<E>>();

        /**
         * <code>The client class that is fed to {@link Listener}</code>
         */
        public Client client;

        /**
         * <code>Quite clazzy, ngl</code>
         */
        public Class<E> clazz;

        /**
         * <code>Intialize yourself an empty EventHandler</code>
         *
         * @param client {@link #client} fed to listeners
         * @param clazz  {@link #clazz} super important for deserialization of gateway payloads
         */

        public EventHandler(Client client, Class<E> clazz) {
            this.clazz = clazz;
            this.client = client;
        }


        /**
         * <code>Intialize yourself a pretty neat EventHandler with some listeners to start with</code>
         *
         * @param client    {@link #client} fed to listeners
         * @param clazz     {@link #clazz} super important for deserialization of gateway payloads
         * @param listeners {@link #listeners} give yourself a boost in listeners cause you can
         */

        public EventHandler(Client client, Class<E> clazz, ArrayList<Listener<E>> listeners) {
            this.clazz = clazz;
            this.client = client;
            this.listeners = listeners;
        }

        /**
         * <code>
         * The HANDLER of the EventHandler!!
         * <p> This guy takes in {@link HashMap} payloads from {@link DiscordGateway#dispatchListener(HashMap)}
         * and then turns them into {@link E}/{@link #clazz}. Now he runs down through his list of {@link #listeners}
         * and run any that pass the tester. If the {@link Listener} passed the test and {@link Listener#getOnce()} was
         * true it would be removed from the list. The other way to be removed from the list is by causing an error,
         * which prompts the handler to remove the error causing listener to prevent further errors.
         * </code>
         *
         * @param payload {@link HashMap} received from the Discord Gateway dispatcher unit which is serialized
         *                and then deserialized into {@link E}/{@link #clazz}
         * @return returns null as it is required to return an object for Discord Gateway event handling system
         */

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

        /**
         * <code> Input fed to {@link Listener#test} and {@link Listener#execute(Input)} by {@link #handler(HashMap)} </code>
         *
         * @param <E> represents {@link #clazz}
         */

        public static class Input<E> {
            public E data;
            public Client client;

            /**
             * <code> Simple constructor</code>
             *
             * @param data
             * @param client
             */

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

                public Builder<E> tester(Function<Input<E>, Boolean> tester) {
                    this.tester = tester;
                    return this;
                }

                public Builder<E> executor(Function<Input<E>, Object> executor) {
                    this.executor = executor;
                    return this;
                }

                public Builder<E> name(String name) {
                    this.name = name;
                    return this;
                }

                public Builder<E> once(Boolean once) {
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
