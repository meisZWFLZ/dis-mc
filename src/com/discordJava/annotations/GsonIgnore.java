package com.discordJava.annotations;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
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
            return f.getAnnotation(GsonIgnore.class) != null || shouldSkipClass(f.getDeclaredClass());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz.getAnnotation(GsonIgnore.class) != null/* || clazz.equals(Client.class)*/;
        }
    }
}


