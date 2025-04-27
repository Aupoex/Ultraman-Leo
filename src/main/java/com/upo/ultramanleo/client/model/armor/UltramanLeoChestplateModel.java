package com.upo.ultramanleo.client.model.armor;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.item.armor.UltramanLeoChestplateItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class UltramanLeoChestplateModel extends GeoModel<UltramanLeoChestplateItem> {

    private static final ResourceLocation MODEL_RESOURCE = UltramanLeo.id("geo/armor/ultraman_leo_chestplate.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = UltramanLeo.id("textures/armor/ultraman_leo_texture.png");
    private static final ResourceLocation ANIMATION_RESOURCE = UltramanLeo.id("animations/armor/ultraman_leo_armor.animation.json");

    @Override
    public ResourceLocation getModelResource(UltramanLeoChestplateItem animatable) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(UltramanLeoChestplateItem animatable) {
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(UltramanLeoChestplateItem animatable) {
        return ANIMATION_RESOURCE;
    }
}
