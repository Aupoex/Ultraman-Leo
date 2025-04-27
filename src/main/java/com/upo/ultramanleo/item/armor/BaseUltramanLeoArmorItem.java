package com.upo.ultramanleo.item.armor;

import com.upo.ultramanleo.client.renderer.armor.UltramanLeoChestplateRenderer;
import com.upo.ultramanleo.client.renderer.armor.UltramanLeoHelmetRenderer;
import com.upo.ultramanleo.client.renderer.armor.UltramanLeoLeggingsRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public abstract class BaseUltramanLeoArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseUltramanLeoArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.CONTINUE));
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
     }

    @Override
    public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;
            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
                                                                                  @Nullable T livingEntity,
                                                                                  ItemStack itemStack,
                                                                                  @Nullable EquipmentSlot equipmentSlot,
                                                                                  @Nullable HumanoidModel<T> original
            ) {
                if (this.renderer == null) {
                    Item currentItem = itemStack.getItem();
                    if (currentItem instanceof UltramanLeoHelmetItem) {
                        this.renderer = new UltramanLeoHelmetRenderer();
                    } else if (currentItem instanceof UltramanLeoChestplateItem) {
                        this.renderer = new UltramanLeoChestplateRenderer();
                    } else if (currentItem instanceof UltramanLeoLeggingsItem) {
                        this.renderer = new UltramanLeoLeggingsRenderer();
                    } else {
                        throw new IllegalStateException("Could not determine armor item type for renderer: " + currentItem);
                    }
                }

                return this.renderer;
            }

        });
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.literal("奥特曼伫立于此").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF5555))));
        tooltipComponents.add(Component.literal("变身状态下无法取下").withStyle(Style.EMPTY.withItalic(true).withColor(TextColor.fromRgb(0xAAAAAA))));
    }
}