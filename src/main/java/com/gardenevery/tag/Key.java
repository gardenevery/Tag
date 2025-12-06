package com.gardenevery.tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

interface Key {
    @Desugar
    record ItemKey(@Nonnull Item item, int metadata) implements Key {
        @Nullable
        public static ItemKey toKey(@Nullable ItemStack stack) {
            if (stack == null || stack.isEmpty()) {
                return null;
            }
            int metadata = stack.getHasSubtypes() ? stack.getMetadata() : 0;
            return new ItemKey(stack.getItem(), metadata);
        }

        @Nonnull
        public ItemStack toElement() {
            return new ItemStack(item, 1, metadata);
        }
    }

    @Desugar
    record FluidKey(@Nonnull Fluid fluid) implements Key {
        @Nullable
        public static FluidKey toKey(@Nullable FluidStack stack) {
            if (stack == null || stack.getFluid() == null) {
                return null;
            }
            return new FluidKey(stack.getFluid());
        }

        @Nonnull
        public FluidStack toElement() {
            return new FluidStack(fluid, 1000);
        }
    }

    @Desugar
    record BlockKey(@Nonnull Block block) implements Key {
        @Nullable
        public static BlockKey toKey(@Nullable Block block) {
            if (block == null) {
                return null;
            }
            return new BlockKey(block);
        }

        @Nonnull
        public Block toElement() {
            return block;
        }
    }
}
