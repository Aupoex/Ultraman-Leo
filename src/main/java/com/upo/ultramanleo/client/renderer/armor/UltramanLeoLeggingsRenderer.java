package com.upo.ultramanleo.client.renderer.armor;

import com.upo.ultramanleo.client.model.armor.UltramanLeoLeggingsModel;
import com.upo.ultramanleo.item.armor.UltramanLeoLeggingsItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class UltramanLeoLeggingsRenderer extends GeoArmorRenderer<UltramanLeoLeggingsItem> {
    public UltramanLeoLeggingsRenderer() {
        super(new UltramanLeoLeggingsModel());
        //this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
