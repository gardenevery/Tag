package com.gardenevery.tag.event;

import com.gardenevery.tag.TagType;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class TagRemoveEvent extends AbstractTagEvent {
    private TagRemoveEvent(String tagName, TagType type) {
        super(tagName, type);
    }

    /**
     * Do not use this
     */
    @Deprecated
    public static TagRemoveEvent remove(String tagName, TagType type) {
        return new TagRemoveEvent(tagName, type);
    }
}
