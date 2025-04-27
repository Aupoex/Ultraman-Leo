package com.upo.ultramanleo.event;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.skill.SkillZHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import com.upo.ultramanleo.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import com.upo.ultramanleo.attachments.ModAttachments;
import com.upo.ultramanleo.item.TransformationDeviceItem;
import java.util.Map;

@EventBusSubscriber(modid = UltramanLeo.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeBusEvents {

    public static void startSkillZMovement(ServerPlayer player, ITransformationState state) {
        if (!state.isUsingSkillZ() && state.getSkillZCooldownTicks() <= 0) {
            state.setUsingSkillZ(true);
            state.setSkillZProgress(1);
            player.push(0, 0.7, 0);
            player.hurtMarked = true;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        ITransformationState state = null;
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {

            state = serverPlayer.getData(ModAttachments.TRANSFORMATION_STATE.get());

            boolean wasUsingSkillZ = state.isUsingSkillZ();

            if (wasUsingSkillZ) {
                SkillZHandler.handleTick(serverPlayer, state);
            }

            if (state.isTransformed()) {
                if (state.getTransformTicks() <= 0) {
                    // 变身时间耗尽，结束变身
                    endTransformation(serverPlayer, state);
                } else {
                    if (!state.isUsingSkillZ()) {
                        applyTransformationEffects(serverPlayer);
                        applyAttributeModifiers(serverPlayer);

                    }
                }
            } else {
                removeTransformationEffects(serverPlayer);
                removeAttributeModifiers(serverPlayer);
                if (isWearingFullSet(serverPlayer)) {
                    player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                    player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                    player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                }
            }
            state.tick();

        }
        if (state != null) {
            state.tick();
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ITransformationState state = serverPlayer.getData(ModAttachments.TRANSFORMATION_STATE);
            endTransformation(serverPlayer, state);
        }
    }

    public static void endTransformation(ServerPlayer player, ITransformationState state) {
        player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
        // player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);

        Map<EquipmentSlot, ItemStack> restoredArmor = TransformationDeviceItem.oldArmorCache.remove(player.getUUID());
        if (restoredArmor != null) {
            for (Map.Entry<EquipmentSlot, ItemStack> entry : restoredArmor.entrySet()) {
                if (player.getItemBySlot(entry.getKey()).isEmpty()) {
                    player.setItemSlot(entry.getKey(), entry.getValue());
                } else {
                    if (!player.addItem(entry.getValue())) {
                        player.drop(entry.getValue(), false);
                    }
                }
            }
        }
        state.endTransform();
        player.sendSystemMessage(Component.literal("变身时间结束！"));
    }

    private static boolean isWearingFullSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.ULTRAMAN_HELMET.get() &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.ULTRAMAN_CHESTPLATE.get() &&
                player.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.ULTRAMAN_LEGGINGS.get();
    }

    private static void applyTransformationEffects(ServerPlayer player) {
        if (!player.hasEffect(MobEffects.WATER_BREATHING)) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 220, 0, true, true));
        }
        if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220, 0, true, true));
        }
        if (!player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 220, 2, true, true));
        }
         if (!player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
             player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 1, true, true));
         }
        if (!player.hasEffect(MobEffects.JUMP)) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 220, 1, true, true));
        }
        if (!player.hasEffect(MobEffects.REGENERATION)) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 220, 0, true, true));
        }
    }

    private static void removeTransformationEffects(ServerPlayer player) {
        if (player.hasEffect(MobEffects.NIGHT_VISION)) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
        if (player.hasEffect(MobEffects.WATER_BREATHING)) {
            player.removeEffect(MobEffects.WATER_BREATHING);
        }
        if (player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if (player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        }
        if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
        }
        if (player.hasEffect(MobEffects.JUMP)) {
            player.removeEffect(MobEffects.JUMP);
        }if (player.hasEffect(MobEffects.REGENERATION)) {
            player.removeEffect(MobEffects.REGENERATION);
        }
    }

    private static void applyAttributeModifiers(ServerPlayer player) {
        AttributeInstance healthInstance = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthInstance != null && healthInstance.getModifier(UltramanLeo.ULTRAMAN_MAX_HEALTH_MODIFIER_ID) == null) {
            AttributeModifier healthModifier = new AttributeModifier(
                    UltramanLeo.ULTRAMAN_MAX_HEALTH_MODIFIER_ID,
                    40.0,
                    AttributeModifier.Operation.ADD_VALUE
            );
            healthInstance.addPermanentModifier(healthModifier);
            // healthInstance.addModifier(UltramanLeo.ULTRAMAN_MAX_HEALTH_MODIFIER_ID, 20.0, AttributeModifier.Operation.ADD_VALUE);
            player.heal(40.0f);
        }

        AttributeInstance armorInstance = player.getAttribute(Attributes.ARMOR);
        if (armorInstance != null && armorInstance.getModifier(UltramanLeo.ULTRAMAN_ARMOR_MODIFIER_ID) == null) {
            AttributeModifier armorModifier = new AttributeModifier(
                    UltramanLeo.ULTRAMAN_ARMOR_MODIFIER_ID,
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE
            );
            armorInstance.addPermanentModifier(armorModifier);
        }

        AttributeInstance toughnessInstance = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughnessInstance != null && toughnessInstance.getModifier(UltramanLeo.ULTRAMAN_ARMOR_TOUGHNESS_MODIFIER_ID) == null) {
            AttributeModifier toughnessModifier = new AttributeModifier(
                    UltramanLeo.ULTRAMAN_ARMOR_TOUGHNESS_MODIFIER_ID,
                    5.0,
                    AttributeModifier.Operation.ADD_VALUE
            );
            toughnessInstance.addPermanentModifier(toughnessModifier);
        }
    }

    private static void removeAttributeModifiers(ServerPlayer player) {
        AttributeInstance healthInstance = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthInstance != null) {
            healthInstance.removeModifier(UltramanLeo.ULTRAMAN_MAX_HEALTH_MODIFIER_ID);
            if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
        }

        AttributeInstance armorInstance = player.getAttribute(Attributes.ARMOR);
        if (armorInstance != null) {
            armorInstance.removeModifier(UltramanLeo.ULTRAMAN_ARMOR_MODIFIER_ID);
        }

        AttributeInstance toughnessInstance = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughnessInstance != null) {
            toughnessInstance.removeModifier(UltramanLeo.ULTRAMAN_ARMOR_TOUGHNESS_MODIFIER_ID);
        }
    }
}
