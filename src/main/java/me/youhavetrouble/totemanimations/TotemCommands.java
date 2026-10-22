package me.youhavetrouble.totemanimations;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TotemCommands {

    public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> modelArgument =
            Commands.argument("players", ArgumentTypes.players())
                    .requires(css -> css.getSender().hasPermission("totemanimations.command"))
                            .then(
                                    Commands.argument("model", ArgumentTypes.key())
                                            .requires(css -> css.getSender().hasPermission("totemanimations.command"))
                                            .executes(context -> {
                                                PlayerSelectorArgumentResolver targetResolver = context.getArgument("players", PlayerSelectorArgumentResolver.class);
                                                List<Player> players = targetResolver.resolve(context.getSource());
                                                Key key = context.getArgument("model", Key.class);
                                                for (Player player : players) {
                                                    TotemAnimations.playTotemAnimation(player, key);
                                                }
                                                sendCommandFeedback(context.getSource().getSender(), players);
                                                return Command.SINGLE_SUCCESS;
                                            })
                            );

    public static ArgumentBuilder<CommandSourceStack, RequiredArgumentBuilder<CommandSourceStack, CmdValue>> customModelDataArgument = Commands.argument("custom_model_data", new CustomModelDataArgument())
            .requires(css -> css.getSender().hasPermission("totemanimations.command"))
            .executes(context -> {
                PlayerSelectorArgumentResolver targetResolver = context.getArgument("players", PlayerSelectorArgumentResolver.class);
                List<Player> players = targetResolver.resolve(context.getSource());
                Key key = context.getArgument("model", Key.class);
                CmdValue customModelData = context.getArgument("custom_model_data", CmdValue.class);
                List<CmdValue> tags = new ArrayList<>();
                for (ParsedCommandNode<?> node : context.getNodes()) {
                    if (node.getNode().getName().equals("custom_model_data")) {
                        StringRange r = node.getRange();
                        tags.add(CustomModelDataArgument.parseValue(context.getInput().substring(r.getStart(), r.getEnd())));
                    }
                }
                tags.forEach(System.out::println);
                for (Player player : players) {
                    TotemAnimations.playTotemAnimation(player, key, tags.toArray(new CmdValue[0]));
                }
                sendCommandFeedback(context.getSource().getSender(), players);
                return Command.SINGLE_SUCCESS;
            });

    private static void sendCommandFeedback(CommandSender sender, List<Player> players) {
        String feedback = "Displayed totem animation to " + players.size() + " players";
        if (players.isEmpty()) {
            feedback = "No players matching selector found.";
        } else if (players.size() == 1) {
            feedback = "Displayed totem animation to " + players.getFirst().getName();
        }
        sender.sendMessage(Component.text(feedback, NamedTextColor.GRAY));
    }

}
