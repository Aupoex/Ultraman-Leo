package com.upo.ultramanleo.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.upo.ultramanleo.UltramanLeo; // 需要主类
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.RenderUtil; // 需要 RenderUtil

// T 是 Animatable 类型 (我们的盔甲 Item), O 是相关对象 (通常同T), R 是 RenderState (通常同T)
public class UltramanLeoGlowLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    // 发光纹理，需要一个方法从 renderer 获取或者在这里定义
    // private final ResourceLocation glowTextureLocation;

    public UltramanLeoGlowLayer(GeoRenderer<T> renderer) {
        super(renderer);
        // this.glowTextureLocation = UltramanLeo.id("textures/armor/ultraman_leo_texture_glowmask.png"); // 或者动态获取
    }

    // 这个 render 方法在主模型渲染之后被调用
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer,
                       float partialTick, int packedLight, int packedOverlay) { // 签名保持不变

        // 1. 获取发光设置
        RenderType glowRenderType = RenderType.eyes(this.getGlowTextureResource(animatable));
        VertexConsumer glowBuffer = bufferSource.getBuffer(glowRenderType);
        int fullBrightLight = LightTexture.pack(15, 15);

        // 2. 调用渲染器的 actuallyRender 方法来绘制发光层
        //    注意参数顺序和类型！
        getRenderer().actuallyRender(
                poseStack,
                animatable,
                bakedModel,          // 使用传入的 bakedModel
                glowRenderType,      // 使用发光 RenderType
                bufferSource,        // 传入的 bufferSource
                glowBuffer,          // 使用发光 VertexConsumer
                false,               // isReRender 通常为 false 在这里
                partialTick,         // 传入的 partialTick
                fullBrightLight,     // 使用全亮光照
                OverlayTexture.NO_OVERLAY, // 发光通常无覆盖层
                Color.WHITE.argbInt() // 使用白色 (或 Color.ofRGBA(1, 1, 1, 1).argbInt())
        );

        // 这里不需要调用 super.render 或 defaultRender
        // 因为渲染层只负责添加额外的绘制调用
    }

    /**
     * 获取发光纹理的 ResourceLocation。
     * 覆盖这个方法来自定义发光纹理路径。
     * @param animatable 当前的 Animatable 对象
     * @return 发光纹理的 ResourceLocation
     */
    protected ResourceLocation getGlowTextureResource(T animatable) {
        // 尝试从主纹理自动推断 _glowmask 后缀

        // 或者直接返回固定路径：
        return UltramanLeo.id("textures/armor/ultraman_leo_texture_glowmask.png");
    }

    /**
     * 获取主纹理的 ResourceLocation。
     * @param animatable 当前的 Animatable 对象
     * @return 主纹理的 ResourceLocation
     */
    protected ResourceLocation getTextureResource(T animatable) {
        // 调用渲染器的方法获取主纹理
        return this.renderer.getTextureLocation(animatable);
    }
}
