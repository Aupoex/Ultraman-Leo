package com.upo.ultramanleo.client.model.armor;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.item.armor.UltramanLeoHelmetItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.cache.object.GeoBone;

public class UltramanLeoHelmetModel extends GeoModel<UltramanLeoHelmetItem> {

    private static final ResourceLocation MODEL_RESOURCE = UltramanLeo.id("geo/armor/ultraman_leo_helmet.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = UltramanLeo.id("textures/armor/ultraman_leo_texture.png");
    private static final ResourceLocation ANIMATION_RESOURCE = UltramanLeo.id("animations/armor/ultraman_leo_armor.animation.json");

    @Override
    public ResourceLocation getModelResource(UltramanLeoHelmetItem animatable) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(UltramanLeoHelmetItem animatable) {
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(UltramanLeoHelmetItem animatable) {
        return ANIMATION_RESOURCE;
    }

    @Override
    public void setCustomAnimations(UltramanLeoHelmetItem animatable, long instanceId, AnimationState<UltramanLeoHelmetItem> animationState) {

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (entityData != null) {
            // 获取头部骨骼 (确保你的 Blockbench 模型中有名为 "head" 的骨骼)
            GeoBone headBone = this.getAnimationProcessor().getBone("head");
            if (headBone != null) {
                headBone.setRotX(entityData.headPitch());
                headBone.setRotY(entityData.netHeadYaw());
            }
        }
    }
}