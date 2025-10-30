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
import com.gardenevery.tag.key.BlockStateKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

public final class TagHelper {

    private TagHelper() {
    }

    private static final TagManager MANAGER = TagManager.instance();

    /**
     * Get all tags of an item
     */
    public static Set<String> tags(ItemStack stack) {
        var key = ItemKey.from(stack);
        return key != null ? MANAGER.itemTags.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags of a fluid
     */
    public static Set<String> tags(FluidStack stack) {
        var key = FluidKey.from(stack);
        return key != null ? MANAGER.fluidTags.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags of a block
     */
    public static Set<String> tags(Block block) {
        var key = BlockKey.from(block);
        return MANAGER.blockTags.getTags(key);
    }

    /**
     * Get all tags of a IBlockState
     */
    public static Set<String> tags(IBlockState blockState) {
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.getTags(key);
    }

    /**
     * Check if an item has a specific tag
     */
    public static boolean has(ItemStack stack, String tag) {
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasTag(key, tag);
    }

    /**
     * Check if a fluid has a specific tag
     */
    public static boolean has(FluidStack stack, String tag) {
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasTag(key, tag);
    }

    /**
     * Check if a block has a specific tag
     */
    public static boolean has(Block block, String tag) {
        var key = BlockKey.from(block);
        return MANAGER.blockTags.hasTag(key, tag);
    }

    /**
     * Check if a IBlockState has a specific tag
     */
    public static boolean has(IBlockState blockState, String tag) {
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.hasTag(key, tag);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAny(ItemStack stack, String... tags) {
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasAnyTag(key, tags);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAny(ItemStack stack, Collection<String> tags) {
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAny(FluidStack stack, String... tags) {
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAny(FluidStack stack, Collection<String> tags) {
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAny(Block block, String... tags) {
        var key = BlockKey.from(block);
        return MANAGER.blockTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAny(Block block, Collection<String> tags) {
        var key = BlockKey.from(block);
        return MANAGER.blockTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a IBlockState has any of the specified tags
     */
    public static boolean hasAny(IBlockState blockState, String... tags) {
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a IBlockState has any of the specified tags
     */
    public static boolean hasAny(IBlockState blockState, Collection<String> tags) {
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.hasAnyTag(key, tags);
    }

    /**
     * Get all items under a specific tag
     */
    public static Set<ItemStack> itemStacks(String tag) {
        var itemKeys = MANAGER.itemTags.getKeys(tag);
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
    public static Set<FluidStack> fluidStacks(String tag) {
        var fluidKeys = MANAGER.fluidTags.getKeys(tag);
        if (fluidKeys.isEmpty()) {
            return Collections.emptySet();
        }

        Set<FluidStack> result = new HashSet<>();
        for (var key : fluidKeys) {
            result.add(key.stack().copy());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all registered tag names for items
     */
    public static Set<String> itemTags() {
        return MANAGER.itemTags.getAllTagNames();
    }

    /**
     * Get all registered tag names for fluids
     */
    public static Set<String> fluidTags() {
        return MANAGER.fluidTags.getAllTagNames();
    }

    /**
     * Get all registered tag names for blocks
     */
    public static Set<String> blockTags() {
        return MANAGER.blockTags.getAllTagNames();
    }

    /**
     * Get all registered tag names for block states
     */
    public static Set<String> blockStateTags() {
        return MANAGER.blockStateTags.getAllTagNames();
    }

    /**
     * Check if an item tag exists
     */
    public static boolean hasItemTag(String tag) {
        return isValid(tag) && MANAGER.itemTags.doesTagNameExist(tag);
    }

    /**
     * Check if a fluid tag exists
     */
    public static boolean hasFluidTag(String tag) {
        return isValid(tag) && MANAGER.fluidTags.doesTagNameExist(tag);
    }

    /**
     * Check if a block tag exists
     */
    public static boolean hasBlockTag(String tag) {
        return isValid(tag) && MANAGER.blockTags.doesTagNameExist(tag);
    }

    /**
     * Check if a blockState tag exists
     */
    public static boolean hasBlockStateTag(String tag) {
        return isValid(tag) && MANAGER.blockStateTags.doesTagNameExist(tag);
    }

    /**
     * Check if a tag exists
     */
    public static boolean exists(String tag) {
        return isValid(tag) && (
                MANAGER.itemTags.doesTagNameExist(tag) ||
                        MANAGER.fluidTags.doesTagNameExist(tag) ||
                        MANAGER.blockTags.doesTagNameExist(tag) ||
                        MANAGER.blockStateTags.doesTagNameExist(tag)
        );
    }

    private static boolean isValid(String tag) {
        return tag != null && !tag.isEmpty();
    }
}
