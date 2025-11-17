package com.gardenevery.tag;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
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

    /**
     * Create an item tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.items("minecraft:tools", "minecraft:diggers", "mod:iron_tier")
     */
    @Nonnull
    public static MultiItemTagBuilder items(@Nullable String... tagNames) {
        return new MultiItemTagBuilder(tagNames);
    }

    /**
     * Create a fluid tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluids("forge:liquids", "mod:coolant", "pack:hazardous")
     */
    @Nonnull
    public static MultiFluidTagBuilder fluids(@Nullable String... tagNames) {
        return new MultiFluidTagBuilder(tagNames);
    }

    /**
     * Create a block tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.blocks("forge:ores", "mod:world_gen", "pack:rare_blocks")
     */
    @Nonnull
    public static MultiBlockTagBuilder blocks(@Nullable String... tagNames) {
        return new MultiBlockTagBuilder(tagNames);
    }

    static boolean validateTagName(@Nullable String name) {
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

    static Set<String> validateMultiTagNames(@Nullable String[] names) {
        if (names == null || names.length == 0) {
            LOGGER.warn("Tag names array cannot be null or empty");
            return null;
        }

        Set<String> validNames = new LinkedHashSet<>();
        for (var name : names) {
            if (validateTagName(name)) {
                if (!validNames.add(name)) {
                    LOGGER.debug("Duplicate tag name '{}' found and ignored", name);
                }
            }
        }

        if (validNames.isEmpty()) {
            LOGGER.warn("No valid tag names provided");
            return null;
        }

        if (validNames.size() < names.length) {
            LOGGER.info("Filtered {} tag names to {} unique valid names", names.length, validNames.size());
        }

        return Collections.unmodifiableSet(validNames);
    }

    public interface AddableState<T> {
        @Nonnull
        AddableState<T> add(T element);
    }

    public interface MultiAddableState<T> {
        @Nonnull
        MultiAddableState<T> add(T element);
    }

    public interface RemovableState {
        void remove();
    }

    public interface InitialState<T> extends AddableState<T>, RemovableState {
    }

    public interface CompletedState {
    }

    public static class ItemTagBuilder extends TagBuilder implements InitialState<ItemStack> {
        ItemTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        @Nonnull
        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            if (!isValid) {
                return new InvalidItemTag();
            }

            var key = ItemKey.from(stack);
            if (key != null) {
                var event = ItemTagEvent.create(tagName, stack);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return new InvalidItemTag();
                }
                TagManager.ITEM_TAGS.createTag(tagName, key);
            }

            return new ItemTagAdder(tagName);
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
        public AddableState<ItemStack> add(Item item) {
            return add(new ItemStack(item));
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        public AddableState<ItemStack> add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class ItemTagAdder implements AddableState<ItemStack>, CompletedState {
        private final String tagName;

        ItemTagAdder(@Nullable String tagName) {
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
        public AddableState<ItemStack> add(Item item) {
            return add(new ItemStack(item));
        }

        @SuppressWarnings("ConstantConditions")
        @Nonnull
        public AddableState<ItemStack> add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class InvalidItemTag implements AddableState<ItemStack>, RemovableState, CompletedState {
        @Nonnull
        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public void remove() {
        }
    }

    public static class FluidTagBuilder extends TagBuilder implements InitialState<FluidStack> {
        FluidTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        @Nonnull
        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            if (!isValid) {
                return new InvalidFluidTag();
            }

            var key = FluidKey.from(stack);
            if (key != null) {
                var event = FluidTagEvent.create(tagName, stack);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return new InvalidFluidTag();
                }
                TagManager.FLUID_TAGS.createTag(tagName, key);
            }

            return new FluidTagAdder(tagName);
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
        public AddableState<FluidStack> add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class FluidTagAdder implements AddableState<FluidStack>, CompletedState {
        private final String tagName;

        FluidTagAdder(@Nullable String tagName) {
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
        public AddableState<FluidStack> add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class InvalidFluidTag implements AddableState<FluidStack>, RemovableState, CompletedState {
        @Nonnull
        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public void remove() {
        }
    }

    public static class BlockTagBuilder extends TagBuilder implements InitialState<Block> {
        BlockTagBuilder(@Nullable String tagName) {
            super(tagName);
        }

        @Nonnull
        @Override
        public AddableState<Block> add(Block block) {
            if (!isValid) {
                return new InvalidBlockTag();
            }

            var key = BlockKey.from(block);
            if (key != null) {
                var event = BlockTagEvent.create(tagName, block);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return new InvalidBlockTag();
                }
                TagManager.BLOCK_TAGS.createTag(tagName, key);
            }

            return new BlockTagAdder(tagName);
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

    public static class BlockTagAdder implements AddableState<Block>, CompletedState {
        private final String tagName;

        BlockTagAdder(@Nullable String tagName) {
            this.tagName = tagName;
        }

        @Nonnull
        @Override
        public AddableState<Block> add(Block block) {
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

    public static class InvalidBlockTag implements AddableState<Block>, RemovableState, CompletedState {
        @Nonnull
        @Override
        public AddableState<Block> add(Block block) {
            return this;
        }

        @Override
        public void remove() {
        }
    }

    public static abstract class MultiTagBuilder {
        protected final Set<String> tagNames;
        protected final boolean isValid;

        protected MultiTagBuilder(@Nullable String[] tagNames) {
            Set<String> validNames = validateMultiTagNames(tagNames);
            this.isValid = !registrationClosed && validNames != null;
            this.tagNames = this.isValid ? validNames : Collections.emptySet();

            if (registrationClosed && validNames != null) {
                LOGGER.warn("Tag registration is closed after FMLLoadCompleteEvent. Tags '{}' will not be registered.",
                        validNames);
            }
        }
    }

    public static class MultiItemTagBuilder extends MultiTagBuilder implements MultiAddableState<ItemStack> {
        MultiItemTagBuilder(@Nullable String... tagNames) {
            super(tagNames);
        }

        @Nonnull
        @Override
        public MultiAddableState<ItemStack> add(@Nullable ItemStack stack) {
            if (!isValid) {
                return new InvalidMultiItemTag();
            }

            var key = ItemKey.from(stack);
            if (key != null) {
                for (var tagName : tagNames) {
                    var event = ItemTagEvent.create(tagName, stack);
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        continue;
                    }
                    TagManager.ITEM_TAGS.createTag(tagName, key);
                }
            }

            return new MultiItemTagAdder(tagNames);
        }

        @Nonnull
        public MultiAddableState<ItemStack> add(Item item) {
            return add(new ItemStack(item));
        }

        @Nonnull
        public MultiAddableState<ItemStack> add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class MultiItemTagAdder implements MultiAddableState<ItemStack>, CompletedState {
        private final Set<String> tagNames;

        MultiItemTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Nonnull
        @Override
        public MultiAddableState<ItemStack> add(@Nullable ItemStack stack) {
            var key = ItemKey.from(stack);
            if (key != null) {
                for (var tagName : tagNames) {
                    var event = ItemTagEvent.create(tagName, stack);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        TagManager.ITEM_TAGS.createTag(tagName, key);
                    }
                }
            }
            return this;
        }

        @Nonnull
        public MultiAddableState<ItemStack> add(Item item) {
            return add(new ItemStack(item));
        }

        @Nonnull
        public MultiAddableState<ItemStack> add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class MultiFluidTagBuilder extends MultiTagBuilder implements MultiAddableState<FluidStack> {
        MultiFluidTagBuilder(@Nullable String... tagNames) {
            super(tagNames);
        }

        @Nonnull
        @Override
        public MultiAddableState<FluidStack> add(@Nullable FluidStack stack) {
            if (!isValid) {
                return new InvalidMultiFluidTag();
            }

            var key = FluidKey.from(stack);
            if (key != null) {
                for (var tagName : tagNames) {
                    var event = FluidTagEvent.create(tagName, stack);
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        continue;
                    }
                    TagManager.FLUID_TAGS.createTag(tagName, key);
                }
            }

            return new MultiFluidTagAdder(tagNames);
        }

        @Nonnull
        public MultiAddableState<FluidStack> add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class MultiFluidTagAdder implements MultiAddableState<FluidStack>, CompletedState {
        private final Set<String> tagNames;

        MultiFluidTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Nonnull
        @Override
        public MultiAddableState<FluidStack> add(@Nullable FluidStack stack) {
            var key = FluidKey.from(stack);
            if (key != null) {
                for (var tagName : tagNames) {
                    var event = FluidTagEvent.create(tagName, stack);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        TagManager.FLUID_TAGS.createTag(tagName, key);
                    }
                }
            }
            return this;
        }

        @Nonnull
        public MultiAddableState<FluidStack> add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class MultiBlockTagBuilder extends MultiTagBuilder implements MultiAddableState<Block> {
        MultiBlockTagBuilder(@Nullable String... tagNames) {
            super(tagNames);
        }

        @Nonnull
        @Override
        public MultiAddableState<Block> add(Block block) {
            if (!isValid) {
                return new InvalidMultiBlockTag();
            }

            var key = BlockKey.from(block);
            if (key != null) {
                for (var tagName : tagNames) {
                    var event = BlockTagEvent.create(tagName, block);
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        continue;
                    }
                    TagManager.BLOCK_TAGS.createTag(tagName, key);
                }
            }

            return new MultiBlockTagAdder(tagNames);
        }
    }

    public static class MultiBlockTagAdder implements MultiAddableState<Block>, CompletedState {
        private final Set<String> tagNames;

        MultiBlockTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Nonnull
        @Override
        public MultiAddableState<Block> add(Block block) {
            var key = BlockKey.from(block);
            if (key != null) {
                for (var tagName : tagNames) {
                    var event = BlockTagEvent.create(tagName, block);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        TagManager.BLOCK_TAGS.createTag(tagName, key);
                    }
                }
            }
            return this;
        }
    }

    public static class InvalidMultiItemTag implements MultiAddableState<ItemStack>, CompletedState {
        @Nonnull
        @Override
        public MultiAddableState<ItemStack> add(@Nullable ItemStack stack) {
            return this;
        }
    }

    public static class InvalidMultiFluidTag implements MultiAddableState<FluidStack>, CompletedState {
        @Nonnull
        @Override
        public MultiAddableState<FluidStack> add(@Nullable FluidStack stack) {
            return this;
        }
    }

    public static class InvalidMultiBlockTag implements MultiAddableState<Block>, CompletedState {
        @Nonnull
        @Override
        public MultiAddableState<Block> add(Block block) {
            return this;
        }
    }
}
