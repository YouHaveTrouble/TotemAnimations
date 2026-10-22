package me.youhavetrouble.totemanimations;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TotemAnimations extends JavaPlugin {

    private static final String totemUseSound = "item.totem.use";

    @Override
    public void onEnable() {
        LifecycleEventManager<@NotNull Plugin> manager = getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(Commands.literal("totemanimation")
                    .requires(css -> css.getSender().hasPermission("totemanimations.command"))
                    .then(Commands.argument("players", ArgumentTypes.players())
                            .requires(css -> css.getSender().hasPermission("totemanimations.command"))
                            .then(Commands.argument("model", ArgumentTypes.key())
                                    .requires(css -> css.getSender().hasPermission("totemanimations.command"))
                                    .executes(context -> {
                                        PlayerSelectorArgumentResolver targetResolver = context.getArgument("players", PlayerSelectorArgumentResolver.class);
                                        List<Player> players = targetResolver.resolve(context.getSource());
                                        Key key = context.getArgument("model", Key.class);
                                        for (Player player : players) {
                                            TotemAnimations.playTotemAnimation(player, key);
                                        }
                                        String feedback = "Displayed totem animation to " + players.size() + " players";
                                        if (players.isEmpty()) {
                                            feedback = "No players matching selector found.";
                                        } else if (players.size() == 1) {
                                            feedback = "Displayed totem animation to " + players.getFirst().getName();
                                        }
                                        context.getSource().getSender().sendMessage(Component.text(feedback, NamedTextColor.GRAY));
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                    )
                    .build()
            );
        });
        getServer().getPluginManager().addPermission(
                new Permission(
                        "totemanimations.command",
                        "Allows usage of /totemanimation command",
                        PermissionDefault.OP
                )
        );
    }

    /**
     * Shows totem animation with specified CustomModelData.
     *
     * @param player          Player to show animation to
     * @param customModelData legacy custom model data value
     * @deprecated use {@link #playTotemAnimation(Player, Key, CmdValue...)} instead.
     */
    @Deprecated
    public static void playTotemAnimation(Player player, int customModelData) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        CustomModelData cmd = CustomModelData.customModelData().addFloat(customModelData).build();
        totem.setData(DataComponentTypes.CUSTOM_MODEL_DATA, cmd);
        ItemStack hand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(totem);
        player.playEffect(EntityEffect.PROTECTED_FROM_DEATH);
        player.stopSound(totemUseSound, SoundCategory.PLAYERS);
        player.getInventory().setItemInMainHand(hand);
    }

    /**
     * Shows totem animation with specified model and custom model data.
     *
     * @param player    Player to show animation to
     * @param modelKey  Key of the model to display
     * @param cmdValues List of values that will be passed into custom model data
     */
    public static void playTotemAnimation(
            Player player,
            Key modelKey,
            CmdValue... cmdValues
    ) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        totem.setData(DataComponentTypes.ITEM_MODEL, modelKey);
        if (cmdValues.length != 0) {
            CustomModelData.Builder cmd = CustomModelData.customModelData();
            for (CmdValue value : cmdValues) {
                switch (value) {
                    case CmdValue.FloatValue v -> cmd.addFloat(v.value());
                    case CmdValue.ColorValue v -> cmd.addColor(v.value());
                    case CmdValue.FlagValue v -> cmd.addFlag(v.value());
                    case CmdValue.StringValue v -> cmd.addString(v.value());
                }
            }
            totem.setData(DataComponentTypes.CUSTOM_MODEL_DATA, cmd.build());
        }
        ItemStack hand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(totem);
        player.playEffect(EntityEffect.PROTECTED_FROM_DEATH);
        player.stopSound(totemUseSound, SoundCategory.PLAYERS);
        player.getInventory().setItemInMainHand(hand);
    }

    public enum CustomModelDataType {
        FLOAT(Float.class),
        COLOR(Color.class),
        FLAG(Boolean.class),
        STRING(String.class),
        ;

        public final Class<?> type;

        CustomModelDataType(Class<?> type) {
            this.type = type;
        }
    }

}
