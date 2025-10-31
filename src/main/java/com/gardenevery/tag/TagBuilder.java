package com.gardenevery.tag;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.BlockStateKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

@SuppressWarnings("UnusedReturnValue")
public abstract class TagBuilder<T> {

    private static final Logger LOGGER = LogManager.getLogger("TagBuilder");

    protected final String tagName;
    protected final boolean isValid;
    private static boolean registrationClosed = false;

    protected TagBuilder(String tagName) {
        if (registrationClosed) {
            throw new IllegalStateException("Tag registration is closed after FMLLoadCompleteEvent");
        }
        this.isValid = validateTagName(tagName);
        this.tagName = this.isValid ? tagName : null;
    }

    public abstract TagBuilder<T> add(T element);

    static void closeRegistration() {
        registrationClosed = true;
    }

    /**
     * Create an item tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.item("name").add(ItemStack).add(ItemStack);
     */
    public static ItemTagBuilder item(String tagName) {
        return new ItemTagBuilder(tagName);
    }

    /**
     * Create a fluid tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.fluid("name").add(FluidStack).add(FluidStack);
     */
    public static FluidTagBuilder fluid(String tagName) {
        return new FluidTagBuilder(tagName);
    }

    /**
     * Create a block tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.block("name").add(Block).add(Block);
     */
    public static BlockTagBuilder block(String tagName) {
        return new BlockTagBuilder(tagName);
    }

    /**
     * Create a IBlockState tag
     * @param tagName Tag name (only letters, :, _, / allowed)
     * Example: TagBuilder.blockState("name").add(IBlockState).add(IBlockState);
     */
    public static BlockStateTagBuilder blockState(String tagName) {
        return new BlockStateTagBuilder(tagName);
    }

    private static boolean validateTagName(String name) {
        if (name == null || name.isEmpty()) {
            LOGGER.warn("Tag name cannot be null or empty");
            return false;
        }

        if (!name.matches("[a-zA-Z:_/]+")) {
            LOGGER.warn("Invalid tag name '{}'. Only letters, :, _, / are allowed.", name);
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
                TagManager.ITEM_TAGS.createTag(tagName, key);
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
                TagManager.FLUID_TAGS.createTag(tagName, key);
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
                TagManager.BLOCK_TAGS.createTag(tagName, key);
            }
            return this;
        }
    }

    public static class BlockStateTagBuilder extends TagBuilder<IBlockState> {

        BlockStateTagBuilder(String tagName) {
            super(tagName);
        }

        @Override
        public BlockStateTagBuilder add(IBlockState state) {
            if (!isValid) {
                return this;
            }
            var key = BlockStateKey.from(state);
            if (key != null) {
                TagManager.BLOCK_STATE_TAGS.createTag(tagName, key);
            }
            return this;
        }
    }
}
