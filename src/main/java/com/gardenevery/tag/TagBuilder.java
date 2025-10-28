package com.gardenevery.tag;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

@SuppressWarnings("UnusedReturnValue")
public abstract class TagBuilder<T> {

    private static final Logger LOGGER = LogManager.getLogger("TagBuilder");

    protected final String tagName;
    protected final TagManager tagManager;
    protected final boolean isValid;

    protected TagBuilder(String tagName) {
        this.isValid = validateTagName(tagName);
        this.tagName = this.isValid ? tagName : null;
        this.tagManager = TagManager.getInstance();
    }

    public abstract TagBuilder<T> add(T element);

    /**
     * Create a item tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.item("minecraft:stones").add(ItemStack).add(ItemStack);
     */
    public static ItemTagBuilder item(String tagName) {
        return new ItemTagBuilder(tagName);
    }

    /**
     * Create a fluid tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.fluid("forge:lava").add(FluidStack).add(FluidStack);
     */
    public static FluidTagBuilder fluid(String tagName) {
        return new FluidTagBuilder(tagName);
    }

    /**
     * Create a block tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.block("minecraft:needs_stone_tool").add(Block).add(Block);
     */
    public static BlockTagBuilder block(String tagName) {
        return new BlockTagBuilder(tagName);
    }

    private static boolean validateTagName(String name) {
        if (name == null || name.isEmpty()) {
            LOGGER.warn("Tag name cannot be null or empty");
            return false;
        }

        if (!name.matches("[a-zA-Z:_/]+")) {
            LOGGER.warn("Invalid tag name '{}'. Only letters, :, _, / are allowed. Example: 'minecraft:stones'", name);
            return false;
        }

        return true;
    }

    public static class ItemTagBuilder extends TagBuilder<ItemStack> {

        ItemTagBuilder(String tagName) {
            super(tagName);
        }

        @Override
        public ItemTagBuilder add(ItemStack stack) {
            if (!isValid) {
                return this;
            }
            var key = ItemKey.from(stack);
            if (key != null) {
                tagManager.itemTags.createTag(tagName, key);
            }
            return this;
        }
    }

    public static class FluidTagBuilder extends TagBuilder<FluidStack> {

        FluidTagBuilder(String tagName) {
            super(tagName);
        }

        @Override
        public FluidTagBuilder add(FluidStack stack) {
            if (!isValid) {
                return this;
            }
            var key = FluidKey.from(stack);
            if (key != null) {
                tagManager.fluidTags.createTag(tagName, key);
            }
            return this;
        }
    }

    public static class BlockTagBuilder extends TagBuilder<Block> {

        BlockTagBuilder(String tagName) {
            super(tagName);
        }

        @Override
        public BlockTagBuilder add(Block block) {
            if (!isValid) {
                return this;
            }
            var key = BlockKey.from(block);
            if (key != null) {
                tagManager.blockTags.createTag(tagName, key);
            }
            return this;
        }
    }
}
