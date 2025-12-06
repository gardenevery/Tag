package com.gardenevery.tag;

import javax.annotation.Nullable;

import com.gardenevery.tag.AbstractTagBuilder.BlockInitialState;
import com.gardenevery.tag.AbstractTagBuilder.BlockTagBuilder;
import com.gardenevery.tag.AbstractTagBuilder.FluidInitialState;
import com.gardenevery.tag.AbstractTagBuilder.FluidTagBuilder;
import com.gardenevery.tag.AbstractTagBuilder.ItemInitialState;
import com.gardenevery.tag.AbstractTagBuilder.ItemTagBuilder;
import com.gardenevery.tag.AbstractTagBuilder.MultiBlockInitialState;
import com.gardenevery.tag.AbstractTagBuilder.MultiBlockTagBuilder;
import com.gardenevery.tag.AbstractTagBuilder.MultiFluidInitialState;
import com.gardenevery.tag.AbstractTagBuilder.MultiFluidTagBuilder;
import com.gardenevery.tag.AbstractTagBuilder.MultiItemTagBuilder;
import com.gardenevery.tag.AbstractTagBuilder.MultiItemInitialState;

public final class TagBuilder {

    private TagBuilder() {}

    /**
     * Create an item tag builder
     * <p> Example:TagBuilder.item("minecraft:weapon").add(itemStack).add(itemStack);
     * <p> TagBuilder.item("minecraft:weapon").removeTag();
     * <p> TagBuilder.item("minecraft:weapon").removeKey(itemStack).removeKey(itemStack);
     *
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     */
    public static ItemInitialState item(@Nullable String tagName) {
        return new ItemTagBuilder(tagName).initialState();
    }

    /**
     * Create a fluid tag builder
     * <p> Example: TagBuilder.fluid("forge:lava").add(fluidStack).add(fluidStack);
     * <p> TagBuilder.fluid("forge:lava").removeTag();
     * <p> TagBuilder.fluid("forge:lava").removeKey(fluidStack).removeKey(fluidStack);
     *
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     */
    public static FluidInitialState fluid(@Nullable String tagName) {
        return new FluidTagBuilder(tagName).initialState();
    }

    /**
     * Create a block tag builder
     * <p> Example: TagBuilder.block("minecraft:log").add(block).add(block);
     * <p> TagBuilder.block("minecraft:log").removeTag();
     * <p> TagBuilder.block("minecraft:log").removeKey(block).removeKey(block);
     *
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     */
    public static BlockInitialState block(@Nullable String tagName) {
        return new BlockTagBuilder(tagName).initialState();
    }

    /**
     * Create an item tag builder with multiple tags
     * <p> Example: TagBuilder.item("minecraft:tool", "minecraft:diggers", "mod:iron_tier").add(itemStack).add(itemStack);
     * <p> TagBuilder.item("minecraft:tool", "minecraft:diggers", "mod:iron_tier").removeTag();
     * <p> TagBuilder.item("minecraft:tool", "minecraft:diggers", "mod:iron_tier").removeKey(itemStack).removeKey(itemStack);
     *
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     */
    public static MultiItemInitialState item(@Nullable String... tagNames) {
        return new MultiItemTagBuilder(tagNames).initialState();
    }

    /**
     * Create a fluid tag builder with multiple tags
     * <p> Example: TagBuilder.fluid("forge:liquid", "mod:coolant", "pack:hazardous").add(fluidStack);
     * <p> TagBuilder.fluid("forge:liquid", "mod:coolant", "pack:hazardous").removeTag();
     * <p> TagBuilder.fluid("forge:liquid", "mod:coolant", "pack:hazardous").removeKey(fluidStack).removeKey(fluidStack);
     *
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     */
    public static MultiFluidInitialState fluid(@Nullable String... tagNames) {
        return new MultiFluidTagBuilder(tagNames).initialState();
    }

    /**
     * Create a block tag builder with multiple tags
     * <p> Example: TagBuilder.block("forge:ore", "pack:rare_blocks").add(block);
     * <p> TagBuilder.block("forge:ore", "pack:rare_blocks").removeTag();
     * <p> TagBuilder.block("forge:ore", "pack:rare_blocks").removeKey(block).removeKey(block);
     *
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     */
    public static MultiBlockInitialState block(@Nullable String... tagNames) {
        return new MultiBlockTagBuilder(tagNames).initialState();
    }
}
