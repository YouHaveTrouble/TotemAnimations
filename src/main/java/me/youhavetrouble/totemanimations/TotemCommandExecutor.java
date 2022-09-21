package me.youhavetrouble.totemanimations;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TotemCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) return false;
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return false;
        try {
            int customModelData = Integer.parseInt(args[1]);
            TotemAnimations.playTotemAnimation(player, customModelData);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (sender instanceof Player) {
                    Player senderPlayer = (Player) sender;
                    if (!senderPlayer.canSee(player)) return;
                    suggestions.add(player.getName());
                }
            });
        }
        return suggestions;
    }
}
