package tech.ateliermc.atelier.bridge.server;

import net.minecraft.server.level.ServerPlayer;

public interface ServerScoreboardBridge {
    void bridge$addPlayer(ServerPlayer player, boolean sendPackets);

    void bridge$removePlayer(ServerPlayer player, boolean sendPackets);
}
