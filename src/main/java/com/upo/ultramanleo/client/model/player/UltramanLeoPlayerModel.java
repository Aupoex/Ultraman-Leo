package com.upo.ultramanleo.client.model.player;

import com.upo.ultramanleo.UltramanLeo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class UltramanLeoPlayerModel<T extends GeoAnimatable> extends GeoModel<T> {

    private static final ResourceLocation MODEL_RESOURCE = UltramanLeo.id("geo/player/ultraman_leo_player.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = UltramanLeo.id("textures/player/ultraman_leo_texture.png");
    private static final ResourceLocation ANIMATION_RESOURCE = UltramanLeo.id("animations/player/ultraman_leo_player.animation.json");

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION_RESOURCE;
    }

}