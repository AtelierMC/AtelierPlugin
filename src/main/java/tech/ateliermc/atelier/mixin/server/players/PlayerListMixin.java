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

}
