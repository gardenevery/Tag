package com.gardenevery.tag;

import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    @Nonnull
    public static Set<String> tags(@Nullable ItemStack stack) {
        var key = ItemKey.from(stack);
        return key != null ? TagManager.ITEM_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a fluid
     */
    @Nonnull
    public static Set<String> tags(@Nullable FluidStack stack) {
        var key = FluidKey.from(stack);
        return key != null ? TagManager.FLUID_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block
     */
    @Nonnull
    public static Set<String> tags(@Nullable Block block) {
        var key = BlockKey.from(block);
        return key != null ? TagManager.BLOCK_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block state
     */
    @Nonnull
    public static Set<String> tags(@Nullable IBlockState blockState) {
        if (blockState == null) {
            return Collections.emptySet();
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.getTags(key);
    }

    /**
     * Check if an item has the specified tag
     */
    public static boolean hasTag(@Nullable ItemStack stack, @Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a fluid has the specified tag
     */
    public static boolean hasTag(@Nullable FluidStack stack, @Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a block has the specified tag
     */
    public static boolean hasTag(@Nullable Block block, @Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = BlockKey.from(block);
        return key != null && TagManager.BLOCK_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a block state has the specified tag
     */
    public static boolean hasTag(@Nullable IBlockState blockState, @Nullable String tag) {
        if (isTagInvalid(tag) || blockState == null) {
            return false;
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.hasTag(key, tag);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable ItemStack stack, @Nullable String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable ItemStack stack, @Nullable Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable FluidStack stack, @Nullable String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable FluidStack stack, @Nullable Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable Block block, @Nullable String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = BlockKey.from(block);
        return key != null && TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable Block block, @Nullable Collection<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = BlockKey.from(block);
        return key != null && TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable IBlockState blockState, @Nullable String... tags) {
        if (areTagsInvalid(tags) || blockState == null) {
            return false;
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable IBlockState blockState, @Nullable Collection<String> tags) {
        if (areTagsInvalid(tags) || blockState == null) {
            return false;
        }
        var key = BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK_TAGS.hasAnyTag(key, tags);
    }

    /**
     * Get all items that have the specified tag
     */
    @Nonnull
    public static Set<ItemStack> getItemStacks(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }
        Set<ItemKey> keys = TagManager.ITEM_TAGS.getKeys(tagName);
        Set<ItemStack> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.stack());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all fluids that have the specified tag
     */
    @Nonnull
    public static Set<FluidStack> getFluidStacks(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }
        Set<FluidKey> keys = TagManager.FLUID_TAGS.getKeys(tagName);
        Set<FluidStack> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.stack());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all blocks that have the specified tag
     */
    @Nonnull
    public static Set<Block> getBlocks(@Nullable String tagName) {
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
    @Nonnull
    public static Set<String> getAllTags(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM_TAGS.getAllTags();
            case FLUID -> TagManager.FLUID_TAGS.getAllTags();
            case BLOCK -> TagManager.BLOCK_TAGS.getAllTags();
        };
    }

    /**
     * Get all registered tag names grouped by type
     */
    public static Map<TagType, Set<String>> getAllTags() {
        Map<TagType, Set<String>> map = new HashMap<>();
        map.put(TagType.ITEM, new ObjectOpenHashSet<>(TagManager.ITEM_TAGS.getAllTags()));
        map.put(TagType.FLUID, new ObjectOpenHashSet<>(TagManager.FLUID_TAGS.getAllTags()));
        map.put(TagType.BLOCK, new ObjectOpenHashSet<>(TagManager.BLOCK_TAGS.getAllTags()));
        return Collections.unmodifiableMap(map);
    }

    /**
     * Check if a tag exists for the specified type
     */
    public static boolean tagNameExist(@Nullable String tag, @Nonnull TagType type) {
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
    public static boolean tagNameExist(@Nullable String tag) {
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
    public static int getTagCount(@Nonnull TagType type) {
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
