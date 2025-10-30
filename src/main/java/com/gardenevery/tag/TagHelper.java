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

    public enum TagType {
        ITEM,
        FLUID,
        BLOCK,
        BLOCK_STATE
    }

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
    public static boolean hasTag(ItemStack stack, String tag) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasTag(key, tag);
    }

    /**
     * Check if a fluid has a specific tag
     */
    public static boolean hasTag(FluidStack stack, String tag) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasTag(key, tag);
    }

    /**
     * Check if a block has a specific tag
     */
    public static boolean hasTag(Block block, String tag) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        var key = BlockKey.from(block);
        return MANAGER.blockTags.hasTag(key, tag);
    }

    /**
     * Check if a IBlockState has a specific tag
     */
    public static boolean hasTag(IBlockState blockState, String tag) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.hasTag(key, tag);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(ItemStack stack, String... tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasAnyTag(key, tags);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(ItemStack stack, Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && MANAGER.itemTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(FluidStack stack, String... tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(FluidStack stack, Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && MANAGER.fluidTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(Block block, String... tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }
        var key = BlockKey.from(block);
        return MANAGER.blockTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(Block block, Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        var key = BlockKey.from(block);
        return MANAGER.blockTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a IBlockState has any of the specified tags
     */
    public static boolean hasAnyTags(IBlockState blockState, String... tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.hasAnyTag(key, tags);
    }

    /**
     * Check if a IBlockState has any of the specified tags
     */
    public static boolean hasAnyTags(IBlockState blockState, Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        var key = BlockStateKey.from(blockState);
        return MANAGER.blockStateTags.hasAnyTag(key, tags);
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

    /**
     * Get all registered tag names for corresponding type
     */
    public static Set<String> getAllTags(TagType type) {
        return switch (type) {
            case ITEM -> MANAGER.itemTags.getAllTagNames();
            case FLUID -> MANAGER.fluidTags.getAllTagNames();
            case BLOCK -> MANAGER.blockTags.getAllTagNames();
            case BLOCK_STATE -> MANAGER.blockStateTags.getAllTagNames();
        };
    }

    /**
     * Check if the corresponding type of label exists
     */
    public static boolean tagNameExist(String tag, TagType type) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        return switch (type) {
            case ITEM -> MANAGER.itemTags.doesTagNameExist(tag);
            case FLUID -> MANAGER.fluidTags.doesTagNameExist(tag);
            case BLOCK -> MANAGER.blockTags.doesTagNameExist(tag);
            case BLOCK_STATE -> MANAGER.blockStateTags.doesTagNameExist(tag);
        };
    }

    /**
     * Check if a tag exists
     */
    public static boolean tagNameExist(String tag) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        return (MANAGER.itemTags.doesTagNameExist(tag) ||
                MANAGER.fluidTags.doesTagNameExist(tag) ||
                MANAGER.blockTags.doesTagNameExist(tag) ||
                MANAGER.blockStateTags.doesTagNameExist(tag)
        );
    }
}
