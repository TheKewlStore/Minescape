package com.iandavis.minescape.api.utils;

import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemUtils {
    public static boolean setContainsItemStack(ItemStack itemStack, Set<ItemStack> itemStacks) {
        for (ItemStack compare: itemStacks) {
            if (itemStack.isItemEqualIgnoreDurability(compare)) {
                return true;
            }
        }

        return false;
    }
}
