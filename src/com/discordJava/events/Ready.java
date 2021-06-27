package com.discordJava.events;

import com.discordJava.classes.Application;
import com.discordJava.classes.Guild;
import com.discordJava.classes.User;

public class Ready implements GatewayEvent {
    public Integer v;
    public User user;
    public Guild[] guilds;
    public String session_id;
    public Integer[] shard;
    public Application application;
}
