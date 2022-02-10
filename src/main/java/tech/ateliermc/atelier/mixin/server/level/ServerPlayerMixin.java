package tech.ateliermc.atelier.mixin.server.level;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.mixin.world.entity.player.PlayerMixin;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin extends PlayerMixin implements ServerPlayerBridge {
    private Component listName;

    @Override
    public void setPlayerListName(Component component) {
        this.listName = component;
    }

    /**
     * @author CraftBukkit
     */
    @Overwrite
    public Component getTabListDisplayName() {
        return this.listName;
    }
}
