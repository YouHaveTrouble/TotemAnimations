package me.youhavetrouble.totemanimations;

import org.bukkit.Color;
import org.jspecify.annotations.NonNull;

/**
 * Holds values that can be used in custom model data component.
 */
public sealed interface CmdValue {

    record FloatValue(float value) implements CmdValue {

        @Override
        public @NonNull String toString() {
            return "FloatValue{" +
                    "value=" + value +
                    '}';
        }
    }

    record ColorValue(Color value) implements CmdValue {

        @Override
        public @NonNull String toString() {
            return "ColorValue{" +
                    "value=" + value.toString() +
                    '}';
        }
    }

    record FlagValue(boolean value) implements CmdValue {

        @Override
        public @NonNull String toString() {
            return "FlagValue{" +
                    "value=" + value +
                    '}';
        }
    }

    record StringValue(String value) implements CmdValue {

        @Override
        public @NonNull String toString() {
            return "StringValue{" +
                    "value=" + value +
                    '}';
        }
    }

}
