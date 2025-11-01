package com.gardenevery.tag;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

public final class TagHelper {

    private TagHelper() {
    }

    /**
     * Get all tags associated with an item
     */
    public static Set<String> tags(ItemStack stack) {
        var key = ItemKey.from(stack);
        return key != null ? TagManager.ITEM_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a fluid
     */
    public static Set<String> tags(FluidStack stack) {
        var key = FluidKey.from(stack);
        return key != null ? TagManager.FLUID_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block
     */
    public static Set<String> tags(Block block) {
        var key = BlockKey.from(block);
        return TagManager.BLOCK_TAGS.getTags(key);
    }

    /**
     * Get all tags associated with a block state
     */
    public static Set<String> tags(IBlockState blockState) {
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.getTags(key);
    }

    /**
     * Check if an item has the specified tag
     */
    public static boolean hasTag(ItemStack stack, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a fluid has the specified tag
     */
    public static boolean hasTag(FluidStack stack, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a block has the specified tag
     */
    public static boolean hasTag(Block block, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = BlockKey.from(block);
        return TagManager.BLOCK_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a block state has the specified tag
     */
    public static boolean hasTag(IBlockState blockState, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.hasTag(key, tag);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(ItemStack stack, String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(ItemStack stack, Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(FluidStack stack, String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(FluidStack stack, Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(Block block, String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = BlockKey.from(block);
        return TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(Block block, Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = BlockKey.from(block);
        return TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(IBlockState blockState, String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(IBlockState blockState, Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Get all items that have the specified tag
     */
    public static Set<ItemStack> getItemStacks(String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }
        Set<ItemKey> keys = TagManager.ITEM_TAGS.getKeys(tagName);
        Set<ItemStack> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.stack().copy());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all fluids that have the specified tag
     */
    public static Set<FluidStack> getFluidStacks(String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }
        Set<FluidKey> keys = TagManager.FLUID_TAGS.getKeys(tagName);
        Set<FluidStack> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.stack().copy());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all blocks that have the specified tag
     */
    public static Set<Block> getBlocks(String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }
        Set<BlockKey> keys = TagManager.BLOCK_TAGS.getKeys(tagName);
        Set<Block> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.block());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all registered tag names for the specified type
     */
    public static Set<String> getAllTags(TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM_TAGS.getAllTag();
            case FLUID -> TagManager.FLUID_TAGS.getAllTag();
            case BLOCK -> TagManager.BLOCK_TAGS.getAllTag();
        };
    }

    /**
     * Get all registered tag names grouped by type
     */
    public static Map<String, Set<String>> getAllTags() {
        return Collections.unmodifiableMap(new HashMap<>() {{
            put("item", new ObjectOpenHashSet<>(TagManager.ITEM_TAGS.getAllTag()));
            put("fluid", new ObjectOpenHashSet<>(TagManager.FLUID_TAGS.getAllTag()));
            put("block", new ObjectOpenHashSet<>(TagManager.BLOCK_TAGS.getAllTag()));
        }});
    }

    /**
     * Check if a tag exists for the specified type
     */
    public static boolean tagNameExist(String tag, TagType type) {
        if (isTagInvalid(tag)) {
            return false;
        }
        return switch (type) {
            case ITEM -> TagManager.ITEM_TAGS.doesTagNameExist(tag);
            case FLUID -> TagManager.FLUID_TAGS.doesTagNameExist(tag);
            case BLOCK -> TagManager.BLOCK_TAGS.doesTagNameExist(tag);
        };
    }

    /**
     * Check if a tag exists in any type
     */
    public static boolean tagNameExist(String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        return (TagManager.ITEM_TAGS.doesTagNameExist(tag) ||
                TagManager.FLUID_TAGS.doesTagNameExist(tag) ||
                TagManager.BLOCK_TAGS.doesTagNameExist(tag)
        );
    }

    /**
     * Get the total number of tags for the specified type
     */
    public static int getTagCount(TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM_TAGS.getTagCount();
            case FLUID -> TagManager.FLUID_TAGS.getTagCount();
            case BLOCK -> TagManager.BLOCK_TAGS.getTagCount();
        };
    }

    /**
     * Get the total number of tags across all types
     */
    public static int getTagCount() {
        return TagManager.ITEM_TAGS.getTagCount() +
                TagManager.FLUID_TAGS.getTagCount() +
                TagManager.BLOCK_TAGS.getTagCount();
    }

    private static boolean isTagInvalid(String tag) {
        return tag == null || tag.isEmpty();
    }

    private static boolean areTagsInvalid(Collection<String> tags) {
        return tags == null || tags.isEmpty();
    }

    private static boolean areTagsInvalid(String[] tags) {
        return tags == null || tags.length == 0;
    }
}
