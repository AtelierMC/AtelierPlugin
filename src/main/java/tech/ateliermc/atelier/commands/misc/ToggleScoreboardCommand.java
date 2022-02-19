package tech.ateliermc.atelier.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.bridge.server.ServerScoreboardBridge;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;
import tech.ateliermc.atelier.common.AtelierScoreboard;

public class ToggleScoreboardCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("togglesb")
                .executes(ctx -> {
                    ServerPlayer player;

                    try {
                        player = ctx.getSource().getPlayerOrException();
                    } catch (Exception e) {
                        ctx.getSource().sendFailure(new TextComponent("You must be a player to run this command"));
                        return 0;
                    }

                    if (!((ServerPlayerBridge) player).enableScoreboard()) {
                        ((ServerPlayerBridge) player).setEnableScoreboard(true);
                        ((ServerScoreboardBridge) player.getScoreboard()).bridge$removePlayer(player, true);
                        ((PlayerBridge) player).setScoreboard(AtelierScoreboard.makeScoreboard(player));
                        ((ServerScoreboardBridge) player.getScoreboard()).bridge$addPlayer(player, true);

                        ctx.getSource().sendSuccess(new TextComponent("Scoreboard enabled").withStyle(ChatFormatting.GREEN), false);
                    } else {
                        ((ServerPlayerBridge) player).setEnableScoreboard(false);
                        ((ServerScoreboardBridge) player.getScoreboard()).bridge$removePlayer(player, true);
                        ((PlayerBridge) player).setScoreboard(player.getLevel().getScoreboard());

                        ctx.getSource().sendSuccess(new TextComponent("Scoreboard disabled").withStyle(ChatFormatting.RED), false);
                    }

                    return 1;
                }));
    }
}
