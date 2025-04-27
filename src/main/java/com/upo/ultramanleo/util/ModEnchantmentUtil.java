package com.upo.ultramanleo.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.component.CustomData;
import java.util.Optional;
import net.minecraft.world.item.enchantment.ItemEnchantments.Mutable;

public class ModEnchantmentUtil {

    public static ItemStack applyHiddenBindingCurse(ItemStack stack, HolderLookup.Provider registries) {
        if (stack.isEmpty()) {
            return stack;
        }

        Optional<Holder.Reference<Enchantment>> bindingCurseHolderOpt = registries.lookupOrThrow(Registries.ENCHANTMENT)
                .get(Enchantments.BINDING_CURSE);

        ItemEnchantments bindingCurseEnchants;
        if (bindingCurseHolderOpt.isPresent()) {
            Holder<Enchantment> bindingCurseHolder = bindingCurseHolderOpt.get();
            Mutable mutableEnchants = new Mutable(ItemEnchantments.EMPTY);
            mutableEnchants.set(bindingCurseHolder, 1);
            bindingCurseEnchants = mutableEnchants.toImmutable();
        } else {
            return stack;
        }
        int hideFlagsValue = 1;
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        nbt.putInt("HideFlags", hideFlagsValue);
        CustomData newCustomData = CustomData.of(nbt);

        DataComponentPatch finalPatch = DataComponentPatch.builder()
                .set(DataComponents.ENCHANTMENTS, bindingCurseEnchants)
                .set(DataComponents.CUSTOM_DATA, newCustomData)
                .build();
        stack.applyComponents(finalPatch);

        return stack;
    }
}

