package com.gardenevery.tag.key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

@Desugar
public record FluidKey(Fluid fluid) implements Key {
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
