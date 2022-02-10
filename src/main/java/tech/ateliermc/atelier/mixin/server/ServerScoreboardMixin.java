package tech.ateliermc.atelier.mixin.server;

import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerScoreboard.class)
public class ServerScoreboardMixin extends Scoreboard {
}
