package com.upo.ultramanleo;

import com.upo.ultramanleo.creative_tab.ModCreativeTabs;
import com.upo.ultramanleo.entity.ModEntities;
import com.upo.ultramanleo.item.ModItems;
import com.upo.ultramanleo.item.material.ModArmorMaterials;
import com.upo.ultramanleo.network.NetworkHandler;
import com.upo.ultramanleo.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.upo.ultramanleo.attachments.ModAttachments;

@Mod(UltramanLeo.MODID)
public class UltramanLeo
{
    public static final String MODID = "ultramanleo";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final ResourceLocation ULTRAMAN_MAX_HEALTH_MODIFIER_ID = UltramanLeo.id("ultraman_max_health");
    public static final ResourceLocation ULTRAMAN_ARMOR_MODIFIER_ID = UltramanLeo.id("ultraman_armor");
    public static final ResourceLocation ULTRAMAN_ARMOR_TOUGHNESS_MODIFIER_ID = UltramanLeo.id("ultraman_armor_toughness");
    public static final ResourceLocation ULTRAMAN_MOVEMENT_SPEED_MODIFIER_ID = UltramanLeo.id("ultraman_movement_speed");
    public static final ResourceLocation ULTRAMAN_ATTACK_DAMAGE_MODIFIER_ID = UltramanLeo.id("ultraman_attack_damage");

    public UltramanLeo(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(NetworkHandler::register);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
