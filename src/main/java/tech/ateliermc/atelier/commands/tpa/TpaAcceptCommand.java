package tech.ateliermc.atelier.commands.tpa;

import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.Atelier;
import tech.ateliermc.atelier.AtelierPermissions;

public class TpaAcceptCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpaccept").requires(Permissions.require(AtelierPermissions.TPA))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                    if (Atelier.commandCooldown.containsKey(player.getGameProfile().getId())) {
                        long secondsLeft = (Atelier.commandCooldown.get(player.getGameProfile().getId()) - System.currentTimeMillis()) / 1000;
                        if (secondsLeft > 0) {
                            ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.RED + "You must wait " + secondsLeft + " seconds before using this command again."), false);
                            return 1;
                        } else Atelier.commandCooldown.put(player.getGameProfile().getId(), System.currentTimeMillis() + (Atelier.cooldown * 1000));
                    }

                    if (TpaCommand.tpaMap.containsKey(player)) {
                        ServerPlayer requester = TpaCommand.tpaMap.get(player);
                        requester.sendMessage(new TextComponent("Teleport Request accepted.").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                        player.sendMessage(new TextComponent("Teleport Request accepted.").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                        requester.teleportTo(player.getX(), player.getY(), player.getZ());
                        TpaCommand.tpaMap.remove(player);
                    } else ctx.getSource().sendFailure(new TextComponent("You have no teleport requests to accept"));

                    return 1;
                }));
    }
}
