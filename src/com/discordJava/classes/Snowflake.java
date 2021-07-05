package com.discordJava.classes;

import java.sql.Timestamp;

public class Snowflake {
    public long time;
    public byte internalWorkerID;
    public byte internalProcessID;
    public short processIncrement;

    Snowflake(Long snowflake) {
        this.time = snowflake >> 22;
        this.internalWorkerID = (byte) ((snowflake >> 17) & 0b11111);
        this.internalProcessID = (byte) ((snowflake >> 12) & 0b11111);
        this.processIncrement = (short) (snowflake & 0b111111111111);
    }

    private Timestamp getDate() {
        return new Timestamp(this.time + (31557600000L * 45));
    }
}
