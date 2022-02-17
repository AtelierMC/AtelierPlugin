package tech.ateliermc.atelier.common.hologram;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AtelierHologramEntity extends ArmorStand implements Serializable {
    @Serial
    private static final long serialVersionUID = -2032111199365772255L;

    public List<AtelierHologramEntity> children = new ArrayList<>();

    private AtelierHologram.Data data;

    public AtelierHologramEntity(String name, Level level, double x, double y, double z) {
        super(level, x, y, z);
        this.setInvisible(true);
        this.setCustomNameVisible(true);
        this.data = new AtelierHologram.Data();
        this.data.world = level.dimension().toString();
        this.data.x = x;
        this.data.y = y;
        this.data.z = z;
        if (!name.equalsIgnoreCase("HOLOGRAM_CHILDGRAM")) {
            AtelierHologram.hologramMap.put(name, data);
            AtelierHologram.save();
        }
    }

    public AtelierHologramEntity(Level level, AtelierHologram.Data holo) {
        super(level, holo.x, holo.y, holo.z);
        this.data = holo;
        this.setInvisible(true);
        this.setCustomNameVisible(true);
        this.setLines(holo.lines);
    }

    public AtelierHologram.Data getHologramData() {
        return data;
    }

    @Override
    public void aiStep() {
        // Remove AI
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        // disable
    }

    @Override
    public boolean save(CompoundTag tag) {
        return false; // disable method
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public void kill() {
        // Prevent being killed
    }

    public void killSuper() {
        super.kill();
        super.dead = true;
    }

    public void setLocation(double x, double y, double z) {
        super.teleportTo(x, y, z);
        this.data.x = x;
        this.data.y = y;
        this.data.z = z;
    }

    public String[] getLines() {
        return data.lines;
    }

    public void setLines(String[] lines) {
        this.data.lines = lines;
        for (AtelierHologramEntity child : children)
            child.killSuper();

        this.setCustomName(new TextComponent(lines[0]));
        AtelierHologramEntity lastChild = this;
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) continue;

            AtelierHologramEntity child = new AtelierHologramEntity("HOLOGRAM_CHILDGRAM", this.level, this.getX(), lastChild.getY() - 0.27, this.getZ());
            child.setCustomName(new TextComponent(lines[i]));
            this.level.addFreshEntity(lastChild = child);
            this.children.add(child);
        }
    }
}
