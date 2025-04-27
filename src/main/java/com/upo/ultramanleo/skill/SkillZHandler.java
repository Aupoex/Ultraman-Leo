package com.upo.ultramanleo.skill;

import com.upo.ultramanleo.UltramanLeo; // 主类
import com.upo.ultramanleo.capabilities.ITransformationState; // 状态接口
import com.upo.ultramanleo.sound.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SkillZHandler {

    private static final int SKILL_Z_UPWARD_TICKS = 12;
    private static final double SKILL_Z_UPWARD_FORCE = 0.7;
    private static final double SKILL_Z_FORWARD_SPEED = 1.8;
    private static final double SKILL_Z_DOWNWARD_FACTOR = -0.6;
    private static final float SKILL_Z_DAMAGE = 100.0f;
    private static final int SKILL_Z_MAX_DURATION_TICKS = 40;
    private static final int SKILL_Z_COOLDOWN_TICKS = 100;
    private static final float SKILL_Z_EXPLOSION_RADIUS = 3.0f;
    private static final float SKILL_Z_EXPLOSION_DAMAGE = 100.0f;
    private static final double SKILL_Z_UPWARD_FORCE_PER_TICK = 0.8;
    private static final double MAX_UPWARD_VELOCITY = 10;

    public static void startSkill(ServerPlayer player, ITransformationState state) {

        if (!state.isUsingSkillZ() && state.isTransformed() && state.getSkillZCooldownTicks() <= 0) {
            state.setUsingSkillZ(true);
            state.setSkillZProgress(1);
            state.resetSkillZTimer();
            player.hurtMarked = true;
            player.fallDistance = 0;
            Level level = player.level();
            level.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.SKILL_Z_START_SOUND_EVENT.get(),
                    SoundSource.PLAYERS,
                    0.8F,
                    1.0F
            );
        }
    }
    public static void handleTick(ServerPlayer player, ITransformationState state) {
        if (!state.isUsingSkillZ()) {
            return;
        }
        Level level = player.level();
        int progress = state.getSkillZProgress();

        if (progress == 1) {
            player.fallDistance = 0;
            double currentYVel = player.getDeltaMovement().y;
            double upwardPush = SKILL_Z_UPWARD_FORCE_PER_TICK;
            player.setDeltaMovement(player.getDeltaMovement().x,
                    Math.min(currentYVel + upwardPush, MAX_UPWARD_VELOCITY),
                    player.getDeltaMovement().z);
            player.hurtMarked = true;

            if (state.getSkillZTimerTicks() >= SKILL_Z_UPWARD_TICKS) {
                state.setSkillZProgress(2);
                state.resetSkillZTimer();

                Vec3 lookAngle = player.getViewVector(1.0F);
                player.push(lookAngle.x * 0.2, 0, lookAngle.z * 0.2);
            }
        }
        else if (progress == 2) {
            player.fallDistance = 0;

            Vec3 lookAngle = player.getViewVector(1.0F);
            Vec3 moveDirection = new Vec3(lookAngle.x, SKILL_Z_DOWNWARD_FACTOR, lookAngle.z).normalize();
            player.setDeltaMovement(moveDirection.multiply(SKILL_Z_FORWARD_SPEED, SKILL_Z_FORWARD_SPEED, SKILL_Z_FORWARD_SPEED));
            player.hurtMarked = true;

            Vec3 currentMotion = player.getDeltaMovement();
            AABB playerBox = player.getBoundingBox();
            AABB nextBox = playerBox.expandTowards(currentMotion.x, currentMotion.y, currentMotion.z).inflate(0.1);
            Iterable<VoxelShape> blockCollisionIterable = level.getBlockCollisions(player, nextBox);
            List<VoxelShape> blockCollisions = new ArrayList<>();
            blockCollisionIterable.forEach(blockCollisions::add);
            List<Entity> entityCollisions = level.getEntities(player, nextBox, e -> !e.isSpectator() && e.isPickable() && e != player);
            boolean hitBlock = !blockCollisions.isEmpty();
            boolean hitEntity = !entityCollisions.isEmpty();

            if (hitBlock || hitEntity /*|| timeUp*/) {
                boolean triggeredExplosion = false;

                if (hitEntity) {
                    UltramanLeo.LOGGER.debug("Player {} collided with entity during Z skill.", player.getName().getString());
                    DamageSource source = player.damageSources().playerAttack(player);
                    for (Entity target : entityCollisions) {
                        if (target instanceof LivingEntity livingTarget) {
                            // 对直接碰撞的目标造成单体伤害
                            livingTarget.hurt(source, SKILL_Z_DAMAGE);
                            UltramanLeo.LOGGER.debug("Player {} damaged entity {} with Z skill (direct).", player.getName().getString(), target.getName().getString());
                        }
                    }
                    triggerNonBreakingExplosion(level, player, player.position());
                    triggeredExplosion = true;

                } else if (hitBlock) {
                    UltramanLeo.LOGGER.debug("Player {} collided with block during Z skill.", player.getName().getString());

                }
                endSkill(player, state);
            }
        }
    }

    public static void endSkill(ServerPlayer player, ITransformationState state) {
        if (state.isUsingSkillZ()) {

            state.setUsingSkillZ(false);
            state.setSkillZProgress(0);
            state.setSkillZCooldown(SKILL_Z_COOLDOWN_TICKS);
            player.setDeltaMovement(player.getDeltaMovement().multiply(0.1, 0.1, 0.1));
            // player.setDeltaMovement(Vec3.ZERO);
            player.fallDistance = 0;
        }
    }

    private static void triggerNonBreakingExplosion(Level level, @Nullable Entity source, Vec3 position) {
        if (!level.isClientSide()) {

            level.explode(
                    source,
                    null,
                    null,
                    position.x(), position.y(), position.z(),
                    SKILL_Z_EXPLOSION_RADIUS,
                    true,
                    Level.ExplosionInteraction.NONE
            );
        }
    }
}
