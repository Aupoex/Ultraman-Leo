package com.upo.ultramanleo.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.capabilities.TransformationState;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, UltramanLeo.MODID);
    public static final Codec<TransformationState> TRANSFORMATION_STATE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("transformTicks").orElse(0).forGetter(ITransformationState::getTransformTicks),
            Codec.INT.fieldOf("cooldownTicks").orElse(0).forGetter(ITransformationState::getCooldownTicks)
    ).apply(instance, (transform, cooldown) -> {
        TransformationState state = new TransformationState();
        state.setTransformTicks(transform);
        state.setCooldownTicks(cooldown);
        return state;
    }));
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TransformationState>> TRANSFORMATION_STATE =
            ATTACHMENT_TYPES.register(
                    "transformation_state",
                    () -> AttachmentType.builder(TransformationState::new)
                            .serialize(TransformationState.CODEC)
                            .copyOnDeath()
                            .build()
            );
}
