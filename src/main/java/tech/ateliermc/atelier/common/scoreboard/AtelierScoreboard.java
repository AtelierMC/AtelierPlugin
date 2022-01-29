package tech.ateliermc.atelier.common.scoreboard;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class AtelierScoreboard {

    public void init(ServerPlayer player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("AtelierMC");
    }
    
}
