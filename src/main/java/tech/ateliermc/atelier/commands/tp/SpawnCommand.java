package tech.ateliermc.atelier.commands.tp;

import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelData;
import tech.ateliermc.atelier.Atelier;
import tech.ateliermc.atelier.AtelierPermissions;

public class SpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawn").requires(Permissions.require(AtelierPermissions.SPAWN))
                .executes(ctx -> {
                    ServerPlayer player;

                    try {
                        player = ctx.getSource().getPlayerOrException();
                    } catch (Exception e) {
                        ctx.getSource().sendFailure(new TextComponent("You must be a player to run this command"));
                        return 0;
                    }

                    LevelData data = player.getLevel().getLevelData();

                    if (Atelier.commandCooldown.containsKey(player.getGameProfile().getId())) {
                        long secondsLeft = (Atelier.commandCooldown.get(player.getGameProfile().getId()) - System.currentTimeMillis()) / 1000;
                        if (secondsLeft > 0) {
                            ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.RED + "You must wait " + secondsLeft + " seconds before using this command again."), false);
                            return 1;
                        }
                    }

                    Atelier.commandCooldown.put(player.getGameProfile().getId(), System.currentTimeMillis() + (Atelier.cooldown * 1000));

                    player.teleportTo(player.getLevel(), data.getXSpawn(), data.getYSpawn(), data.getZSpawn(), data.getSpawnAngle(), data.getSpawnAngle());
                    player.sendMessage(new TextComponent("Teleported to spawn.").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);

                    return 1;
                }));
    }
}
