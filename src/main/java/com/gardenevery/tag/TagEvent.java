package com.gardenevery.tag;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public abstract class TagEvent extends Event {
    private final String tagName;
    private final TagType type;

    protected TagEvent(String tagName, TagType type) {
        this.tagName = tagName;
        this.type = type;
    }

    public String getTagName() {
        return tagName;
    }

    public TagType getType() {
        return type;
    }

    public abstract boolean isItemEvent();

    public abstract boolean isFluidEvent();

    public abstract boolean isBlockEvent();
}

