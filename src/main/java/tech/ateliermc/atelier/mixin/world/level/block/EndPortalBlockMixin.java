package tech.ateliermc.atelier.mixin.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(
            method = "entityInside",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void disableEndPortal(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        ci.cancel();
    }
}
