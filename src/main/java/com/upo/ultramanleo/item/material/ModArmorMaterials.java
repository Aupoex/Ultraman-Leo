package com.upo.ultramanleo.item.material;

import com.upo.ultramanleo.UltramanLeo;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, UltramanLeo.MODID);


    public static final Holder<ArmorMaterial> ULTRAMAN_LEO_ARMOR = ARMOR_MATERIALS.register("ultraman_leo_armor", () ->
            new ArmorMaterial(

                    new EnumMap<>(ArmorItem.Type.class) {{
                        put(ArmorItem.Type.BOOTS, 3);
                        put(ArmorItem.Type.LEGGINGS, 6);
                        put(ArmorItem.Type.CHESTPLATE, 8);
                        put(ArmorItem.Type.HELMET, 3);
                    }},

                    15,

                    SoundEvents.ARMOR_EQUIP_DIAMOND,

                    () -> Ingredient.of(Items.REDSTONE),





                    List.of(new ArmorMaterial.Layer(UltramanLeo.id("ultraman_leo_armor"))),

                    2.0F,

                    0.1F
            ));

    // 如果需要第二层纹理（比如发光层），可以添加第二个 Layer
    // List.of(
    //     new ArmorMaterial.Layer(UltramanLeo.id("ultraman_leo_armor")),
    //     new ArmorMaterial.Layer(UltramanLeo.id("ultraman_leo_glow_armor"), "_overlay", true) // 后缀和染色设置
    // )
}