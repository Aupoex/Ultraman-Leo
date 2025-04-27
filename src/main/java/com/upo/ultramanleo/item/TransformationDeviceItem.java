package com.upo.ultramanleo.item;

import com.upo.ultramanleo.attachments.ModAttachments;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.core.registries.Registries;

public class TransformationDeviceItem extends Item {

    public static final int TRANSFORM_DURATION_TICKS = 5 * 60 * 20;
    public static final int COOLDOWN_DURATION_TICKS = 30 * 20;

    public static final Map<UUID, Map<EquipmentSlot, ItemStack>> oldArmorCache = new ConcurrentHashMap<>();

    public TransformationDeviceItem(Properties properties) {
        super(properties);
    }
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent mutableName = super.getName(stack).copy();
        mutableName.withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
        return mutableName;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            ITransformationState state = player.getData(ModAttachments.TRANSFORMATION_STATE.get());

            if (state.getCooldownTicks() > 0&& !state.isTransformed()) {
                player.sendSystemMessage(Component.literal("变身器冷却中..."));
                return InteractionResultHolder.fail(player.getItemInHand(hand));
            }
            if (!state.isTransformed()) {
                Map<EquipmentSlot, ItemStack> oldArmor = new EnumMap<>(EquipmentSlot.class);
                for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                    if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                        oldArmor.put(slot, player.getItemBySlot(slot).copy());
                        player.setItemSlot(slot, ItemStack.EMPTY);
                    }
                }
                oldArmorCache.put(player.getUUID(), oldArmor);
                ItemStack helmetStack = new ItemStack(ModItems.ULTRAMAN_HELMET.get());
                ItemStack chestStack = new ItemStack(ModItems.ULTRAMAN_CHESTPLATE.get());
                ItemStack legsStack = new ItemStack(ModItems.ULTRAMAN_LEGGINGS.get());

                Holder<Enchantment> bindingCurseHolder = null;
                HolderLookup.Provider registries = level.registryAccess();
                Optional<Holder.Reference<Enchantment>> bindingCurseHolderOpt = registries.lookupOrThrow(Registries.ENCHANTMENT)
                        .get(Enchantments.BINDING_CURSE);
                ItemEnchantments bindingCurseEnchants = ItemEnchantments.EMPTY;
                if (bindingCurseHolderOpt.isPresent()) {
                    bindingCurseHolder = bindingCurseHolderOpt.get();
                    ItemEnchantments.Mutable mutableEnchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                    mutableEnchants.set(bindingCurseHolder, 1);
                    bindingCurseEnchants = mutableEnchants.toImmutable();
                }
                int hideFlagsValue = 1;
                CompoundTag helmetNbt = helmetStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                CompoundTag chestNbt = chestStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                CompoundTag legsNbt = legsStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                helmetNbt.putInt("HideFlags", hideFlagsValue);
                chestNbt.putInt("HideFlags", hideFlagsValue);
                legsNbt.putInt("HideFlags", hideFlagsValue);
                CustomData newHelmetData = CustomData.of(helmetNbt);
                CustomData newChestData = CustomData.of(chestNbt);
                CustomData newLegsData = CustomData.of(legsNbt);



                DataComponentPatch finalHelmetPatch = DataComponentPatch.builder()
                        .set(DataComponents.ENCHANTMENTS, bindingCurseEnchants)

                        .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                        .build();
                DataComponentPatch finalChestPatch = DataComponentPatch.builder()
                        .set(DataComponents.ENCHANTMENTS, bindingCurseEnchants)
                        //.set(DataComponents.CUSTOM_DATA, newChestData)
                        .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                        .build();
                DataComponentPatch finalLegsPatch = DataComponentPatch.builder()
                        .set(DataComponents.ENCHANTMENTS, bindingCurseEnchants)
                        //.set(DataComponents.CUSTOM_DATA, newLegsData)
                        .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                        .build();
                helmetStack.applyComponents(finalHelmetPatch);
                chestStack.applyComponents(finalChestPatch);
                legsStack.applyComponents(finalLegsPatch);
                player.setItemSlot(EquipmentSlot.HEAD, helmetStack);
                player.setItemSlot(EquipmentSlot.CHEST, chestStack);
                player.setItemSlot(EquipmentSlot.LEGS, legsStack);
                player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                state.startTransform(TRANSFORM_DURATION_TICKS);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.TRANSFORM_SOUND_EVENT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                player.sendSystemMessage(Component.literal("变身！"));

                return InteractionResultHolder.success(player.getItemInHand(hand));
            } else {
                player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                Map<EquipmentSlot, ItemStack> restoredArmor = oldArmorCache.remove(player.getUUID());
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
                player.sendSystemMessage(Component.literal("你主动解除了变身！"));
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}

