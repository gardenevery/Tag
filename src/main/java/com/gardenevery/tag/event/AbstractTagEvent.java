package com.gardenevery.tag.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import com.gardenevery.tag.TagType;

@Cancelable
public abstract class AbstractTagEvent extends Event {
    private final String tagName;
    private final TagType type;

    protected AbstractTagEvent(String tagName, TagType type) {
        this.tagName = tagName;
        this.type = type;
    }

    public String getTagName() {
        return tagName;
    }

    public TagType getType() {
        return type;
    }

    public boolean isItemEvent() {
        return type == TagType.ITEM;
    }

    public boolean isFluidEvent() {
        return type == TagType.FLUID;
    }

    public boolean isBlockEvent() {
        return type == TagType.BLOCK;
    }
}
