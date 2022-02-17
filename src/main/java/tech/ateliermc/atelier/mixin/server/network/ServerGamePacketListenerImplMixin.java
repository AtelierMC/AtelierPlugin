package tech.ateliermc.atelier.mixin.server.network;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.ateliermc.atelier.bridge.server.ServerScoreboardBridge;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;
import tech.ateliermc.atelier.common.AtelierChat;

import java.util.UUID;
import java.util.function.Function;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Redirect(
            method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"
            )
    )
    private void handleChat(PlayerList playerList, Component rawComponent, Function function, ChatType chatType, UUID uuid) {
        AtelierChat.handleChat(playerList, player, rawComponent, chatType, uuid);
    }

    @Inject(method = "onDisconnect(Lnet/minecraft/network/chat/Component;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;disconnect()V"
            )
    )
    private void removeScoreboard(Component component, CallbackInfo ci) {
        ((PlayerBridge) this.player).setScoreboard(null);
        ((ServerScoreboardBridge) this.player.getScoreboard()).bridge$removePlayer(this.player, true);
    }
}
