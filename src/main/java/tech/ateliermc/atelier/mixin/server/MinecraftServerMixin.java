package tech.ateliermc.atelier.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.ateliermc.atelier.common.hologram.AtelierHologram;
import tech.ateliermc.atelier.common.hologram.AtelierHologramEntity;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(at = @At("TAIL"), method = "loadLevel")
    public void afterWorldLoad(CallbackInfo ci) {
        for (AtelierHologram.Data holo : AtelierHologram.hologramMap.values()) {
            ((MinecraftServer)(Object) this).getAllLevels().forEach(w -> {
                if (holo.world.equalsIgnoreCase(w.dimension().toString())) {
                    AtelierHologramEntity entity = new AtelierHologramEntity(w, holo);
                    AtelierHologram.entityMap.put(holo, entity);
                    w.addFreshEntity(entity);
                }
            });
        }
    }
}
