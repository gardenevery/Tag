package com.gardenevery.tag;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.BlockStateKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

public final class TagManager {

    private static final TagManager INSTANCE = new TagManager();

    private TagManager() {
    }

    static TagManager instance() {
        return INSTANCE;
    }

    final Tag<ItemKey> itemTags = new Tag<>();
    final Tag<FluidKey> fluidTags = new Tag<>();
    final Tag<BlockKey> blockTags = new Tag<>();
    final Tag<BlockStateKey> blockStateTags = new Tag<>();
}
