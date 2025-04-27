package com.upo.ultramanleo.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.jetbrains.annotations.Nullable;

public class ModCapabilities {
    public static final EntityCapability<ITransformationState, @Nullable Void> TRANSFORMATION_STATE =
            EntityCapability.createVoid(
                    ResourceLocation.fromNamespaceAndPath("ultramanleo", "transformation_state"),
                    ITransformationState.class
            );

}

