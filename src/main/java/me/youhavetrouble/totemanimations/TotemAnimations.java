package me.youhavetrouble.totemanimations;

import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TotemAnimations extends JavaPlugin {

    @Override
    public void onEnable() {
        LifecycleEventManager<@NotNull Plugin> manager = getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            CommandNode<CommandSourceStack> playersNode = TotemCommands.modelArgument.build();
            CommandNode<CommandSourceStack> modelNode = playersNode.getChildren().stream()
                    .filter(node -> "model".equals(node.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("model node missing"));

            CommandNode<CommandSourceStack> customModelDataNode = TotemCommands.customModelDataArgument
                    .redirect(modelNode)
                    .build();

            modelNode.addChild(customModelDataNode);

            commands.register(
                    Commands.literal("totemanimation")
                            .requires(css -> css.getSender().hasPermission("totemanimations.command"))
                            .then(playersNode)
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
