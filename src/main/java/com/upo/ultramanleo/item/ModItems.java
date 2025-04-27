package com.upo.ultramanleo.item;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.item.armor.UltramanLeoChestplateItem;
import com.upo.ultramanleo.item.armor.UltramanLeoHelmetItem;
import com.upo.ultramanleo.item.armor.UltramanLeoLeggingsItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(UltramanLeo.MODID);

    public static final DeferredItem<Item> TRANSFORMATION_DEVICE = ITEMS.register(
            "transformation_device",
            () -> new TransformationDeviceItem(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<Item> ULTRAMAN_HELMET = ITEMS.register(
            "ultraman_helmet",
            () -> new UltramanLeoHelmetItem(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<Item> ULTRAMAN_CHESTPLATE = ITEMS.register(
            "ultraman_chestplate",
            () -> new UltramanLeoChestplateItem(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<Item> ULTRAMAN_LEGGINGS = ITEMS.register(
            "ultraman_leggings",
            () -> new UltramanLeoLeggingsItem(new Item.Properties().stacksTo(1))
    );

}