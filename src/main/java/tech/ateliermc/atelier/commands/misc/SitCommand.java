package tech.ateliermc.atelier.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tech.ateliermc.atelier.AtelierPermissions;
import tech.ateliermc.atelier.common.AtelierSit;

public class SitCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sit").requires(Permissions.require(AtelierPermissions.SIT))
                .executes(ctx -> {
                    final CommandSourceStack source = ctx.getSource();
                    ServerPlayer player;

                    try {
                        player = source.getPlayerOrException();
                    } catch (Exception e) {
                        source.sendFailure(new TextComponent("You must be a player to run this command"));
                        return 0;
                    }

                    BlockState blockState = player.getLevel().getBlockState(new BlockPos(player.getX(), player.getY() - 1, player.getZ()));
                    if (player.isPassenger() || player.isFallFlying() || player.isSleeping() || player.isSwimming() || player.isSpectator() || blockState.isAir() || blockState.getMaterial().isLiquid())
                        return 0;
                    Entity entity = AtelierSit.createChair(player.getLevel(), player.getOnPos(), new Vec3(0, -0.727, 0), player.position(), false);
                    player.startRiding(entity, true);
                    return 1;
                }));
    }
}
