package com.upo.ultramanleo.capabilities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

public class TransformationState implements ITransformationState{
    private int transformTicks = 0;
    private int cooldownTicks = 0;
    private boolean usingSkillZ = false;
    private int skillZProgress = 0;
    private int skillZCooldownTicks = 0;
    private int skillZTimerTicks = 0;

    public static final Codec<TransformationState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("transformTicks").orElse(0).forGetter(TransformationState::getTransformTicks),
            Codec.INT.fieldOf("cooldownTicks").orElse(0).forGetter(TransformationState::getCooldownTicks),
            Codec.BOOL.fieldOf("usingSkillZ").orElse(false).forGetter(TransformationState::isUsingSkillZ),
            Codec.INT.fieldOf("skillZProgress").orElse(0).forGetter(TransformationState::getSkillZProgress),
            Codec.INT.fieldOf("skillZCooldownTicks").orElse(0).forGetter(TransformationState::getSkillZCooldownTicks),
            // Codec.INT.fieldOf("skillZDurationTimer").orElse(0).forGetter(TransformationState::getSkillZDurationTimer)
            Codec.INT.fieldOf("skillZTimerTicks").orElse(0).forGetter(TransformationState::getSkillZTimerTicks)
    ).apply(instance, TransformationState::new));
    private TransformationState(int transformTicks, int cooldownTicks, boolean usingSkillZ, int skillZProgress, int skillZCooldownTicks, int skillZTimerTicks) {
        this.transformTicks = transformTicks;
        this.cooldownTicks = cooldownTicks;
        this.usingSkillZ = usingSkillZ;
        this.skillZProgress = skillZProgress;
        this.skillZCooldownTicks = skillZCooldownTicks;
        this.skillZTimerTicks = skillZTimerTicks;
    }
    public TransformationState() {}
    @Override
    public boolean isUsingSkillZ() { return this.usingSkillZ; }
    @Override
    public void setUsingSkillZ(boolean using) { this.usingSkillZ = using; }
    @Override
    public int getSkillZProgress() { return this.skillZProgress; }
    @Override
    public void setSkillZProgress(int progress) { this.skillZProgress = progress; }
    @Override
    public int getSkillZCooldownTicks() { return this.skillZCooldownTicks; }
    @Override
    public void setSkillZCooldown(int ticks) { this.skillZCooldownTicks = ticks; }
    @Override
    public void tickSkillZCooldown() {
        if (this.skillZCooldownTicks > 0) {
            this.skillZCooldownTicks--;
        }
    }
    @Override
    public boolean isTransformed() {
        return this.transformTicks > 0;
    }
    @Override
    public int getTransformTicks() {
        return this.transformTicks;
    }
    @Override
    public void setTransformTicks(int ticks) {
        this.transformTicks = Math.max(0, ticks);
    }
    @Override
    public int getCooldownTicks() {
        return this.cooldownTicks;
    }
    @Override
    public void setCooldownTicks(int ticks) {
        this.cooldownTicks = Math.max(0, ticks);
    }
    @Override
    public void startTransform(int durationTicks) {
        setTransformTicks(durationTicks);
    }
    @Override
    public void endTransform() {
        setTransformTicks(0);
        setCooldownTicks(com.upo.ultramanleo.item.TransformationDeviceItem.COOLDOWN_DURATION_TICKS);
        setUsingSkillZ(false);
        setSkillZProgress(0);
        setSkillZCooldown(0);
    }

    @Override
    public void tick() {
        if (this.isTransformed() && !this.isUsingSkillZ() && this.transformTicks > 0) {
            this.transformTicks--;
        }
        this.tickCooldown();
        this.tickSkillZCooldown();
        if (this.isUsingSkillZ()) {
            this.tickSkillZTimer();
        }
    }
    @Override
    public void tickCooldown() {
        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
        }
    }
    @Override public int getSkillZTimerTicks() { return this.skillZTimerTicks; }
    @Override public void tickSkillZTimer() { this.skillZTimerTicks++; }
    @Override public void resetSkillZTimer() { this.skillZTimerTicks = 0; }

    public void saveNBTData(CompoundTag nbt) {
        com.upo.ultramanleo.UltramanLeo.LOGGER.info(">>> Saving TransformationState NBT data...");
        nbt.putInt("transformTicks", this.transformTicks);
        nbt.putInt("cooldownTicks", this.cooldownTicks);
        nbt.putBoolean("usingSkillZ", this.usingSkillZ);
        nbt.putInt("skillZProgress", this.skillZProgress);
        nbt.putInt("skillZCooldownTicks", this.skillZCooldownTicks);
    }

    public void loadNBTData(CompoundTag nbt) {
        com.upo.ultramanleo.UltramanLeo.LOGGER.info("<<< Loading TransformationState NBT data...");
        this.transformTicks = nbt.getInt("transformTicks");
        this.cooldownTicks = nbt.getInt("cooldownTicks");
        if (nbt.contains("usingSkillZ")) {
            this.usingSkillZ = nbt.getBoolean("usingSkillZ");
        } else {
            this.usingSkillZ = false;
        }
        if (nbt.contains("skillZProgress")) {
            this.skillZProgress = nbt.getInt("skillZProgress");
        } else {
            this.skillZProgress = 0;
        }
        if (nbt.contains("skillZCooldownTicks")) {
            this.skillZCooldownTicks = nbt.getInt("skillZCooldownTicks");
            com.upo.ultramanleo.UltramanLeo.LOGGER.info("<<< Loaded skillZCooldownTicks: {}", this.skillZCooldownTicks);
        } else {
            this.skillZCooldownTicks = 0;
        }
    }
}
