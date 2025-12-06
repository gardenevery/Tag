package com.gardenevery.tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Desugar
record ItemKey(@Nonnull Item item, int metadata) {
    @Nullable
    public static ItemKey toKey(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        int metadata = stack.getHasSubtypes() ? stack.getMetadata() : 0;
        return new ItemKey(stack.getItem(), metadata);
    }

    @Nonnull
    public ItemStack toElement() {
        return new ItemStack(item, 1, metadata);
    }
}
