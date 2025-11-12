package com.gardenevery.tag;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class TagEvent extends Event {

    private final String tagName;
    private final TagType type;
    private ItemStack itemStack;
    private FluidStack fluidStack;
    private Block block;

    private TagEvent(String tagName, TagType type) {
        this.tagName = tagName;
        this.type = type;
    }

    static TagEvent itemEvent(String tagName, ItemStack itemStack) {
        var event = new TagEvent(tagName, TagType.ITEM);
        event.itemStack = itemStack.copy();
        return event;
    }

    static TagEvent fluidEvent(String tagName, FluidStack fluidStack) {
        var event = new TagEvent(tagName, TagType.FLUID);
        event.fluidStack = fluidStack.copy();
        return event;
    }

    static TagEvent blockEvent(String tagName, Block block) {
        var event = new TagEvent(tagName, TagType.BLOCK);
        event.block = block;
        return event;
    }

    public String getTagName() {
        return tagName;
    }

    public TagType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        return type == TagType.ITEM ? itemStack : null;
    }

    public FluidStack getFluidStack() {
        return type == TagType.FLUID ? fluidStack : null;
    }

    public Block getBlock() {
        return type == TagType.BLOCK ? block : null;
    }
}
