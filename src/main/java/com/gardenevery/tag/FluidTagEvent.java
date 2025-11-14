package com.gardenevery.tag;

import net.minecraftforge.fluids.FluidStack;

public class FluidTagEvent extends TagEvent {
    private final FluidStack fluidStack;

    public FluidTagEvent(String tagName, FluidStack fluidStack) {
        super(tagName, TagType.FLUID);
        this.fluidStack = fluidStack != null ? fluidStack.copy() : null;
    }

    public FluidStack getFluidStack() {
        return fluidStack != null ? fluidStack.copy() : null;
    }

    @Override
    public boolean isItemEvent() {
        return false;
    }

    @Override
    public boolean isFluidEvent() {
        return true;
    }

    @Override
    public boolean isBlockEvent() {
        return false;
    }

    static FluidTagEvent create(String tagName, FluidStack fluidStack) {
        return new FluidTagEvent(tagName, fluidStack);
    }
}
