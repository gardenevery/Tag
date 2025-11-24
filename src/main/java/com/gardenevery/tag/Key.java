package com.gardenevery.tag;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

interface Key {
    @Desugar
    record ItemKey(@Nonnull Item item, int metadata) implements Key {
        @Nullable
        public static ItemKey from(@Nullable ItemStack stack) {
            if (stack == null || stack.isEmpty()) {
                return null;
            }
            int metadata = stack.getHasSubtypes() ? stack.getMetadata() : 0;
            return new ItemKey(stack.getItem(), metadata);
        }

        @Nonnull
        public ItemStack toStack() {
            return new ItemStack(item, 1, metadata);
        }
    }

    @Desugar
    record FluidKey(@Nonnull Fluid fluid) implements Key {
        @Nullable
        public static FluidKey from(@Nullable FluidStack stack) {
            if (stack == null || stack.getFluid() == null) {
                return null;
            }
            return new FluidKey(stack.getFluid());
        }

        @Nonnull
        public FluidStack toStack() {
            return new FluidStack(fluid, 1000);
        }
    }

    @Desugar
    record BlockKey(@Nonnull Block block) implements Key {
        @Nullable
        public static BlockKey from(@Nullable Block block) {
            if (block == null) {
                return null;
            }
            return new BlockKey(block);
        }

        @Nonnull
        public Block toBlock() {
            return block;
        }
    }

//    @Desugar
//    record ItemKey(@Nonnull ResourceLocation item, int metadata) implements Key {
//        @Nullable
//        public static ItemKey from(@Nullable ItemStack stack) {
//            if (stack == null || stack.isEmpty()) {
//                return null;
//            }
//
//            var item = stack.getItem();
//            var registryName = item.getRegistryName();
//            if (registryName == null) {
//                return null;
//            }
//
//            int metadata = stack.getHasSubtypes() ? stack.getMetadata() : 0;
//            var initialKey = new ItemKey(registryName, metadata);
//            return validateKey(initialKey);
//        }
//
//        @Nonnull
//        public ItemStack toStack() {
//            return new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(item)), 1, metadata);
//        }
//
//        @SuppressWarnings("ConstantConditions")
//        @Nullable
//        private static ItemKey validateKey(@Nonnull ItemKey key) {
//            var convertedStack = new ItemStack(ForgeRegistries.ITEMS.getValue(key.item), 1, key.metadata);
//            var verifiedKey = ItemKey.from(convertedStack);
//
//            if (key.equals(verifiedKey)) {
//                return key;
//            }
//            return null;
//        }
//    }
//
//    @Desugar
//    record FluidKey(@Nonnull String fluid) implements Key {
//        @Nullable
//        public static FluidKey from(@Nullable FluidStack stack) {
//            if (stack == null || stack.getFluid() == null) {
//                return null;
//            }
//
//            var fluidName = FluidRegistry.getFluidName(stack.getFluid());
//            if (fluidName == null) {
//                return null;
//            }
//
//            var initialKey = new FluidKey(fluidName);
//            return validateKey(initialKey);
//        }
//
//        @Nonnull
//        public FluidStack toStack() {
//            var fluidName = fluid();
//            var fluid = FluidRegistry.getFluid(fluidName);
//            return new FluidStack(fluid, 1000);
//        }
//
//        @Nullable
//        private static FluidKey validateKey(@Nonnull FluidKey key) {
//            var fluidName = key.fluid();
//            var fluid = FluidRegistry.getFluid(fluidName);
//            var convertedStack = new FluidStack(fluid, 1000);
//            var verifiedKey = FluidKey.from(convertedStack);
//
//            if (key.equals(verifiedKey)) {
//                return key;
//            }
//            return null;
//        }
//    }
//
//    @Desugar
//    record BlockKey(@Nonnull ResourceLocation block) implements Key {
//        @Nullable
//        public static BlockKey from(@Nullable Block block) {
//            if (block == null) {
//                return null;
//            }
//
//            var registryName = block.getRegistryName();
//            if (registryName == null) {
//                return null;
//            }
//
//            var initialKey = new BlockKey(registryName);
//            return validateKey(initialKey);
//        }
//
//        @Nonnull
//        public Block toBlock() {
//            return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(this.block));
//        }
//
//        @Nullable
//        private static BlockKey validateKey(@Nonnull BlockKey key) {
//            var convertedBlock = ForgeRegistries.BLOCKS.getValue(key.block);
//            var verifiedKey = BlockKey.from(convertedBlock);
//
//            if (key.equals(verifiedKey)) {
//                return key;
//            }
//            return null;
//        }
//    }
}
