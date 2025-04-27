package com.upo.ultramanleo.creative_tab;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltramanLeo.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ULTRAMANLEO_TAB = CREATIVE_MODE_TABS.register(
            "ultramanleo_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.TRANSFORMATION_DEVICE.get()))
                    .title(Component.translatable("creativetab.ultramanleo_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TRANSFORMATION_DEVICE.get());
                        output.accept(ModItems.ULTRAMAN_HELMET.get());
                        output.accept(ModItems.ULTRAMAN_CHESTPLATE.get());
                        output.accept(ModItems.ULTRAMAN_LEGGINGS.get());
                    })
                    .build()
    );
}
