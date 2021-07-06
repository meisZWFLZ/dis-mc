package com.discordJava.classes;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;

public class Snowflake {
    public final Long time;
    public final Byte internalWorkerID;
    public final Byte internalProcessID;
    public final Short processIncrement;
    public final String str;
    public final Long num;

    Snowflake(String str) {
        this.str = str;
        long snowflake = 0L;
        double index = 0d;

        for (Character c : this.str.toCharArray())
            //may cause errors in very distant future
            if (index < 20)
                if (Character.isDigit(c))
                    snowflake = snowflake + ((long) c * ((long) Math.pow(10d, index++)));
                else index++;
            else break;

        this.num = snowflake;

        this.time = snowflake >> 22;
        this.internalWorkerID = ((Long) ((snowflake >> 17) & 0b11111)).byteValue();
        this.internalProcessID = ((Long) ((snowflake >> 12) & 0b11111)).byteValue();
        this.processIncrement = ((Long) (snowflake & 0b111111111111)).shortValue();
    }

    private Timestamp getDate() {
        return new Timestamp(this.time + (31557600000L * 45));
    }

    @Override
    public String toString() {
        return str;
    }

    public static class SnowflakeAdapter extends TypeAdapter<Snowflake> {

        @Override
        public void write(JsonWriter writer, Snowflake value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value.toString());
        }

        @Override
        public Snowflake read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            return new Snowflake(reader.nextString());
        }
    }
}
