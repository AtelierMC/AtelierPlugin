package tech.ateliermc.atelier.mixin.server;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.ateliermc.atelier.bridge.server.ServerScoreboardBridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(ServerScoreboard.class)
public class ServerScoreboardMixin extends Scoreboard implements ServerScoreboardBridge {
    private final List<ServerPlayer> impl$players = new ArrayList<>();

    @Override
    public void bridge$addPlayer(final ServerPlayer player, final boolean sendPackets) {
        this.impl$players.add(player);
        if (sendPackets) {
            for (final PlayerTeam team : this.getPlayerTeams()) {
                player.connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
            }

            for (final net.minecraft.world.scores.Objective objective : this.getObjectives()) {
                player.connection.send(new ClientboundSetObjectivePacket(objective, 0));
                for (int i = 0; i < 19; ++i) {
                    if (this.getDisplayObjective(i) == objective) {
                        player.connection.send(new ClientboundSetDisplayObjectivePacket(i, objective));
                    }
                }
                for (final Score score : this.getPlayerScores(objective)) {
                    final ClientboundSetScorePacket packetIn = new ClientboundSetScorePacket(
                            ServerScoreboard.Method.CHANGE,
                            score.getObjective().getName(),
                            score.getOwner(),
                            score.getScore());
                    player.connection.send(packetIn);
                }
            }
        }
    }

    @Override
    public void bridge$removePlayer(final ServerPlayer player, final boolean sendPackets) {
        this.impl$players.remove(player);
        if (sendPackets) {
            this.impl$removeScoreboard(player);
        }
    }

    private void impl$removeScoreboard(final ServerPlayer player) {
        this.impl$removeTeams(player);
        this.impl$removeObjectives(player);
    }

    private void impl$removeTeams(final ServerPlayer player) {
        for (final PlayerTeam team : this.getPlayerTeams()) {
            player.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(team));
        }
    }

    private void impl$removeObjectives(final ServerPlayer player) {
        for (final net.minecraft.world.scores.Objective objective : this.getObjectives()) {
            player.connection.send(new ClientboundSetObjectivePacket(objective, 1));
        }
    }

    @Redirect(method = "onScoreChanged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void onUpdateScoreValue(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "onScoreChanged", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", remap = false))
    private boolean onUpdateScoreValue(final Set<?> set, final Object object) {
        return true;
    }

    @Redirect(method = "onPlayerRemoved",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updatePlayersOnRemoval(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "onPlayerScoreRemoved",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updatePlayersOnRemovalOfObjective(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Inject(method = "onObjectiveAdded", at = @At("RETURN"))
    private void impl$UpdatePlayersScoreObjective(final net.minecraft.world.scores.Objective objective, final CallbackInfo ci) {
        this.sendToPlayers(new ClientboundSetObjectivePacket(objective, 0));
    }

    @Redirect(method = "addPlayerToTeam",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updatePlayersOnPlayerAdd(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "removePlayerFromTeam(Ljava/lang/String;Lnet/minecraft/world/scores/PlayerTeam;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updatePlayersOnPlayerRemoval(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "onObjectiveChanged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updatePlayersOnObjectiveDisplay(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "onObjectiveChanged",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", remap = false))
    private boolean impl$alwaysReturnTrueForObjectivesDisplayName(final Set<net.minecraft.world.scores.Objective> set, final Object object) {
        return true;
    }

    @Redirect(method = "onObjectiveRemoved",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", remap = false))
    private boolean impl$alwaysReturnTrueForObjectiveRemoval(final Set<net.minecraft.world.scores.Objective> set, final Object object) {
        return true;
    }

    @Redirect(method = "onTeamAdded",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updateAllPlayersOnTeamCreation(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "onTeamChanged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updateAllPlayersOnTeamInfo(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "onTeamRemoved",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void impl$updateAllPlayersOnTeamRemoval(final PlayerList manager, final Packet<?> packet) {
        this.sendToPlayers(packet);
    }

    @Redirect(method = "startTrackingObjective",
            at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 0, remap = false))
    private Iterator impl$useOurScoreboardForPlayers(final List list) {
        return this.impl$players.iterator();
    }

    @Redirect(method = "stopTrackingObjective",
            at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 0, remap = false))
    private Iterator impl$useOurScoreboardForPlayersOnRemoval(final List list) {
        return this.impl$players.iterator();
    }

    private void sendToPlayers(final Packet<?> packet) {
        for (final ServerPlayer player: this.impl$players) {
            player.connection.send(packet);
        }
    }
}
