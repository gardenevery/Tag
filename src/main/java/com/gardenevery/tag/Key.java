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
    record ItemKey(Item item, int metadata) implements Key {
        @Nullable
        public static ItemKey from(@Nullable ItemStack stack) {
            if (stack == null || stack.isEmpty()) {
                return null;
            }
            int metadata = stack.getHasSubtypes() ? stack.getMetadata() : 0;
            return new ItemKey(stack.getItem(), metadata);
        }

        @Nonnull
        public ItemStack stack() {
            return new ItemStack(item, 1, metadata);
        }
    }

    @Desugar
    record FluidKey(Fluid fluid) implements Key {
        @Nullable
        public static FluidKey from(@Nullable FluidStack stack) {
            if (stack == null || stack.getFluid() == null) {
                return null;
            }
            return new FluidKey(stack.getFluid());
        }

        @Nonnull
        public FluidStack stack() {
            return new FluidStack(fluid, 1000);
        }
    }

    @Desugar
    record BlockKey(Block block) implements Key {
        @Nullable
        public static BlockKey from(@Nullable Block block) {
            if (block == null) {
                return null;
            }
            return new BlockKey(block);
        }
    }
}
