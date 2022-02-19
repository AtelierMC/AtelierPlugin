package tech.ateliermc.atelier.mixin.world.entity.player;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerBridge {
    protected Scoreboard scoreboard;

    @Shadow
    protected abstract MutableComponent decorateDisplayNameComponent(MutableComponent mutableComponent);

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

    /**
     * @author JustMango
     */
    @Overwrite
    public Component getDisplayName() {
        return this.decorateDisplayNameComponent(PlayerTeam.formatNameForTeam(((Player) (Object) this).getTeam(), ((Player) (Object) this).getName()));
    }
}
