package com.gardenevery.tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    protected final boolean isValid;
    private static boolean registrationClosed = false;

    protected TagBuilder(@Nullable String tagName) {
        this.isValid = !registrationClosed && validateTagName(tagName);
        this.tagName = this.isValid ? tagName : null;

        if (registrationClosed && validateTagName(tagName)) {
            LOGGER.warn("Tag registration is closed after FMLLoadCompleteEvent. Tag '{}' will not be registered.", tagName);
        }
    }

    /**
     * Add an element to the tag
     */
    @Nonnull
    public abstract TagBuilder<T> add(@Nullable T element);

    static void closeRegistration() {
        registrationClosed = true;
    }

    /**
     * Create an item tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.item("minecraft:weapons").add(itemStack);
     */
    @Nonnull
    public static ItemTagBuilder item(@Nullable String tagName) {
        return new ItemTagBuilder(tagName);
    }

    /**
     * Create a fluid tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluid("forge:lava").add(fluidStack);
     */
    @Nonnull
    public static FluidTagBuilder fluid(@Nullable String tagName) {
        return new FluidTagBuilder(tagName);
    }

    /**
     * Create a block tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.block("minecraft:logs").add(block);
     */
    @Nonnull
    public static BlockTagBuilder block(@Nullable String tagName) {
        return new BlockTagBuilder(tagName);
    }

    private static boolean validateTagName(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            LOGGER.warn("Tag name cannot be null or empty");
            return false;
        }

        if (!name.matches("[a-zA-Z0-9:_/]+")) {
            LOGGER.warn("Invalid tag name '{}'. Only letters, numbers, :, _, / are allowed.", name);
            return false;
        }

        return true;
    }

    /**
     * Builder for item tags
     */
    public static class ItemTagBuilder extends TagBuilder<ItemStack> {

        ItemTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        /**
         * Add an item stack to the tag
         */
        @Nonnull
        @Override
        public ItemTagBuilder add(@Nullable ItemStack stack) {
            if (!isValid) {
                return this;
            }
            var key = ItemKey.from(stack);
            if (key != null) {
                TagManager.ITEM_TAGS.createTag(tagName, key);
            }
            return this;
        }
    }

    /**
     * Builder for fluid tags
     */
    public static class FluidTagBuilder extends TagBuilder<FluidStack> {

        FluidTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        /**
         * Add a fluid stack to the tag
         */
        @Nonnull
        @Override
        public FluidTagBuilder add(@Nullable FluidStack stack) {
            if (!isValid) {
                return this;
            }
            var key = FluidKey.from(stack);
            if (key != null) {
                TagManager.FLUID_TAGS.createTag(tagName, key);
            }
            return this;
        }
    }

    /**
     * Builder for block tags
     */
    public static class BlockTagBuilder extends TagBuilder<Block> {

        BlockTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        /**
         * Add a block to the tag
         */
        @Nonnull
        @Override
        public BlockTagBuilder add(@Nullable Block block) {
            if (!isValid) {
                return this;
            }
            var key = BlockKey.from(block);
            if (key != null) {
                TagManager.BLOCK_TAGS.createTag(tagName, key);
            }
            return this;
        }
    }
}
