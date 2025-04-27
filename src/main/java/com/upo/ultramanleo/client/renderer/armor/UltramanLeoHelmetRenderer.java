package com.upo.ultramanleo.client.renderer.armor;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.client.model.armor.UltramanLeoHelmetModel;
import com.upo.ultramanleo.item.armor.UltramanLeoHelmetItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class UltramanLeoHelmetRenderer extends GeoArmorRenderer<UltramanLeoHelmetItem> {

    private static final ResourceLocation GLOW_TEXTURE = UltramanLeo.id("textures/armor/ultraman_leo_texture_glowmask.png");
    public UltramanLeoHelmetRenderer() {
        super(new UltramanLeoHelmetModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}

