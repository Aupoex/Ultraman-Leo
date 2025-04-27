package com.upo.ultramanleo.client.model.armor;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.item.armor.UltramanLeoLeggingsItem; // 导入对应的物品类
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class UltramanLeoLeggingsModel extends GeoModel<UltramanLeoLeggingsItem> {

    private static final ResourceLocation MODEL_RESOURCE = UltramanLeo.id("geo/armor/ultraman_leo_leggings.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = UltramanLeo.id("textures/armor/ultraman_leo_texture.png");
    private static final ResourceLocation ANIMATION_RESOURCE = UltramanLeo.id("animations/armor/ultraman_leo_armor.animation.json");

    @Override
    public ResourceLocation getModelResource(UltramanLeoLeggingsItem animatable) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(UltramanLeoLeggingsItem animatable) {
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(UltramanLeoLeggingsItem animatable) {
        return ANIMATION_RESOURCE;
    }
}
