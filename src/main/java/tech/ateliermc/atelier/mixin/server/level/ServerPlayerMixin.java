package tech.ateliermc.atelier.mixin.server.level;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.mixin.world.entity.player.PlayerMixin;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements ServerPlayerBridge {
    private Component listName;

    @Unique
    private boolean enableScoreboard = true;

    @Shadow @Final
    public MinecraftServer server;

    @Override
    public void setPlayerListName(Component component) {
        this.listName = component;
    }

    @Override
    public Component getDisplayName() {
        return ((ServerPlayer) (Object) this).hasCustomName() ? ((ServerPlayer) (Object) this).getCustomName() : super.getDisplayName();
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

    @Override
    public void setEnableScoreboard(boolean enableScoreboard) {
        this.enableScoreboard = enableScoreboard;
    }

    @Override
    public boolean enableScoreboard() {
        return this.enableScoreboard;
    }

    @Inject(method = "addAdditionalSaveData",
            at = @At(
                    value = "TAIL"
            )
    )
    private void addCustomData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("EnableScoreboard", this.enableScoreboard);
    }

    @Inject(method = "readAdditionalSaveData",
            at = @At(
                    value = "TAIL"
            )
    )
    private void loadCustomData(CompoundTag compoundTag, CallbackInfo ci) {
        this.enableScoreboard = compoundTag.getBoolean("EnableScoreboard");
    }
}
