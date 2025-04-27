package com.upo.ultramanleo.entity;

import com.upo.ultramanleo.UltramanLeo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, UltramanLeo.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SkillProjectileEntity>> SKILL_PROJECTILE =
            ENTITY_TYPES.register("skill_projectile",
                    () -> EntityType.Builder.<SkillProjectileEntity>of(SkillProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            // .setShouldReceiveVelocityUpdates(true)
                            .build("skill_projectile"));
}
