package tech.ateliermc.atelier.common;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AtelierSit {
    public static final Set<Entity> _chairs = new HashSet<Entity>();

    public static Entity createChair(Level level, BlockPos blockPos, Vec3 blockPosOffset, @Nullable Vec3 target, boolean boundToBlock) {
        ArmorStand entity = new ArmorStand(level, 0.5d + blockPos.getX() + blockPosOffset.x(), blockPos.getY() + blockPosOffset.y(), 0.5d + blockPos.getZ() + blockPosOffset.z()) {
            private boolean v = false;

            @Override
            protected void addPassenger(Entity passenger) {
                super.addPassenger(passenger);
                v = true;
            }

            @Override
            public boolean isEffectiveAi() {
                return false;
            }

            @Override
            public boolean isPickable() {
                return false;
            }

            @Override
            public void tick() {
                if (v && this.getPassengers().size() < 1) {
                    kill();
                }
                if (getLevel().getBlockState(eyeBlockPosition()).isAir() && boundToBlock) {
                    kill();
                }
                super.tick();
            }

        };
        if (target != null)
            entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.subtract(0, (target.y() * 2), 0));
        entity.setInvisible(true);
        entity.setInvulnerable(true);
        entity.setCustomName(new TextComponent("FABRIC_SEAT"));
        entity.setNoGravity(true);
        level.addFreshEntity(entity);

        _chairs.add(entity);

        return entity;
    }
}
