package me.youhavetrouble.totemanimations;

import org.bukkit.Color;

/**
 * Holds values that can be used in custom model data component.
 */
public sealed interface CmdValue {

    record FloatValue(float value) implements CmdValue {}

    record ColorValue(Color value) implements CmdValue {}

    record FlagValue(boolean value) implements CmdValue {}

    record StringValue(String value) implements CmdValue {}

}
