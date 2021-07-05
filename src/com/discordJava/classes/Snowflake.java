package com.discordJava.classes;

import java.sql.Timestamp;

public class Snowflake {
    public Long time;
    public Byte internalWorkerID;
    public Byte internalProcessID;
    public Short processIncrement;

    Snowflake(Long snowflake) {
        this.time = snowflake >> 22;
        this.internalWorkerID = ((Long) ((snowflake >> 17) & 0b11111)).byteValue();
        this.internalProcessID = ((Long) ((snowflake >> 12) & 0b11111)).byteValue();
        this.processIncrement = ((Long) (snowflake & 0b111111111111)).shortValue();
    }

    private Timestamp getDate() {
        return new Timestamp(this.time + (31557600000L * 45));
    }
}
