package tech.ateliermc.atelier.mixin.server.players;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.ateliermc.atelier.bridge.server.ServerScoreboardBridge;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;
import tech.ateliermc.atelier.common.AtelierScoreboard;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At(
                    value = "RETURN"
            )
    )
    private void setupScoreboard(Connection connection, ServerPlayer player, CallbackInfo ci) {
        if (((ServerPlayerBridge) player).enableScoreboard()) {
            ((ServerScoreboardBridge) player.getScoreboard()).bridge$removePlayer(player, true);
            ((PlayerBridge) player).setScoreboard(AtelierScoreboard.makeScoreboard(player));
            ((ServerScoreboardBridge) player.getScoreboard()).bridge$addPlayer(player, true);
        }
    }
}
