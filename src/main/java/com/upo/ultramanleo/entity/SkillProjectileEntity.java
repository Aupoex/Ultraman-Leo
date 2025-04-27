package com.upo.ultramanleo.entity;

import com.upo.ultramanleo.UltramanLeo;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import java.util.List;
import java.util.UUID;

public class SkillProjectileEntity extends Projectile implements IEntityWithComplexSpawn {

    private static final EntityDataAccessor<Float> DATA_DAMAGE =
            SynchedEntityData.defineId(SkillProjectileEntity.class, EntityDataSerializers.FLOAT);

    private int lifeTicks = 0;
    private static final int MAX_LIFE_TICKS = 60;
    private static final float EXPLOSION_RADIUS = 3.5f;
    private static final boolean DESTROY_TERRAIN = true;
    private static final boolean BREAK_BEDROCK = true;

    public SkillProjectileEntity(EntityType<? extends SkillProjectileEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public SkillProjectileEntity(Level level, double x, double y, double z) {
        this(ModEntities.SKILL_PROJECTILE.get(), level);
        this.setPos(x, y, z);
    }

    public SkillProjectileEntity(Level level, LivingEntity owner) {
        this(ModEntities.SKILL_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.setPos(owner.getX() + owner.getViewVector(1.0f).x,
                owner.getEyeY() - 0.1 + owner.getViewVector(1.0f).y,
                owner.getZ() + owner.getViewVector(1.0f).z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

        builder.define(DATA_DAMAGE, 10.0f);
    }

    public void setCustomDamage(float damage) {
        this.entityData.set(DATA_DAMAGE, Math.max(0f, damage));
    }
    public float getCustomDamage() {
        return this.entityData.get(DATA_DAMAGE);
    }

    @Override
    public void tick() {
        super.tick();
        this.lifeTicks++;
        Vec3 currentPos = this.position();
        Vec3 currentDelta = this.getDeltaMovement();

        if (this.level().isClientSide) {
            // level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
            return;
        }


        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
            return;
        }

        if (this.lifeTicks >= MAX_LIFE_TICKS) {
            this.triggerExplosion();
            this.discard();
            return;
        }
        Vec3 delta = this.getDeltaMovement();
        double nextX = this.getX() + delta.x;
        double nextY = this.getY() + delta.y;
        double nextZ = this.getZ() + delta.z;
        this.setPos(nextX, nextY, nextZ);
        this.checkInsideBlocks();
    }

    @Override
    protected void onHit(HitResult result) {
        if (!this.level().isClientSide()) {
            UltramanLeo.LOGGER.debug("Skill projectile hit something at {}", result.getLocation());
            this.triggerExplosion();
            this.discard();
        }
    }

    private void triggerExplosion() {
        Level level = this.level();
        if (!level.isClientSide()) {
            if (DESTROY_TERRAIN) {
                BlockPos center = this.blockPosition();
                int radiusInt = (int) Math.ceil(EXPLOSION_RADIUS);
                for (int x = -radiusInt; x <= radiusInt; x++) { /* ... */
                    for (int y = -radiusInt; y <= radiusInt; y++) { /* ... */
                        for (int z = -radiusInt; z <= radiusInt; z++) {
                            BlockPos currentPos = center.offset(x, y, z);
                            if (center.distSqr(currentPos) <= EXPLOSION_RADIUS * EXPLOSION_RADIUS) {
                                BlockState blockState = level.getBlockState(currentPos);
                                if (!blockState.isAir()) {
                                    if (blockState.is(Blocks.BEDROCK) || blockState.is(Blocks.END_PORTAL_FRAME)) {
                                        if (!BREAK_BEDROCK) continue;
                                    }
                                    level.removeBlock(currentPos, false);
                                }
                            }
                        }
                    }
                }
            }
            AABB damageArea = this.getBoundingBox().inflate(EXPLOSION_RADIUS);
            List<LivingEntity> entitiesInRange = level.getEntitiesOfClass(LivingEntity.class, damageArea /*...*/);
            float damage = getCustomDamage();
            DamageSource damageSource = damageSources().indirectMagic(this, this.getOwner());
            for (LivingEntity entity : entitiesInRange) {
                entity.hurt(damageSource, damage);
            }
            level.explode(this.getOwner(), damageSource, null, this.getX(), this.getY(), this.getZ(), EXPLOSION_RADIUS, false, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeFloat(this.getCustomDamage());
        Entity owner = this.getOwner();
        buffer.writeBoolean(owner != null);
        if (owner != null) {
            buffer.writeUUID(owner.getUUID());
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {

        float damage = buffer.readFloat();
        this.setCustomDamage(damage);
        boolean hasOwner = buffer.readBoolean();
        if (hasOwner) {
            UUID ownerUUID = buffer.readUUID();
        }
    }

    @Override protected void readAdditionalSaveData(CompoundTag compound) {  compound.putFloat("Damage", getCustomDamage());}
    @Override protected void addAdditionalSaveData(CompoundTag compound) {  entityData.set(DATA_DAMAGE, compound.getFloat("Damage")); }
    @Override public boolean displayFireAnimation() { return false; }
    @Override public boolean isPickable() { return false; }
}

