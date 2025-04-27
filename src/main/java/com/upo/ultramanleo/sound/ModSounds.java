package com.upo.ultramanleo.sound;

import com.upo.ultramanleo.UltramanLeo;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, UltramanLeo.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> TRANSFORM_SOUND_EVENT = SOUND_EVENTS.register(
            "item.transformation_device.use",
            () -> SoundEvent.createVariableRangeEvent(UltramanLeo.id("item.transformation_device.use"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> SKILL_Z_START_SOUND_EVENT = SOUND_EVENTS.register(
            "skill.z.start",
            () -> SoundEvent.createVariableRangeEvent(UltramanLeo.id("skill.z.start"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> SKILL_X_SHOOT_SOUND_EVENT = SOUND_EVENTS.register(
            "skill.x.shoot",
            () -> SoundEvent.createVariableRangeEvent(UltramanLeo.id("skill.x.shoot"))
    );
}