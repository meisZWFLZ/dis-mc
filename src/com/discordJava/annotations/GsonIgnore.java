package com.discordJava.annotations;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.Arrays;

/**
 * <code>
 * Annotate gson's favorite classes and fields to create errors on to stop errors from happeing!
 * <p> Make sure always to use {@link com.discordJava.classes.Client#GSON} whenever working with gson
 * </code>
 *
 * @see GsonIgnore.Strategy
 * @see ExclusionStrategy
 * @see com.discordJava.classes.Client#GSON
 */

public @interface GsonIgnore {
    /**
     * <code>prevent annoying gson from reading what its not supposed to by annotating your class with {@link GsonIgnore} (dum gson)</code>
     *
     * @see GsonIgnore
     * @see ExclusionStrategy
     * @see com.discordJava.classes.Client#GSON
     */
    class Strategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotations().contains(GsonIgnore.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Arrays.asList(clazz.getAnnotations()).contains(GsonIgnore.class);
        }
    }
}


