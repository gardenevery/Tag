package com.gardenevery.tag.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import com.gardenevery.tag.TagType;

@Cancelable
public abstract class TagEvent extends AbstractTagEvent {
    protected TagEvent(String tagName, TagType type) {
        super(tagName, type);
    }
}
