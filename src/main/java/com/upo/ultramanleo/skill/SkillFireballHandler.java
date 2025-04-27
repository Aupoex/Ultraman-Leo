package com.upo.ultramanleo.skill;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.entity.ModEntities;
import com.upo.ultramanleo.entity.SkillProjectileEntity;
import com.upo.ultramanleo.sound.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SkillFireballHandler {

    private static final double PROJECTILE_SPEED = 1.8;
    private static final float PROJECTILE_DAMAGE = 100.0f;
    private static final int FIREBALL_COOLDOWN_TICKS = 40;
    public static void shootFireball(ServerPlayer player, ITransformationState state) {
        Level level = player.level();

        Vec3 lookAngle = player.getViewVector(1.0F);
        double spawnX = player.getX() + lookAngle.x * 1.5;
        double spawnY = player.getEyeY() - 0.2 + lookAngle.y * 1.5;
        double spawnZ = player.getZ() + lookAngle.z * 1.5;
        Vec3 initialMovement = lookAngle.scale(PROJECTILE_SPEED);

        SkillProjectileEntity projectile = new SkillProjectileEntity(ModEntities.SKILL_PROJECTILE.get(), level);

        projectile.setOwner(player);
        projectile.setPos(spawnX, spawnY, spawnZ);
        projectile.setDeltaMovement(initialMovement);
        projectile.setCustomDamage(PROJECTILE_DAMAGE);
        level.addFreshEntity(projectile);

        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                ModSounds.SKILL_X_SHOOT_SOUND_EVENT.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
        UltramanLeo.LOGGER.debug("Player {} shot a skill projectile.", player.getName().getString());
    }
}
