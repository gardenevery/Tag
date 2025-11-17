package com.gardenevery.tag.event;

import com.gardenevery.tag.TagType;
import net.minecraftforge.fluids.FluidStack;

public class FluidTagEvent extends TagEvent {
    private final FluidStack fluidStack;

    private FluidTagEvent(String tagName, FluidStack fluidStack) {
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

    /**
     * Do not use this
     */
    @Deprecated
    public static FluidTagEvent create(String tagName, FluidStack fluidStack) {
        return new FluidTagEvent(tagName, fluidStack);
    }
}
