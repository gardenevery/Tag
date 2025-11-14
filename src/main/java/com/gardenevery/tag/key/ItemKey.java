package com.gardenevery.tag.key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Desugar
public record ItemKey(Item item, int metadata) implements Key {
    @Nullable
    public static ItemKey from(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        int metadata = stack.getHasSubtypes() ? stack.getMetadata() : 0;
        return new ItemKey(stack.getItem(), metadata);
    }

    @Nonnull
    public ItemStack stack() {
        return new ItemStack(item, 1, metadata);
    }
}
