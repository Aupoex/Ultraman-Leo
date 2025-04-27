package com.upo.ultramanleo.event;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.capabilities.ModCapabilities;
import com.upo.ultramanleo.capabilities.TransformationState;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = UltramanLeo.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                ModCapabilities.TRANSFORMATION_STATE,
                EntityType.PLAYER,
                (player, context) -> {
                    return new TransformationState();
                }
        );
    }
}

