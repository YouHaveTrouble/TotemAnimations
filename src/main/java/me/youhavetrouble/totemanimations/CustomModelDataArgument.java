package me.youhavetrouble.totemanimations;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NonNull;


import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CustomModelDataArgument implements CustomArgumentType<CmdValue, String> {


    public static CmdValue parseValue(String input) throws CommandSyntaxException {

        if (!input.contains(":")) {
            Message message = new LiteralMessage("Missing custom model data type: " + input);
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }

        String[] split = input.split(":");
        String key = split[0];
        String value = split[1];

        return switch (key) {
            case "string" -> new CmdValue.StringValue(value);
            case "flag" -> {
                if (value.equals("true")) {
                    yield new CmdValue.FlagValue(true);
                } else if (value.equals("false")) {
                    yield new CmdValue.FlagValue(false);
                } else {
                    Message message = new LiteralMessage("Error parsing custom model data flag argument: " + input);
                    throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
                }
            }
            case "float" -> {
                try {
                    float f = Float.parseFloat(split[1]);
                    yield new CmdValue.FloatValue(f);
                } catch (NullPointerException | NumberFormatException e) {
                    Message message = new LiteralMessage("Error parsing custom model data float argument: " + input);
                    throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
                }
            }
            default -> {
                Message message = new LiteralMessage("Error parsing custom model data argument type: " + input);
                throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
            }
        };

    }

    @Override
    public @NonNull CmdValue parse(@NonNull StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        String rawArg = reader.getString().substring(cursor);
        String[] args = rawArg.split(" ");
        String arg = args[0];
        reader.setCursor(cursor + arg.length());
        return parseValue(arg);
    }

    @Override
    public @NonNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        String input = builder.getInput().toLowerCase(Locale.ENGLISH);

        return builder.buildFuture();
    }
}
