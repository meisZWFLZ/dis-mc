package com.discordJava.events;

public interface GatewayEvent {
    default String getEventName() {
        String[] x;
        String name = (x = this.getClass().getName().split("\\."))[x.length - 1];
        Integer i = -1;
        Integer j = 0;
        String s;
        for (Character c : name.toCharArray())
            if (++i != 0)
                if ((s = c.toString()).toUpperCase().equals(s))
                    name = name.substring(0, i + j) + "_" + name.substring(i + j++);
        return name.toUpperCase();
    }
}
