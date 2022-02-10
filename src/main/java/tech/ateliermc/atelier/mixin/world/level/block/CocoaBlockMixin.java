package tech.ateliermc.atelier.mixin.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.ateliermc.atelier.mixin.world.level.state.BlockBehaviourMixin;

@Mixin(CocoaBlock.class)
public class CocoaBlockMixin extends BlockBehaviourMixin {
    @Override
    public void rightClickHarvest(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (blockState.getValue(CocoaBlock.AGE) >= CocoaBlock.MAX_AGE) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(CocoaBlock.AGE, 0));
            Block.dropResources(blockState, level, blockPos, null, player, player.getItemInHand(interactionHand));

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
