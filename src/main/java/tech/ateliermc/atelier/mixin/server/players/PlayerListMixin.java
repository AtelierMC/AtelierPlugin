package tech.ateliermc.atelier.mixin.server.players;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.ateliermc.atelier.common.AtelierScoreboard;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At(
                    value = "RETURN"
            )
    )
    private void setupScoreboard(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        AtelierScoreboard.initSidebar(serverPlayer);
        AtelierScoreboard.initTablist(serverPlayer);
    }
}
