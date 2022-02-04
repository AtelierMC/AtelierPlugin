package tech.ateliermc.atelier.mixin.server.level;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.ateliermc.atelier.common.AtelierSit;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;
    @Shadow private GameType gameModeForPlayer;

    @Shadow protected ServerLevel level;

    @Inject(method = "useItemOn(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void inject(ServerPlayer serverPlayer, Level level, ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (gameModeForPlayer == GameType.SPECTATOR || (!player.getInventory().getSelected().isEmpty()) || player.isShiftKeyDown()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }

        BlockPos blockPos = blockHitResult.getBlockPos();

        if (level.getBlockState(blockPos.offset(0, 1, 0)).canOcclude())
            return;

        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (!(block instanceof StairBlock || block instanceof SlabBlock) || blockState.isFaceSturdy(level, blockPos, Direction.UP, SupportType.RIGID)) {
            return;
        }

        Vec3 lookTarget;
        Vec3 blockPosOffset;
        if (block instanceof StairBlock) {
            Direction direction = blockState.getValue(StairBlock.FACING);
            Vector3f offset = direction.step();
            StairsShape stairShape = blockState.getValue(StairBlock.SHAPE);
            if (stairShape == StairsShape.OUTER_RIGHT || stairShape == StairsShape.INNER_RIGHT)
                offset.add(direction.getClockWise().step());
            if (stairShape == StairsShape.OUTER_LEFT || stairShape == StairsShape.INNER_LEFT)
                offset.add(direction.getCounterClockWise().step());
            lookTarget = new Vec3(blockPos.getX() + 0.5 - offset.x(), blockPos.getY(), blockPos.getZ() + 0.5 - offset.z());
            blockPosOffset = new Vec3(offset);
            blockPosOffset = blockPosOffset.scale(-3f/16f);
        }
        else {
            lookTarget = player.position();
            blockPosOffset = Vec3.ZERO;
        }

        blockPosOffset = blockPosOffset.add(0, -1.2, 0);
        Entity chair = AtelierSit.createChair(level, blockPos, blockPosOffset, lookTarget, true);

        Entity v = player.getVehicle();
        if (v != null) {
            player.setShiftKeyDown(true);
            player.rideTick();
        }
        player.startRiding(chair, true);
        cir.setReturnValue(InteractionResult.sidedSuccess(true));
        cir.cancel();
    }

    @Inject(method = "setGameModeForPlayer(Lnet/minecraft/world/level/GameType;Lnet/minecraft/world/level/GameType;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    public void inject(GameType gameType, GameType previousGameType, CallbackInfo ci) {
        if (gameType == GameType.SPECTATOR && previousGameType != GameType.SPECTATOR && player.getVehicle() != null) {
            player.setShiftKeyDown(true);
            player.rideTick();
        }
    }
}
