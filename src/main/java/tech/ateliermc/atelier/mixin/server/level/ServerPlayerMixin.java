package tech.ateliermc.atelier.mixin.server.level;


import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.mixin.world.entity.player.PlayerMixin;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin extends PlayerMixin implements ServerPlayerBridge {
    private Component listName;
    private Component nickName;

    @Shadow @Final
    public MinecraftServer server;

    @Override
    public void setPlayerListName(Component component) {
        this.listName = component;
    }

    @Override
    public Component getNick() {
        return null;
    }

    /**
     * @author CraftBukkit
     */
    @Overwrite
    public Component getTabListDisplayName() {
        return this.listName;
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard != null ? this.scoreboard : new ServerScoreboard(server);
    }
}
