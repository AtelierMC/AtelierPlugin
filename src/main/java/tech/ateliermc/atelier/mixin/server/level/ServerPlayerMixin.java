package tech.ateliermc.atelier.mixin.server.level;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.mixin.world.entity.player.PlayerMixin;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin extends PlayerMixin implements ServerPlayerBridge {

}
