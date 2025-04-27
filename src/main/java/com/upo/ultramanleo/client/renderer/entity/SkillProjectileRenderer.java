package com.upo.ultramanleo.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.entity.SkillProjectileEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SkillProjectileRenderer extends EntityRenderer<SkillProjectileEntity> {

    private static final ResourceLocation FIREBALL_TEXTURE = UltramanLeo.id("textures/entity/projectile/skill_fireball.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(FIREBALL_TEXTURE);

    public SkillProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(SkillProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float scale = 1.2f;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(new Quaternionf().rotateY(Mth.PI));
        VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        float size = 0.5f;
        int fullBrightLight = LightTexture.pack(15, 15);
        vertexconsumer.addVertex(matrix4f, -size, -size, 0.0F).setColor(255, 255, 255, 255).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fullBrightLight).setNormal(0.0F, 0.0F, 1.0F);
        vertexconsumer.addVertex(matrix4f, size, -size, 0.0F).setColor(255, 255, 255, 255).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fullBrightLight).setNormal(0.0F, 0.0F, 1.0F);
        vertexconsumer.addVertex(matrix4f, size, size, 0.0F).setColor(255, 255, 255, 255).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fullBrightLight).setNormal(0.0F, 0.0F, 1.0F);
        vertexconsumer.addVertex(matrix4f, -size, size, 0.0F).setColor(255, 255, 255, 255).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fullBrightLight).setNormal(0.0F, 0.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SkillProjectileEntity entity) {
        return FIREBALL_TEXTURE;
    }
}
