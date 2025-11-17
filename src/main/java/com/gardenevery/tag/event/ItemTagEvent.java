package com.gardenevery.tag.event;

import net.minecraft.item.ItemStack;

import com.gardenevery.tag.TagType;

public class ItemTagEvent extends TagEvent {
    private final ItemStack itemStack;

    private ItemTagEvent(String tagName, ItemStack itemStack) {
        super(tagName, TagType.ITEM);
        this.itemStack = itemStack != null ? itemStack.copy() : ItemStack.EMPTY;
    }

    public ItemStack getItemStack() {
        return itemStack.copy();
    }

    @Override
    public boolean isItemEvent() {
        return true;
    }

    @Override
    public boolean isFluidEvent() {
        return false;
    }

    @Override
    public boolean isBlockEvent() {
        return false;
    }

    /**
     * Do not use this
     */
    @Deprecated
    public static ItemTagEvent create(String tagName, ItemStack itemStack) {
        return new ItemTagEvent(tagName, itemStack);
    }
}
