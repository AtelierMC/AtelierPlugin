package tech.ateliermc.atelier.mixin.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.ateliermc.atelier.mixin.world.level.state.BlockBehaviourMixin;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends BlockBehaviourMixin {
    @Shadow
    public abstract boolean isMaxAge(BlockState state);

    @Override
    public void rightClickHarvest(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (isMaxAge(blockState)) {
            level.setBlockAndUpdate(blockPos, ((CropBlock) (Object) this).getStateForAge(0));
            Block.dropResources(blockState, level, blockPos, null, player, player.getItemInHand(interactionHand));

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
