package com.gardenevery.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

public final class TagHelper {

    private TagHelper() {
    }

    /**
     * Get all tags of an item
     */
    public static Set<String> tags(ItemStack stack) {
        var key = ItemKey.from(stack);
        return key != null ? TagManager.ITEM_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags of a fluid
     */
    public static Set<String> tags(FluidStack stack) {
        var key = FluidKey.from(stack);
        return key != null ? TagManager.FLUID_TAGS.getTags(key) : Collections.emptySet();
    }

    /**
     * Get all tags of a block
     */
    public static Set<String> tags(Block block) {
        var key = BlockKey.from(block);
        return TagManager.BLOCK_TAGS.getTags(key);
    }

    /**
     * Check if an item has a specific tag
     */
    public static boolean hasTag(ItemStack stack, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = ItemKey.from(stack);
        return key != null && TagManager.ITEM_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a fluid has a specific tag
     */
    public static boolean hasTag(FluidStack stack, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = FluidKey.from(stack);
        return key != null && TagManager.FLUID_TAGS.hasTag(key, tag);
    }

    /**
     * Check if a block has a specific tag
     */
    public static boolean hasTag(Block block, String tag) {
        if (isTagInvalid(tag)) {
            return false;
        }
        var key = BlockKey.from(block);
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
     * Get all items under a specific tag
     */
    public static Set<ItemStack> getItemStacks(String tagName) {
        var itemKeys = TagManager.ITEM_TAGS.getKeys(tagName);
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
        var fluidKeys = TagManager.FLUID_TAGS.getKeys(tagName);
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
            case ITEM -> TagManager.ITEM_TAGS.getAllTagNames();
            case FLUID -> TagManager.FLUID_TAGS.getAllTagNames();
            case BLOCK -> TagManager.BLOCK_TAGS.getAllTagNames();
        };
    }

    /**
     * Check if the corresponding type of label exists
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
     * Check if a tag exists
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
