package com.gardenevery.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

public final class TagHelper {

    private TagHelper() {
    }

    private static final TagManager MANAGER = TagManager.getInstance();

    /**
     * Get all tags of an item
     */
    public static Set<String> getTags(ItemStack stack) {
        var key = ItemKey.from(stack);
        return key != null ? MANAGER.itemTags.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags of a fluid
     */
    public static Set<String> getTags(FluidStack stack) {
        var key = FluidKey.from(stack);
        return key != null ? MANAGER.fluidTags.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags of a block
     */
    public static Set<String> getTags(IBlockState blockState) {
        return getTags(blockState.getBlock());
    }

    /**
     * Get all tags of a block
     */
    public static Set<String> getTags(Block block) {
        var key = BlockKey.from(block);
        return key != null ? MANAGER.blockTags.getTags(key) : Collections.emptySet();
    }

    /**
     * Check if an item has a specific tag
     */
    public static boolean hasTag(ItemStack stack, String tagName) {
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasTag(key, tagName);
    }

    /**
     * Check if a fluid has a specific tag
     */
    public static boolean hasTag(FluidStack stack, String tagName) {
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasTag(key, tagName);
    }

    /**
     * Check if a block has a specific tag
     */
    public static boolean hasTag(IBlockState blockState, String tagName) {
        return hasTag(blockState.getBlock(), tagName);
    }

    /**
     * Check if a block has a specific tag
     */
    public static boolean hasTag(Block block, String tagName) {
        var key = BlockKey.from(block);
        return key != null && MANAGER.blockTags.hasTag(key, tagName);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasTags(ItemStack stack, String... tagNames) {
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasTags(ItemStack stack, Collection<String> tagNames) {
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasTags(FluidStack stack, String... tagNames) {
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasTags(FluidStack stack, Collection<String> tagNames) {
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasTags(IBlockState blockState, String... tagNames) {
        var key = BlockKey.from(blockState.getBlock());
        return MANAGER.blockTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasTags(IBlockState blockState, Collection<String> tagNames) {
        var key = BlockKey.from(blockState.getBlock());
        return MANAGER.blockTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasTags(Block block, String... tagNames) {
        var key = BlockKey.from(block);
        return key != null && MANAGER.blockTags.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasTags(Block block, Collection<String> tagNames) {
        var key = BlockKey.from(block);
        return key != null && MANAGER.blockTags.hasAnyTag(key, tagNames);
    }

    /**
     * Get all items under a specific tag
     */
    public static Set<ItemStack> getItemStacks(String tagName) {
        var itemKeys = MANAGER.itemTags.getKeys(tagName);
        if (itemKeys.isEmpty()) {
            return Collections.emptySet();
        }

        Set<ItemStack> result = new HashSet<>();
        for (var key : itemKeys) {
            result.add(key.stack().copy());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all fluids under a specific tag
     */
    public static Set<FluidStack> getFluidStacks(String tagName) {
        var fluidKeys = MANAGER.fluidTags.getKeys(tagName);
        if (fluidKeys.isEmpty()) {
            return Collections.emptySet();
        }

        Set<FluidStack> result = new HashSet<>();
        for (var key : fluidKeys) {
            result.add(key.stack().copy());
        }
        return Collections.unmodifiableSet(result);
    }
}
