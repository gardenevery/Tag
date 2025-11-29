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
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.item("minecraft:weapons").add(itemStack).add(itemStack);
     *          TagBuilder.item("minecraft:weapons").removeTag();
     *          TagBuilder.item("minecraft:weapons").removeKey(itemStack).removeKey(itemStack);
     */
    public static ItemInitialState item(@Nullable String tagName) {
        return new ItemTagBuilder(tagName).initialState();
    }

    /**
     * Create a fluid tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluid("forge:lava").add(fluidStack).add(fluidStack);
     *          TagBuilder.fluid("forge:lava").removeTag();
     *          TagBuilder.fluid("forge:lava").removeKey(fluidStack).removeKey(fluidStack);
     */
    public static FluidInitialState fluid(@Nullable String tagName) {
        return new FluidTagBuilder(tagName).initialState();
    }

    /**
     * Create a block tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.block("minecraft:logs").add(block).add(block);
     *          TagBuilder.block("minecraft:logs").removeTag();
     *          TagBuilder.block("minecraft:logs").removeKey(block).removeKey(block);
     */
    public static BlockInitialState block(@Nullable String tagName) {
        return new BlockTagBuilder(tagName).initialState();
    }

    /**
     * Create an item tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.item("minecraft:tools", "minecraft:diggers", "mod:iron_tier").add(itemStack).add(itemStack);
     *          TagBuilder.item("minecraft:tools", "minecraft:diggers", "mod:iron_tier").removeTag();
     *          TagBuilder.item("minecraft:tools", "minecraft:diggers", "mod:iron_tier").removeKey(itemStack).removeKey(itemStack);
     */
    public static MultiItemInitialState item(@Nullable String... tagNames) {
        return new MultiItemTagBuilder(tagNames).initialState();
    }

    /**
     * Create a fluid tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluid("forge:liquids", "mod:coolant", "pack:hazardous").add(fluidStack);
     *          TagBuilder.fluid("forge:liquids", "mod:coolant", "pack:hazardous").removeTag();
     *          TagBuilder.fluid("forge:liquids", "mod:coolant", "pack:hazardous").removeKey(fluidStack).removeKey(fluidStack);
     */
    public static MultiFluidInitialState fluid(@Nullable String... tagNames) {
        return new MultiFluidTagBuilder(tagNames).initialState();
    }

    /**
     * Create a block tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.block("forge:ores", "mod:world_gen", "pack:rare_blocks").add(block);
     *          TagBuilder.block("forge:ores", "mod:world_gen", "pack:rare_blocks").removeTag();
     *          TagBuilder.block("forge:ores", "mod:world_gen", "pack:rare_blocks").removeKey(block).removeKey(block);
     */
    public static MultiBlockInitialState block(@Nullable String... tagNames) {
        return new MultiBlockTagBuilder(tagNames).initialState();
    }
}
