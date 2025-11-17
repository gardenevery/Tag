package com.gardenevery.tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gardenevery.tag.event.*;
import com.gardenevery.tag.key.*;

@SuppressWarnings("deprecation")
public abstract class TagBuilder {

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

    static void closeRegistration() {
        registrationClosed = true;
    }

    /**
     * Create an item tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.item("minecraft:weapons").add(itemStack);
     */
    @Nonnull
    public static InitialItemTagBuilder item(@Nullable String tagName) {
        return new InitialItemTagBuilder(tagName);
    }

    /**
     * Create a fluid tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluid("forge:lava").add(fluidStack);
     */
    @Nonnull
    public static InitialFluidTagBuilder fluid(@Nullable String tagName) {
        return new InitialFluidTagBuilder(tagName);
    }

    /**
     * Create a block tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.block("minecraft:logs").add(block);
     */
    @Nonnull
    public static InitialBlockTagBuilder block(@Nullable String tagName) {
        return new InitialBlockTagBuilder(tagName);
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

    public interface AddableState<T> {
        @Nonnull
        AddableState<T> add(@Nullable T element);
    }

    public interface RemovableState {
        void remove();
    }

    public interface InitialState<T> extends AddableState<T>, RemovableState {
    }

    public interface CompletedState {
    }

    public static class InitialItemTagBuilder extends TagBuilder implements InitialState<ItemStack> {
        InitialItemTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        @Nonnull
        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            if (!isValid) {
                return new InvalidItemTagBuilder();
            }

            var key = ItemKey.from(stack);
            if (key != null) {
                var event = ItemTagEvent.create(tagName, stack);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return new InvalidItemTagBuilder();
                }
                TagManager.ITEM_TAGS.createTag(tagName, key);
            }

            return new AddableItemTagBuilder(tagName);
        }

        @Override
        public void remove() {
            if (!isValid) {
                return;
            }

            var event = TagRemoveEvent.remove(tagName, TagType.ITEM);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                TagManager.ITEM_TAGS.removeTag(tagName);
            }
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        public AddableState<ItemStack> add(@Nullable Item item) {
            return add(new ItemStack(item));
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        public AddableState<ItemStack> add(@Nullable Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class AddableItemTagBuilder implements AddableState<ItemStack>, CompletedState {
        private final String tagName;

        AddableItemTagBuilder(String tagName) {
            this.tagName = tagName;
        }

        @Nonnull
        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            var key = ItemKey.from(stack);
            if (key != null) {
                var event = ItemTagEvent.create(tagName, stack);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    TagManager.ITEM_TAGS.createTag(tagName, key);
                }
            }
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        public AddableState<ItemStack> add(@Nullable Item item) {
            return add(new ItemStack(item));
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        public AddableState<ItemStack> add(@Nullable Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class InvalidItemTagBuilder implements AddableState<ItemStack>, RemovableState, CompletedState {
        @Nonnull
        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public void remove() {
        }
    }

    public static class InitialFluidTagBuilder extends TagBuilder implements InitialState<FluidStack> {
        InitialFluidTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        @Nonnull
        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            if (!isValid) {
                return new InvalidFluidTagBuilder();
            }

            var key = FluidKey.from(stack);
            if (key != null) {
                var event = FluidTagEvent.create(tagName, stack);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return new InvalidFluidTagBuilder();
                }
                TagManager.FLUID_TAGS.createTag(tagName, key);
            }

            return new AddableFluidTagBuilder(tagName);
        }

        @Override
        public void remove() {
            if (!isValid) {
                return;
            }

            var event = TagRemoveEvent.remove(tagName, TagType.FLUID);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                TagManager.FLUID_TAGS.removeTag(tagName);
            }
        }

        @Nonnull
        public AddableState<FluidStack> add(@Nullable Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class AddableFluidTagBuilder implements AddableState<FluidStack>, CompletedState {
        private final String tagName;

        AddableFluidTagBuilder(String tagName) {
            this.tagName = tagName;
        }

        @Nonnull
        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            var key = FluidKey.from(stack);
            if (key != null) {
                var event = FluidTagEvent.create(tagName, stack);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    TagManager.FLUID_TAGS.createTag(tagName, key);
                }
            }
            return this;
        }

        @Nonnull
        public AddableState<FluidStack> add(@Nullable Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class InvalidFluidTagBuilder implements AddableState<FluidStack>, RemovableState, CompletedState {
        @Nonnull
        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public void remove() {
        }
    }

    public static class InitialBlockTagBuilder extends TagBuilder implements InitialState<Block> {
        InitialBlockTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        @Nonnull
        @Override
        public AddableState<Block> add(@Nullable Block block) {
            if (!isValid) {
                return new InvalidBlockTagBuilder();
            }

            var key = BlockKey.from(block);
            if (key != null) {
                var event = BlockTagEvent.create(tagName, block);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return new InvalidBlockTagBuilder();
                }
                TagManager.BLOCK_TAGS.createTag(tagName, key);
            }

            return new AddableBlockTagBuilder(tagName);
        }

        @Override
        public void remove() {
            if (!isValid) {
                return;
            }

            var event = TagRemoveEvent.remove(tagName, TagType.BLOCK);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                TagManager.BLOCK_TAGS.removeTag(tagName);
            }
        }
    }

    public static class AddableBlockTagBuilder implements AddableState<Block>, CompletedState {
        private final String tagName;

        AddableBlockTagBuilder(String tagName) {
            this.tagName = tagName;
        }

        @Nonnull
        @Override
        public AddableState<Block> add(@Nullable Block block) {
            var key = BlockKey.from(block);
            if (key != null) {
                var event = BlockTagEvent.create(tagName, block);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    TagManager.BLOCK_TAGS.createTag(tagName, key);
                }
            }
            return this;
        }
    }

    public static class InvalidBlockTagBuilder implements AddableState<Block>, RemovableState, CompletedState {
        @Nonnull
        @Override
        public AddableState<Block> add(@Nullable Block block) {
            return this;
        }

        @Override
        public void remove() {
        }
    }
}
