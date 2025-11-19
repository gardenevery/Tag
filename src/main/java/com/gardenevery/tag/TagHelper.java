package com.gardenevery.tag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class TagHelper {

    private TagHelper() {
    }

    /**
     * Get all tags associated with an item
     */
    @Nonnull
    public static Set<String> tags(@Nullable ItemStack stack) {
        var key = Key.ItemKey.from(stack);
        return key != null ? TagManager.ITEM.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a fluid
     */
    @Nonnull
    public static Set<String> tags(@Nullable FluidStack stack) {
        var key = Key.FluidKey.from(stack);
        return key != null ? TagManager.FLUID.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block
     */
    @Nonnull
    public static Set<String> tags(@Nullable Block block) {
        var key = Key.BlockKey.from(block);
        return key != null ? TagManager.BLOCK.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block state
     */
    @Nonnull
    public static Set<String> tags(@Nullable IBlockState blockState) {
        if (blockState == null) {
            return Collections.emptySet();
        }
        var key = Key.BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK.getTags(key);
    }

    /**
     * Check if an item has the specified tag
     */
    public static boolean hasTag(@Nullable ItemStack stack, @Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = Key.ItemKey.from(stack);
        return key != null && TagManager.ITEM.hasTag(key, tag);
    }

    /**
     * Check if a fluid has the specified tag
     */
    public static boolean hasTag(@Nullable FluidStack stack, @Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = Key.FluidKey.from(stack);
        return key != null && TagManager.FLUID.hasTag(key, tag);
    }

    /**
     * Check if a block has the specified tag
     */
    public static boolean hasTag(@Nullable Block block, @Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = Key.BlockKey.from(block);
        return key != null && TagManager.BLOCK.hasTag(key, tag);
    }

    /**
     * Check if a block state has the specified tag
     */
    public static boolean hasTag(@Nullable IBlockState blockState, @Nullable String tag) {
        if (isTagInvalid(tag) || blockState == null) {
            return false;
        }
        var key = Key.BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK.hasTag(key, tag);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable ItemStack stack, @Nullable String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = Key.ItemKey.from(stack);
        return key != null && TagManager.ITEM.hasAnyTag(key, new HashSet<>(Arrays.asList(tags)));
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable ItemStack stack, @Nullable Set<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = Key.ItemKey.from(stack);
        return key != null && TagManager.ITEM.hasAnyTag(key, tags);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable FluidStack stack, @Nullable String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = Key.FluidKey.from(stack);
        return key != null && TagManager.FLUID.hasAnyTag(key, new HashSet<>(Arrays.asList(tags)));
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable FluidStack stack, @Nullable Set<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = Key.FluidKey.from(stack);
        return key != null && TagManager.FLUID.hasAnyTag(key, tags);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable Block block, @Nullable String... tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = Key.BlockKey.from(block);
        return key != null && TagManager.BLOCK.hasAnyTag(key, new HashSet<>(Arrays.asList(tags)));
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable Block block, @Nullable Set<String> tags) {
        if (areTagsInvalid(tags)) {
            return false;
        }
        var key = Key.BlockKey.from(block);
        return key != null && TagManager.BLOCK.hasAnyTag(key, tags);
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable IBlockState blockState, @Nullable String... tags) {
        if (areTagsInvalid(tags) || blockState == null) {
            return false;
        }
        var key = Key.BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK.hasAnyTag(key, new HashSet<>(Arrays.asList(tags)));
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable IBlockState blockState, @Nullable Set<String> tags) {
        if (areTagsInvalid(tags) || blockState == null) {
            return false;
        }
        var key = Key.BlockKey.from(blockState.getBlock());
        return TagManager.BLOCK.hasAnyTag(key, tags);
    }

    /**
     * Get all items that have the specified tag
     */
    @Nonnull
    public static Set<ItemStack> getItemStacks(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }
        Set<Key.ItemKey> keys = TagManager.ITEM.getKeys(tagName);
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
        Set<Key.FluidKey> keys = TagManager.FLUID.getKeys(tagName);
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
        Set<Key.BlockKey> keys = TagManager.BLOCK.getKeys(tagName);
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
            case ITEM -> TagManager.ITEM.getAllTags();
            case FLUID -> TagManager.FLUID.getAllTags();
            case BLOCK -> TagManager.BLOCK.getAllTags();
        };
    }

    /**
     * Get all registered tag names grouped by type
     */
    public static Map<TagType, Set<String>> getAllTags() {
        Map<TagType, Set<String>> map = new HashMap<>();
        map.put(TagType.ITEM, new ObjectOpenHashSet<>(TagManager.ITEM.getAllTags()));
        map.put(TagType.FLUID, new ObjectOpenHashSet<>(TagManager.FLUID.getAllTags()));
        map.put(TagType.BLOCK, new ObjectOpenHashSet<>(TagManager.BLOCK.getAllTags()));
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
            case ITEM -> TagManager.ITEM.doesTagNameExist(tag);
            case FLUID -> TagManager.FLUID.doesTagNameExist(tag);
            case BLOCK -> TagManager.BLOCK.doesTagNameExist(tag);
        };
    }

    /**
     * Check if a tag exists in any type
     */
    public static boolean tagNameExist(@Nullable String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        return (TagManager.ITEM.doesTagNameExist(tag) ||
                TagManager.FLUID.doesTagNameExist(tag) ||
                TagManager.BLOCK.doesTagNameExist(tag)
        );
    }

    /**
     * Get the total number of tags for the specified type
     */
    public static int getTagCount(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM.getTagCount();
            case FLUID -> TagManager.FLUID.getTagCount();
            case BLOCK -> TagManager.BLOCK.getTagCount();
        };
    }

    /**
     * Get the total number of tags across all types
     */
    public static int getTagCount() {
        return TagManager.ITEM.getTagCount() +
                TagManager.FLUID.getTagCount() +
                TagManager.BLOCK.getTagCount();
    }

    /**
     * Get the total number of associations for the specified type
     * (sum of all keys across all tags)
     */
    public static int getTotalAssociations(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM.getTotalAssociations();
            case FLUID -> TagManager.FLUID.getTotalAssociations();
            case BLOCK -> TagManager.BLOCK.getTotalAssociations();
        };
    }

    /**
     * Get the total number of associations across all types
     */
    public static int getTotalAssociations() {
        return TagManager.ITEM.getTotalAssociations() +
                TagManager.FLUID.getTotalAssociations() +
                TagManager.BLOCK.getTotalAssociations();
    }

    /**
     * Get the number of unique keys for the specified type
     * (count of distinct keys across all tags)
     */
    public static int getUniqueKeyCount(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM.getUniqueKeyCount();
            case FLUID -> TagManager.FLUID.getUniqueKeyCount();
            case BLOCK -> TagManager.BLOCK.getUniqueKeyCount();
        };
    }

    /**
     * Get the total number of unique keys across all types
     */
    public static int getUniqueKeyCount() {
        return TagManager.ITEM.getUniqueKeyCount() +
                TagManager.FLUID.getUniqueKeyCount() +
                TagManager.BLOCK.getUniqueKeyCount();
    }

    private static boolean isTagInvalid(@Nullable String tag) {
        return tag == null || tag.isEmpty();
    }

    private static boolean areTagsInvalid(@Nullable Collection<String> tags) {
        return tags == null || tags.isEmpty();
    }

    private static boolean areTagsInvalid(@Nullable String[] tags) {
        return tags == null || tags.length == 0;
    }
}
