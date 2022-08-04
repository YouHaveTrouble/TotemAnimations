package me.youhavetrouble.totemanimations;

import org.bukkit.plugin.java.JavaPlugin;

public final class TotemAnimations extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("totemanimation").setExecutor(new TotemCommandExecutor());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
