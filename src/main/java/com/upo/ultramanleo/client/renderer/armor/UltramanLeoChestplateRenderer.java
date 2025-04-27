package com.upo.ultramanleo.client.renderer.armor;

import com.upo.ultramanleo.client.model.armor.UltramanLeoChestplateModel;
import com.upo.ultramanleo.item.armor.UltramanLeoChestplateItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class UltramanLeoChestplateRenderer extends GeoArmorRenderer<UltramanLeoChestplateItem> {

    public UltramanLeoChestplateRenderer() {
        super(new UltramanLeoChestplateModel());
        //this.addRenderLayer(new UltramanLeoGlowLayer<>(this));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
