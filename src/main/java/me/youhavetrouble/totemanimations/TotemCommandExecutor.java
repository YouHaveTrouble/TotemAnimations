package me.youhavetrouble.totemanimations;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TotemCommandExecutor implements TabExecutor {

    private final ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
    ItemMeta meta = totem.getItemMeta();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) return false;
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return false;
        try {
            int customModelData = Integer.parseInt(args[1]);
            playTotemAnimation(player, customModelData);
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
                if (sender instanceof Player senderPlayer) {
                    if (!senderPlayer.canSee(player)) return;
                    suggestions.add(player.getName());
                }
            });
        }

        return suggestions;
    }

    public void playTotemAnimation(Player player, int customModelData) {
        meta.setCustomModelData(customModelData);
        totem.setItemMeta(meta);
        ItemStack hand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(totem);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        player.getInventory().setItemInMainHand(hand);
    }

}
