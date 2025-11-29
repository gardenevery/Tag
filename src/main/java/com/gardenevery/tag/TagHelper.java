package com.gardenevery.tag;

import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import com.gardenevery.tag.Key.BlockKey;
import com.gardenevery.tag.Key.FluidKey;
import com.gardenevery.tag.Key.ItemKey;

public final class TagHelper {

    private TagHelper() {}

    /**
     * Get all tags associated with an item
     */
    public static Set<String> tags(@Nullable ItemStack stack) {
        var key = ItemKey.toKey(stack);
        return key != null ? TagManager.ITEM.getTag(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a fluid
     */
    public static Set<String> tags(@Nullable FluidStack stack) {
        var key = FluidKey.toKey(stack);
        return key != null ? TagManager.FLUID.getTag(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block
     */
    public static Set<String> tags(@Nullable Block block) {
        var key = BlockKey.toKey(block);
        return key != null ? TagManager.BLOCK.getTag(key) : Collections.emptySet();
    }

    /**
     * Get all tags associated with a block state
     */
    public static Set<String> tags(@Nullable IBlockState blockState) {
        BlockKey key = null;
        if (blockState != null) {
            key = BlockKey.toKey(blockState.getBlock());
        }
        return key != null ? TagManager.BLOCK.getTag(key) : Collections.emptySet();
    }

    /**
     * Get all elements (keys) associated with a tag name and type
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> element(@Nullable String tagName, @Nonnull TagType type) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }

        return (Set<T>) switch (type) {
            case ITEM -> {
                Set<ItemKey> keys = TagManager.ITEM.getKey(tagName);
                Set<ItemStack> result = new ObjectOpenHashSet<>();
                for (var key : keys) {
                    result.add(key.toElement());
                }
                yield Collections.unmodifiableSet(result);
            }
            case FLUID -> {
                Set<FluidKey> keys = TagManager.FLUID.getKey(tagName);
                Set<FluidStack> result = new ObjectOpenHashSet<>();
                for (var key : keys) {
                    result.add(key.toElement());
                }
                yield Collections.unmodifiableSet(result);
            }
            case BLOCK -> {
                Set<BlockKey> keys = TagManager.BLOCK.getKey(tagName);
                Set<Block> result = new ObjectOpenHashSet<>();
                for (var key : keys) {
                    result.add(key.toElement());
                }
                yield Collections.unmodifiableSet(result);
            }
        };
    }

    /**
     * Get all item elements associated with a tag name
     */
    public static Set<ItemStack> itemElement(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }

        Set<ItemKey> keys = TagManager.ITEM.getKey(tagName);
        Set<ItemStack> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.toElement());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all fluid elements associated with a tag name
     */
    public static Set<FluidStack> fluidElement(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }

        Set<FluidKey> keys = TagManager.FLUID.getKey(tagName);
        Set<FluidStack> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.toElement());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get all block elements associated with a tag name
     */
    public static Set<Block> blockElement(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return Collections.emptySet();
        }

        Set<BlockKey> keys = TagManager.BLOCK.getKey(tagName);
        Set<Block> result = new ObjectOpenHashSet<>();
        for (var key : keys) {
            result.add(key.toElement());
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Check if an item has the specified tag
     */
    public static boolean hasTag(@Nullable ItemStack stack, @Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return false;
        }

        var key = ItemKey.toKey(stack);
        return key != null && TagManager.ITEM.hasTag(key, tagName);
    }

    /**
     * Check if a fluid has the specified tag
     */
    public static boolean hasTag(@Nullable FluidStack stack, @Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return false;
        }

        var key = FluidKey.toKey(stack);
        return key != null && TagManager.FLUID.hasTag(key, tagName);
    }

    /**
     * Check if a block has the specified tag
     */
    public static boolean hasTag(@Nullable Block block, @Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return false;
        }

        var key = BlockKey.toKey(block);
        return key != null && TagManager.BLOCK.hasTag(key, tagName);
    }

    /**
     * Check if a block state has the specified tag
     */
    public static boolean hasTag(@Nullable IBlockState blockState, @Nullable String tagName) {
        if (isTagInvalid(tagName) || blockState == null) {
            return false;
        }

        var key = BlockKey.toKey(blockState.getBlock());
        return key != null && TagManager.BLOCK.hasTag(key, tagName);
    }

    /**
     * Check if an item has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable ItemStack stack, @Nullable Set<String> tagNames) {
        if (areTagsInvalid(tagNames)) {
            return false;
        }

        var key = ItemKey.toKey(stack);
        return key != null && TagManager.ITEM.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a fluid has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable FluidStack stack, @Nullable Set<String> tagNames) {
        if (areTagsInvalid(tagNames)) {
            return false;
        }

        var key = FluidKey.toKey(stack);
        return key != null && TagManager.FLUID.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a block has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable Block block, @Nullable Set<String> tagNames) {
        if (areTagsInvalid(tagNames)) {
            return false;
        }

        var key = BlockKey.toKey(block);
        return key != null && TagManager.BLOCK.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a blockState has any of the specified tags
     */
    public static boolean hasAnyTags(@Nullable IBlockState blockState, @Nullable Set<String> tagNames) {
        if (areTagsInvalid(tagNames) || blockState == null) {
            return false;
        }

        var key = BlockKey.toKey(blockState.getBlock());
        return key != null && TagManager.BLOCK.hasAnyTag(key, tagNames);
    }

    /**
     * Check if a tag exists for the specified type
     */
    public static boolean doesTagExist(@Nullable String tagName, @Nonnull TagType type) {
        if (isTagInvalid(tagName)) {
            return false;
        }

        return switch (type) {
            case ITEM -> TagManager.ITEM.doesTagExist(tagName);
            case FLUID -> TagManager.FLUID.doesTagExist(tagName);
            case BLOCK -> TagManager.BLOCK.doesTagExist(tagName);
        };
    }

    /**
     * Check if a tag exists in any type
     */
    public static boolean doesTagExist(@Nullable String tagName) {
        if (isTagInvalid(tagName)) {
            return false;
        }
        return (TagManager.ITEM.doesTagExist(tagName) || TagManager.FLUID.doesTagExist(tagName) || TagManager.BLOCK.doesTagExist(tagName));
    }

    /**
     * Check if an item exists in the tag system (has at least one tag)
     */
    public static boolean contains(@Nullable ItemStack stack) {
        var key = ItemKey.toKey(stack);
        return key != null && TagManager.ITEM.containsKey(key);
    }

    /**
     * Check if a fluid exists in the tag system (has at least one tag)
     */
    public static boolean contains(@Nullable FluidStack stack) {
        var key = FluidKey.toKey(stack);
        return key != null && TagManager.FLUID.containsKey(key);
    }

    /**
     * Check if a block exists in the tag system (has at least one tag)
     */
    public static boolean contains(@Nullable Block block) {
        var key = BlockKey.toKey(block);
        return key != null && TagManager.BLOCK.containsKey(key);
    }

    /**
     * Check if a block state exists in the tag system (has at least one tag)
     */
    public static boolean contains(@Nullable IBlockState blockState) {
        if (blockState == null) {
            return false;
        }

        var key = BlockKey.toKey(blockState.getBlock());
        return key != null && TagManager.BLOCK.containsKey(key);
    }

    /**
     * Get the total number of tags for the specified type
     */
    public static int tagCount(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM.getTagCount();
            case FLUID -> TagManager.FLUID.getTagCount();
            case BLOCK -> TagManager.BLOCK.getTagCount();
        };
    }

    /**
     * Get the total number of tags across all types
     */
    public static int tagCount() {
        return TagManager.ITEM.getTagCount() + TagManager.FLUID.getTagCount() + TagManager.BLOCK.getTagCount();
    }

    /**
     * Get the total number of associations for the specified type
     * (sum of all keys across all tags)
     */
    public static int totalAssociations(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM.getAssociations();
            case FLUID -> TagManager.FLUID.getAssociations();
            case BLOCK -> TagManager.BLOCK.getAssociations();
        };
    }

    /**
     * Get the total number of associations across all types
     */
    public static int associations() {
        return TagManager.ITEM.getAssociations() + TagManager.FLUID.getAssociations() + TagManager.BLOCK.getAssociations();
    }

    /**
     * Get the number of unique keys for the specified type
     * (count of distinct keys across all tags)
     */
    public static int uniqueKeyCount(@Nonnull TagType type) {
        return switch (type) {
            case ITEM -> TagManager.ITEM.getKeyCount();
            case FLUID -> TagManager.FLUID.getKeyCount();
            case BLOCK -> TagManager.BLOCK.getKeyCount();
        };
    }

    /**
     * Get the total number of unique keys across all types
     */
    public static int keyCount() {
        return TagManager.ITEM.getKeyCount() + TagManager.FLUID.getKeyCount() + TagManager.BLOCK.getKeyCount();
    }

    private static boolean isTagInvalid(@Nullable String tagName) {
        return tagName == null || tagName.isEmpty();
    }

    private static boolean areTagsInvalid(@Nullable Set<String> tagNames) {
        return tagNames == null || tagNames.isEmpty();
    }

    private static boolean areTagsInvalid(@Nullable String[] tagNames) {
        return tagNames == null || tagNames.length == 0;
    }
}
