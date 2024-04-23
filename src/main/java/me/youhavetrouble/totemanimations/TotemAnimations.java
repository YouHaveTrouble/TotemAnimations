package me.youhavetrouble.totemanimations;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class TotemAnimations extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginCommand command = getCommand("totemanimation");
        if (command == null) return;
        command.setExecutor(new TotemCommandExecutor());
    }

    /**
     * Shows totem animation with specified CustomModelData.
     * @param player Player to show animation to
     * @param customModelData Totem CustomModelData value
     */
    public static void playTotemAnimation(Player player, int customModelData) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = totem.getItemMeta();
        if (meta == null) return;
        meta.setCustomModelData(customModelData);
        totem.setItemMeta(meta);
        ItemStack hand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(totem);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        player.getInventory().setItemInMainHand(hand);
    }
}
