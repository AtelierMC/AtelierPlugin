package tech.ateliermc.atelier.mixin.world.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;

@Mixin(Player.class)
public class PlayerMixin implements PlayerBridge {
    protected Scoreboard scoreboard;

    /**
     * @author JustMango
     */
    @Overwrite
    public Scoreboard getScoreboard() {
        return this.scoreboard != null ? this.scoreboard : ((Player) (Object) this).getLevel().getScoreboard();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
}
