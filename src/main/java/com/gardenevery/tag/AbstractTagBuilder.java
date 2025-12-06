package com.gardenevery.tag;

import java.util.Collections;
import java.util.HashSet;
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

@SuppressWarnings("UnusedReturnValue")
abstract class AbstractTagBuilder {

    private static final Logger LOGGER = LogManager.getLogger("TagBuilder");
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9:_/]+");

    protected final String tagName;
    protected final boolean isValid;
    private static boolean registrationClosed = false;

    protected AbstractTagBuilder(String tagName) {
        this.isValid = !registrationClosed && validateTagName(tagName);
        this.tagName = this.isValid ? tagName : null;

        if (registrationClosed && validateTagName(tagName)) {
            logRegistrationClosed();
        }
    }

    static void closeRegistration() {
        registrationClosed = true;
    }

    static boolean validateTagName(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            LOGGER.warn("Tag name '{}' is invalid.", name);
            return false;
        }
        return true;
    }

    static Set<String> validateTagName(@Nullable String[] names) {
        if (names == null || names.length == 0) {
            LOGGER.warn("Tag names array cannot be null or empty.");
            return Collections.emptySet();
        }

        Set<String> validNames = new HashSet<>();
        for (var name : names) {
            if (validateTagName(name)) {
                validNames.add(name);
            }
        }

        if (validNames.isEmpty()) {
            return Collections.emptySet();
        }
        return validNames;
    }

    private static void logRegistrationClosed() {
        LOGGER.warn("Tag registration is closed after FMLLoadCompleteEvent.");
    }

    public interface ItemAddable {
        ItemAddable add(Item item);
        ItemAddable add(Item item, int metadata);
        ItemAddable add(@Nullable ItemStack stack);
    }

    public interface FluidAddable {
        FluidAddable add(Fluid fluid);
        FluidAddable add(@Nullable FluidStack stack);
    }

    public interface BlockAddable {
        BlockAddable add(@Nullable Block block);
    }

    public interface MultiItemAddable {
        MultiItemAddable add(Item item);
        MultiItemAddable add(Item item, int metadata);
        MultiItemAddable add(@Nullable ItemStack stack);
    }

    public interface MultiFluidAddable {
        MultiFluidAddable add(Fluid fluid);
        MultiFluidAddable add(@Nullable FluidStack stack);
    }

    public interface MultiBlockAddable {
        MultiBlockAddable add(@Nullable Block block);
    }

    public interface ItemRemoveKey {
        ItemRemoveKey removeKey(Item item);
        ItemRemoveKey removeKey(Item item, int metadata);
        ItemRemoveKey removeKey(@Nullable ItemStack stack);
    }

    public interface FluidRemoveKey {
        FluidRemoveKey removeKey(Fluid fluid);
        FluidRemoveKey removeKey(@Nullable FluidStack stack);
    }

    public interface BlockRemoveKey {
        BlockRemoveKey removeKey(@Nullable Block block);
    }

    public interface MultiItemRemoveKey {
        MultiItemRemoveKey removeKey(Item item);
        MultiItemRemoveKey removeKey(Item item, int metadata);
        MultiItemRemoveKey removeKey(@Nullable ItemStack stack);
    }

    public interface MultiFluidRemoveKey {
        MultiFluidRemoveKey removeKey(Fluid fluid);
        MultiFluidRemoveKey removeKey(@Nullable FluidStack stack);
    }

    public interface MultiBlockRemoveKey {
        MultiBlockRemoveKey removeKey(@Nullable Block block);
    }

    public interface ItemInitialState {
        ItemAddable add(Item item);
        ItemAddable add(Item item, int metadata);
        ItemAddable add(@Nullable ItemStack stack);
        void removeTag();
        ItemRemoveKey removeKey(Item item);
        ItemRemoveKey removeKey(Item item, int metadata);
        ItemRemoveKey removeKey(@Nullable ItemStack stack);
    }

    public interface FluidInitialState {
        FluidAddable add(Fluid fluid);
        FluidAddable add(@Nullable FluidStack stack);
        void removeTag();
        FluidRemoveKey removeKey(Fluid fluid);
        FluidRemoveKey removeKey(@Nullable FluidStack stack);
    }

    public interface BlockInitialState {
        BlockAddable add(@Nullable Block block);
        void removeTag();
        BlockRemoveKey removeKey(@Nullable Block block);
    }

    public interface MultiItemInitialState {
        MultiItemAddable add(Item item);
        MultiItemAddable add(Item item, int metadata);
        MultiItemAddable add(@Nullable ItemStack stack);
        void removeTag();
        MultiItemRemoveKey removeKey(Item item);
        MultiItemRemoveKey removeKey(Item item, int metadata);
        MultiItemRemoveKey removeKey(@Nullable ItemStack stack);
    }

    public interface MultiFluidInitialState {
        MultiFluidAddable add(Fluid fluid);
        MultiFluidAddable add(@Nullable FluidStack stack);
        void removeTag();
        MultiFluidRemoveKey removeKey(Fluid fluid);
        MultiFluidRemoveKey removeKey(@Nullable FluidStack stack);
    }

    public interface MultiBlockInitialState {
        MultiBlockAddable add(@Nullable Block block);
        void removeTag();
        MultiBlockRemoveKey removeKey(@Nullable Block block);
    }

    public interface Completed {}

    public interface MultiCompleted {}

    public static class ItemTagBuilder extends AbstractTagBuilder {
        public ItemTagBuilder(String tagName) {
            super(tagName);
        }

        public ItemInitialState initialState() {
            return new InitialState();
        }

        public class InitialState implements ItemInitialState {
            @Override
            public ItemAddable add(@Nullable ItemStack stack) {
                if (!isValid) {
                    return new InvalidItemTag();
                }

                var key = ItemKey.toKey(stack);
                if (key != null) {
                    TagManager.ITEM.createTag(tagName, key);
                }
                return new ItemTagAdder(tagName);
            }

            @Override
            public ItemRemoveKey removeKey(@Nullable ItemStack stack) {
                if (!isValid) {
                    return new InvalidItemTag();
                }

                var key = ItemKey.toKey(stack);
                if (key != null) {
                    TagManager.ITEM.removeTagKey(tagName, key);
                }
                return new ItemTagRemover(tagName);
            }

            @Override
            public ItemRemoveKey removeKey(Item item) {
                return removeKey(new ItemStack(item));
            }

            @Override
            public ItemRemoveKey removeKey(Item item, int metadata) {
                return removeKey(new ItemStack(item, 1, metadata));
            }

            @Override
            public void removeTag() {
                if (!isValid) {
                    return;
                }
                TagManager.ITEM.removeTag(tagName);
            }

            @Override
            public ItemAddable add(Item item) {
                return add(new ItemStack(item));
            }

            @Override
            public ItemAddable add(Item item, int metadata) {
                return add(new ItemStack(item, 1, metadata));
            }
        }
    }

    public static class ItemTagRemover implements ItemRemoveKey, Completed {
        private final String tagName;

        ItemTagRemover(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public ItemRemoveKey removeKey(@Nullable ItemStack stack) {
            var key = ItemKey.toKey(stack);
            if (key != null) {
                TagManager.ITEM.removeTagKey(tagName, key);
            }
            return this;
        }

        @Override
        public ItemRemoveKey removeKey(Item item) {
            return removeKey(new ItemStack(item));
        }

        @Override
        public ItemRemoveKey removeKey(Item item, int metadata) {
            return removeKey(new ItemStack(item, 1, metadata));
        }
    }

    public static class ItemTagAdder implements ItemAddable, Completed {
        private final String tagName;

        ItemTagAdder(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public ItemAddable add(@Nullable ItemStack stack) {
            var key = ItemKey.toKey(stack);
            if (key != null) {
                TagManager.ITEM.createTag(tagName, key);
            }
            return this;
        }

        @Override
        public ItemAddable add(Item item) {
            return add(new ItemStack(item));
        }

        @Override
        public ItemAddable add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class InvalidItemTag implements ItemAddable, ItemRemoveKey, Completed {
        @Override
        public ItemAddable add(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public ItemRemoveKey removeKey(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public ItemRemoveKey removeKey(Item item) {
            return this;
        }

        @Override
        public ItemRemoveKey removeKey(Item item, int metadata) {
            return this;
        }

        @Override
        public ItemAddable add(Item item) {
            return this;
        }

        @Override
        public ItemAddable add(Item item, int metadata) {
            return this;
        }
    }

    public static class FluidTagBuilder extends AbstractTagBuilder {
        public FluidTagBuilder(String tagName) {
            super(tagName);
        }

        public FluidInitialState initialState() {
            return new InitialState();
        }

        public class InitialState implements FluidInitialState {
            @Override
            public FluidAddable add(@Nullable FluidStack stack) {
                if (!isValid) {
                    return new InvalidFluidTag();
                }

                if (stack != null) {
                    TagManager.FLUID.createTag(tagName, stack.getFluid());
                }
                return new FluidTagAdder(tagName);
            }

            @Override
            public FluidRemoveKey removeKey(@Nullable FluidStack stack) {
                if (!isValid) {
                    return new InvalidFluidTag();
                }

                if (stack != null) {
                    TagManager.FLUID.removeTagKey(tagName, stack.getFluid());
                }
                return new FluidTagRemover(tagName);
            }

            @Override
            public FluidRemoveKey removeKey(Fluid fluid) {
                return removeKey(new FluidStack(fluid, 1000));
            }

            @Override
            public void removeTag() {
                if (!isValid) {
                    return;
                }
                TagManager.FLUID.removeTag(tagName);
            }

            @Override
            public FluidAddable add(Fluid fluid) {
                return add(new FluidStack(fluid, 1000));
            }
        }
    }

    public static class FluidTagRemover implements FluidRemoveKey, Completed {
        private final String tagName;

        FluidTagRemover(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public FluidRemoveKey removeKey(@Nullable FluidStack stack) {
            if (stack != null) {
                TagManager.FLUID.removeTagKey(tagName, stack.getFluid());
            }
            return this;
        }

        @Override
        public FluidRemoveKey removeKey(Fluid fluid) {
            return removeKey(new FluidStack(fluid, 1000));
        }
    }

    public static class FluidTagAdder implements FluidAddable, Completed {
        private final String tagName;

        FluidTagAdder(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public FluidAddable add(@Nullable FluidStack stack) {
            if (stack != null) {
                TagManager.FLUID.createTag(tagName, stack.getFluid());
            }
            return this;
        }

        @Override
        public FluidAddable add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class InvalidFluidTag implements FluidAddable, FluidRemoveKey, Completed {
        @Override
        public FluidAddable add(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public FluidRemoveKey removeKey(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public FluidRemoveKey removeKey(Fluid fluid) {
            return this;
        }

        @Override
        public FluidAddable add(Fluid fluid) {
            return this;
        }
    }

    public static class BlockTagBuilder extends AbstractTagBuilder {
        public BlockTagBuilder(String tagName) {
            super(tagName);
        }

        public BlockInitialState initialState() {
            return new InitialState();
        }

        public class InitialState implements BlockInitialState {
            @Override
            public BlockAddable add(@Nullable Block block) {
                if (!isValid) {
                    return new InvalidBlockTag();
                }

                if (block != null) {
                    TagManager.BLOCK.createTag(tagName, block);
                }
                return new BlockTagAdder(tagName);
            }

            @Override
            public BlockRemoveKey removeKey(@Nullable Block block) {
                if (!isValid) {
                    return new InvalidBlockTag();
                }

                if (block != null) {
                    TagManager.BLOCK.removeTagKey(tagName, block);
                }
                return new BlockTagRemover(tagName);
            }

            @Override
            public void removeTag() {
                if (!isValid) {
                    return;
                }
                TagManager.BLOCK.removeTag(tagName);
            }
        }
    }

    public static class BlockTagRemover implements BlockRemoveKey, Completed {
        private final String tagName;

        BlockTagRemover(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public BlockRemoveKey removeKey(@Nullable Block block) {
            if (block != null) {
                TagManager.BLOCK.removeTagKey(tagName, block);
            }
            return this;
        }
    }

    public static class BlockTagAdder implements BlockAddable, Completed {
        private final String tagName;

        BlockTagAdder(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public BlockAddable add(@Nullable Block block) {
            if (block != null) {
                TagManager.BLOCK.createTag(tagName, block);
            }
            return this;
        }
    }

    public static class InvalidBlockTag implements BlockAddable, BlockRemoveKey, Completed {
        @Override
        public BlockAddable add(@Nullable Block block) {
            return this;
        }

        @Override
        public BlockRemoveKey removeKey(@Nullable Block block) {
            return this;
        }
    }

    public static abstract class MultiTagBuilder {
        protected final Set<String> tagNames;
        protected final boolean isValid;

        protected MultiTagBuilder(String[] tagNames) {
            Set<String> validNames = validateTagName(tagNames);
            this.isValid = !registrationClosed && !validNames.isEmpty();
            this.tagNames = this.isValid ? validNames : Collections.emptySet();

            if (registrationClosed && !validNames.isEmpty()) {
                logRegistrationClosed();
            }
        }

        public abstract Object initialState();
    }

    public static class MultiItemTagBuilder extends MultiTagBuilder {
        public MultiItemTagBuilder(String... tagNames) {
            super(tagNames);
        }

        public MultiItemInitialState initialState() {
            return new InitialState();
        }

        public class InitialState implements MultiItemInitialState {
            @Override
            public MultiItemAddable add(@Nullable ItemStack stack) {
                if (!isValid) {
                    return new InvalidMultiItemTag();
                }

                var key = ItemKey.toKey(stack);
                if (key != null) {
                    TagManager.ITEM.createTag(tagNames, key);
                }
                return new MultiItemTagAdder(tagNames);
            }

            @Override
            public MultiItemRemoveKey removeKey(@Nullable ItemStack stack) {
                if (!isValid) {
                    return new InvalidMultiItemTag();
                }

                var key = ItemKey.toKey(stack);
                if (key != null) {
                    TagManager.ITEM.removeTagKey(tagNames, key);
                }
                return new MultiItemTagRemover(tagNames);
            }

            @Override
            public MultiItemRemoveKey removeKey(Item item) {
                return removeKey(new ItemStack(item));
            }

            @Override
            public MultiItemRemoveKey removeKey(Item item, int metadata) {
                return removeKey(new ItemStack(item, 1, metadata));
            }

            @Override
            public void removeTag() {
                if (!isValid) {
                    return;
                }
                TagManager.ITEM.removeTag(tagNames);
            }

            @Override
            public MultiItemAddable add(Item item) {
                return add(new ItemStack(item));
            }

            @Override
            public MultiItemAddable add(Item item, int metadata) {
                return add(new ItemStack(item, 1, metadata));
            }
        }
    }

    public static class MultiItemTagRemover implements MultiItemRemoveKey, MultiCompleted {
        private final Set<String> tagNames;

        MultiItemTagRemover(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiItemRemoveKey removeKey(@Nullable ItemStack stack) {
            var key = ItemKey.toKey(stack);
            if (key != null) {
                TagManager.ITEM.removeTagKey(tagNames, key);
            }
            return this;
        }

        @Override
        public MultiItemRemoveKey removeKey(Item item) {
            return removeKey(new ItemStack(item));
        }

        @Override
        public MultiItemRemoveKey removeKey(Item item, int metadata) {
            return removeKey(new ItemStack(item, 1, metadata));
        }
    }

    public static class MultiItemTagAdder implements MultiItemAddable, MultiCompleted {
        private final Set<String> tagNames;

        MultiItemTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiItemAddable add(@Nullable ItemStack stack) {
            var key = ItemKey.toKey(stack);
            if (key != null) {
                TagManager.ITEM.createTag(tagNames, key);
            }
            return this;
        }

        @Override
        public MultiItemAddable add(Item item) {
            return add(new ItemStack(item));
        }

        @Override
        public MultiItemAddable add(Item item, int metadata) {
            return add(new ItemStack(item, 1, metadata));
        }
    }

    public static class MultiFluidTagBuilder extends MultiTagBuilder {
        public MultiFluidTagBuilder(String... tagNames) {
            super(tagNames);
        }

        public MultiFluidInitialState initialState() {
            return new InitialState();
        }

        public class InitialState implements MultiFluidInitialState {
            @Override
            public MultiFluidAddable add(@Nullable FluidStack stack) {
                if (!isValid) {
                    return new InvalidMultiFluidTag();
                }

                if (stack != null) {
                    TagManager.FLUID.createTag(tagNames, stack.getFluid());
                }
                return new MultiFluidTagAdder(tagNames);
            }

            @Override
            public MultiFluidRemoveKey removeKey(@Nullable FluidStack stack) {
                if (!isValid) {
                    return new InvalidMultiFluidTag();
                }

                if (stack != null) {
                    TagManager.FLUID.removeTagKey(tagNames, stack.getFluid());
                }
                return new MultiFluidTagRemover(tagNames);
            }

            @Override
            public MultiFluidRemoveKey removeKey(Fluid fluid) {
                return removeKey(new FluidStack(fluid, 1000));
            }

            @Override
            public void removeTag() {
                if (!isValid) {
                    return;
                }
                TagManager.FLUID.removeTag(tagNames);
            }

            @Override
            public MultiFluidAddable add(Fluid fluid) {
                return add(new FluidStack(fluid, 1000));
            }
        }
    }

    public static class MultiFluidTagRemover implements MultiFluidRemoveKey, MultiCompleted {
        private final Set<String> tagNames;

        MultiFluidTagRemover(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiFluidRemoveKey removeKey(@Nullable FluidStack stack) {
            if (stack != null) {
                TagManager.FLUID.removeTagKey(tagNames, stack.getFluid());
            }
            return this;
        }

        @Override
        public MultiFluidRemoveKey removeKey(Fluid fluid) {
            return removeKey(new FluidStack(fluid, 1000));
        }
    }

    public static class MultiFluidTagAdder implements MultiFluidAddable, MultiCompleted {
        private final Set<String> tagNames;

        MultiFluidTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiFluidAddable add(@Nullable FluidStack stack) {
            if (stack != null) {
                TagManager.FLUID.createTag(tagNames, stack.getFluid());
            }
            return this;
        }

        @Override
        public MultiFluidAddable add(Fluid fluid) {
            return add(new FluidStack(fluid, 1000));
        }
    }

    public static class MultiBlockTagBuilder extends MultiTagBuilder {
        public MultiBlockTagBuilder(String... tagNames) {
            super(tagNames);
        }

        public MultiBlockInitialState initialState() {
            return new InitialState();
        }

        public class InitialState implements MultiBlockInitialState {
            @Override
            public MultiBlockAddable add(@Nullable Block block) {
                if (!isValid) {
                    return new InvalidMultiBlockTag();
                }

                if (block != null) {
                    TagManager.BLOCK.createTag(tagNames, block);
                }
                return new MultiBlockTagAdder(tagNames);
            }

            @Override
            public MultiBlockRemoveKey removeKey(@Nullable Block block) {
                if (!isValid) {
                    return new InvalidMultiBlockTag();
                }

                if (block != null) {
                    TagManager.BLOCK.removeTagKey(tagNames, block);
                }
                return new MultiBlockTagRemover(tagNames);
            }

            @Override
            public void removeTag() {
                if (!isValid) {
                    return;
                }
                TagManager.BLOCK.removeTag(tagNames);
            }
        }
    }

    public static class MultiBlockTagRemover implements MultiBlockRemoveKey, MultiCompleted {
        private final Set<String> tagNames;

        MultiBlockTagRemover(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiBlockRemoveKey removeKey(@Nullable Block block) {
            if (block != null) {
                TagManager.BLOCK.removeTagKey(tagNames, block);
            }
            return this;
        }
    }

    public static class MultiBlockTagAdder implements MultiBlockAddable, MultiCompleted {
        private final Set<String> tagNames;

        MultiBlockTagAdder(Set<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public MultiBlockAddable add(@Nullable Block block) {
            if (block != null) {
                TagManager.BLOCK.createTag(tagNames, block);
            }
            return this;
        }
    }

    public static class InvalidMultiItemTag implements MultiItemAddable, MultiItemRemoveKey, MultiCompleted {
        @Override
        public MultiItemAddable add(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public MultiItemRemoveKey removeKey(@Nullable ItemStack stack) {
            return this;
        }

        @Override
        public MultiItemRemoveKey removeKey(Item item) {
            return this;
        }

        @Override
        public MultiItemRemoveKey removeKey(Item item, int metadata) {
            return this;
        }

        @Override
        public MultiItemAddable add(Item item) {
            return this;
        }

        @Override
        public MultiItemAddable add(Item item, int metadata) {
            return this;
        }
    }

    public static class InvalidMultiFluidTag implements MultiFluidAddable, MultiFluidRemoveKey, MultiCompleted {
        @Override
        public MultiFluidAddable add(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public MultiFluidRemoveKey removeKey(@Nullable FluidStack stack) {
            return this;
        }

        @Override
        public MultiFluidRemoveKey removeKey(Fluid fluid) {
            return this;
        }

        @Override
        public MultiFluidAddable add(Fluid fluid) {
            return this;
        }
    }

    public static class InvalidMultiBlockTag implements MultiBlockAddable, MultiBlockRemoveKey, MultiCompleted {
        @Override
        public MultiBlockAddable add(@Nullable Block block) {
            return this;
        }

        @Override
        public MultiBlockRemoveKey removeKey(@Nullable Block block) {
            return this;
        }
    }
}
