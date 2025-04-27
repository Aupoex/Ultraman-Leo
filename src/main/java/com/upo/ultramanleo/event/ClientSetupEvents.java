package com.upo.ultramanleo.event;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.upo.ultramanleo.client.keybinding.ModKeyBindings;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import com.upo.ultramanleo.client.renderer.entity.SkillProjectileRenderer;

@EventBusSubscriber(modid = UltramanLeo.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SKILL_PROJECTILE.get(), SkillProjectileRenderer::new);
    }
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyBindings.ULTRAMAN_Z_KEY);
        event.register(ModKeyBindings.ULTRAMAN_X_KEY);
    }
}
