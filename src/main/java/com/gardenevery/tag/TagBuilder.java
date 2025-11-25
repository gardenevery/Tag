package com.gardenevery.tag;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gardenevery.tag.Key.BlockKey;
import com.gardenevery.tag.Key.FluidKey;
import com.gardenevery.tag.Key.ItemKey;

public abstract class TagBuilder {

    private static final Logger LOGGER = LogManager.getLogger("TagBuilder");
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9:_/]+");

    protected final String tagName;
    protected final boolean isValid;
    private static boolean registrationClosed = false;

    protected TagBuilder(String tagName) {
        this.isValid = !registrationClosed && validateTagName(tagName);
        this.tagName = this.isValid ? tagName : null;

        if (registrationClosed && validateTagName(tagName)) {
            LOGGER.warn("Tag registration is closed after FMLLoadCompleteEvent. Tag '{}' will not be registered.",
                    tagName);
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

    public static ItemTagBuilder.InitialState item(@Nullable String tagName) {
        return new ItemTagBuilder(tagName).new InitialState();
    }

    /**
     * Create a fluid tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluid("forge:lava").add(fluidStack);
     */

    public static FluidTagBuilder.InitialState fluid(@Nullable String tagName) {
        return new FluidTagBuilder(tagName).new InitialState();
    }

    /**
     * Create a block tag builder
     * @param tagName Tag name (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.block("minecraft:logs").add(block);
     */

    public static BlockTagBuilder.InitialState block(@Nullable String tagName) {
        return new BlockTagBuilder(tagName).new InitialState();
    }

    /**
     * Create an item tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.items("minecraft:tools", "minecraft:diggers", "mod:iron_tier")
     */

    public static MultiItemTagBuilder.InitialState item(@Nullable String... tagNames) {
        return new MultiItemTagBuilder(tagNames).new InitialState();
    }

    /**
     * Create a fluid tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.fluids("forge:liquids", "mod:coolant", "pack:hazardous")
     */

    public static MultiFluidTagBuilder.InitialState fluid(@Nullable String... tagNames) {
        return new MultiFluidTagBuilder(tagNames).new InitialState();
    }

    /**
     * Create a block tag builder with multiple tags
     * @param tagNames Multiple tag names (only letters, numbers, :, _, / allowed)
     * Example: TagBuilder.blocks("forge:ores", "mod:world_gen", "pack:rare_blocks")
     */

    public static MultiBlockTagBuilder.InitialState block(@Nullable String... tagNames) {
        return new MultiBlockTagBuilder(tagNames).new InitialState();
    }

    static boolean validateTagName(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            LOGGER.warn("Tag name cannot be null or empty");
            return false;
        }

        if (!TAG_NAME_PATTERN.matcher(name).matches()) {
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
        int invalidCount = 0;
        for (var name : names) {
            if (name == null || name.isEmpty()) {
                invalidCount++;
                continue;
            }

            if (!TAG_NAME_PATTERN.matcher(name).matches()) {
                invalidCount++;
                continue;
            }
            validNames.add(name);
        }

        if (validNames.isEmpty()) {
            LOGGER.warn("No valid tag names provided. Total: {}, all invalid: {}",
                    names.length, invalidCount);
            return null;
        }

        if (invalidCount > 0) {
            LOGGER.info("Tag names filtered. Total: {}, valid: {}, invalid: {}",
                    names.length, validNames.size(), invalidCount);
        }
        return Collections.unmodifiableSet(validNames);
    }

    public interface AddableState<T> {
        AddableState<T> add(@Nullable T element);
    }

    public interface RemovableState {
        void remove();
    }

    public interface InitialState<T> extends AddableState<T>, RemovableState {}

    public interface CompletedState {}

    public interface MultiAddableState<T> {
        MultiAddableState<T> add(@Nullable T element);
    }

    public interface MultiRemovableState {
        void remove();
    }

    public interface MultiInitialState<T> extends MultiAddableState<T>, MultiRemovableState {}

    public interface MultiCompletedState {}

    public static class ItemTagBuilder extends TagBuilder {
        private ItemTagBuilder(String tagName) {
            super(tagName);
        }

        public class InitialState implements TagBuilder.InitialState<ItemStack> {
            @Override
            public AddableState<ItemStack> add(@Nullable ItemStack stack) {
                if (!isValid) {
                    return new InvalidItemTag();
                }

                var key = ItemKey.from(stack);
                if (key != null) {
                    TagManager.ITEM.createTag(tagName, key);
                }
                return new ItemTagAdder(tagName);
            }

            @Override
            public void remove() {
                if (!isValid) {
                    return;
                }
                TagManager.ITEM.removeTag(tagName);
            }

            public AddableState<ItemStack> add(Item item) {
                return add(new ItemStack(item));
            }

            public AddableState<ItemStack> add(Item item, int metadata) {
                return add(new ItemStack(item, 1, metadata));
            }
        }
    }

    public static class ItemTagAdder implements AddableState<ItemStack>, CompletedState {
        private final String tagName;

        ItemTagAdder(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            var key = ItemKey.from(stack);
            if (key != null) {
                TagManager.ITEM.createTag(tagName, key);
            }
            return this;
        }

        public AddableState<ItemStack> add(Item item) {
            return add(new ItemStack(item));
        }

        public AddableState<ItemStack> add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class InvalidItemTag implements AddableState<ItemStack>, RemovableState, CompletedState {
        @Override
        public AddableState<ItemStack> add(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public void remove() {}
    }

    public static class FluidTagBuilder extends TagBuilder {
        private FluidTagBuilder(String tagName) {
            super(tagName);
        }

        public class InitialState implements TagBuilder.InitialState<FluidStack> {
            @Override
            public AddableState<FluidStack> add(@Nullable FluidStack stack) {
                if (!isValid) {
                    return new InvalidFluidTag();
                }

                var key = FluidKey.from(stack);
                if (key != null) {
                    TagManager.FLUID.createTag(tagName, key);
                }
                return new FluidTagAdder(tagName);
            }

            @Override
            public void remove() {
                if (!isValid) {
                    return;
                }
                TagManager.FLUID.removeTag(tagName);
            }

            public AddableState<FluidStack> add(Fluid fluid) {
                return add(new FluidStack(fluid, 1000));
            }
        }
    }

    public static class FluidTagAdder implements AddableState<FluidStack>, CompletedState {
        private final String tagName;

        FluidTagAdder(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            var key = FluidKey.from(stack);
            if (key != null) {
                TagManager.FLUID.createTag(tagName, key);
            }
            return this;
        }

        public AddableState<FluidStack> add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class InvalidFluidTag implements AddableState<FluidStack>, RemovableState, CompletedState {
        @Override
        public AddableState<FluidStack> add(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public void remove() {}
    }

    public static class BlockTagBuilder extends TagBuilder {
        private BlockTagBuilder(String tagName) {
            super(tagName);
        }

        public class InitialState implements TagBuilder.InitialState<Block> {
            @Override
            public AddableState<Block> add(@Nullable Block block) {
                if (!isValid) {
                    return new InvalidBlockTag();
                }

                var key = BlockKey.from(block);
                if (key != null) {
                    TagManager.BLOCK.createTag(tagName, key);
                }
                return new BlockTagAdder(tagName);
            }

            @Override
            public void remove() {
                if (!isValid) {
                    return;
                }
                TagManager.BLOCK.removeTag(tagName);
            }
        }
    }

    public static class BlockTagAdder implements AddableState<Block>, CompletedState {
        private final String tagName;

        BlockTagAdder(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public AddableState<Block> add(@Nullable Block block) {
            var key = BlockKey.from(block);
            if (key != null) {
                TagManager.BLOCK.createTag(tagName, key);
            }
            return this;
        }
    }

    public static class InvalidBlockTag implements AddableState<Block>, RemovableState, CompletedState {
        @Override
        public AddableState<Block> add(@Nullable Block block) {
            return this;
        }

        @Override
        public void remove() {}
    }

    public static abstract class MultiTagBuilder {
        protected final Set<String> tagNames;
        protected final boolean isValid;

        protected MultiTagBuilder(String[] tagNames) {
            Set<String> validNames = validateMultiTagNames(tagNames);
            this.isValid = !registrationClosed && validNames != null;
            this.tagNames = this.isValid ? validNames : Collections.emptySet();

            if (registrationClosed && validNames != null) {
                LOGGER.warn("Tag registration is closed after FMLLoadCompleteEvent. Tags '{}' will not be registered.",
                        validNames);
            }
        }
    }

    public static class MultiItemTagBuilder extends MultiTagBuilder {
        private MultiItemTagBuilder(String... tagNames) {
            super(tagNames);
        }

        public class InitialState implements TagBuilder.MultiInitialState<ItemStack> {
            @Override
            public MultiAddableState<ItemStack> add(@Nullable ItemStack stack) {
                if (!isValid) {
                    return new InvalidMultiItemTag();
                }

                var key = ItemKey.from(stack);
                if (key != null) {
                    TagManager.ITEM.createTags(tagNames, key);
                }
                return new MultiItemTagAdder(tagNames);
            }

            @Override
            public void remove() {
                if (!isValid) {
                    return;
                }
                TagManager.ITEM.removeTags(tagNames);
            }

            public MultiAddableState<ItemStack> add(Item item) {
                return add(new ItemStack(item));
            }

            public MultiAddableState<ItemStack> add(Item item, int metadata) {
                return add(new ItemStack(item, 1, metadata));
            }
        }
    }

    public static class MultiItemTagAdder implements MultiAddableState<ItemStack>, MultiCompletedState {
        private final Set<String> tagNames;

        MultiItemTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiAddableState<ItemStack> add(@Nullable ItemStack stack) {
            var key = ItemKey.from(stack);
            if (key != null) {
                TagManager.ITEM.createTags(tagNames, key);
            }
            return this;
        }

        public MultiAddableState<ItemStack> add(Item item) {
            return add(new ItemStack(item));
        }

        public MultiAddableState<ItemStack> add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class MultiFluidTagBuilder extends MultiTagBuilder {
        private MultiFluidTagBuilder(String... tagNames) {
            super(tagNames);
        }

        public class InitialState implements TagBuilder.MultiInitialState<FluidStack> {
            @Override
            public MultiAddableState<FluidStack> add(@Nullable FluidStack stack) {
                if (!isValid) {
                    return new InvalidMultiFluidTag();
                }

                var key = FluidKey.from(stack);
                if (key != null) {
                    TagManager.FLUID.createTags(tagNames, key);
                }
                return new MultiFluidTagAdder(tagNames);
            }

            @Override
            public void remove() {
                if (!isValid) {
                    return;
                }
                TagManager.FLUID.removeTags(tagNames);
            }

            public MultiAddableState<FluidStack> add(Fluid fluid) {
                return add(new FluidStack(fluid, 1000));
            }
        }
    }

    public static class MultiFluidTagAdder implements MultiAddableState<FluidStack>, MultiCompletedState {
        private final Set<String> tagNames;

        MultiFluidTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiAddableState<FluidStack> add(@Nullable FluidStack stack) {
            var key = FluidKey.from(stack);
            if (key != null) {
                TagManager.FLUID.createTags(tagNames, key);
            }
            return this;
        }

        public MultiAddableState<FluidStack> add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class MultiBlockTagBuilder extends MultiTagBuilder {
        private MultiBlockTagBuilder(String... tagNames) {
            super(tagNames);
        }

        public class InitialState implements TagBuilder.MultiInitialState<Block> {
            @Override
            public MultiAddableState<Block> add(@Nullable Block block) {
                if (!isValid) {
                    return new InvalidMultiBlockTag();
                }

                var key = BlockKey.from(block);
                if (key != null) {
                    TagManager.BLOCK.createTags(tagNames, key);
                }
                return new MultiBlockTagAdder(tagNames);
            }

            @Override
            public void remove() {
                if (!isValid) {
                    return;
                }
                TagManager.BLOCK.removeTags(tagNames);
            }
        }
    }

    public static class MultiBlockTagAdder implements MultiAddableState<Block>, MultiCompletedState {
        private final Set<String> tagNames;

        MultiBlockTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiAddableState<Block> add(@Nullable Block block) {
            var key = BlockKey.from(block);
            if (key != null) {
                TagManager.BLOCK.createTags(tagNames, key);
            }
            return this;
        }
    }

    public static class InvalidMultiItemTag implements MultiAddableState<ItemStack>, MultiCompletedState {
        @Override
        public MultiAddableState<ItemStack> add(@Nullable ItemStack stack) {
            return this;
        }
    }

    public static class InvalidMultiFluidTag implements MultiAddableState<FluidStack>, MultiCompletedState {
        @Override
        public MultiAddableState<FluidStack> add(@Nullable FluidStack stack) {
            return this;
        }
    }

    public static class InvalidMultiBlockTag implements MultiAddableState<Block>, MultiCompletedState {
        @Override
        public MultiAddableState<Block> add(@Nullable Block block) {
            return this;
        }
    }
}
