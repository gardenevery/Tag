package com.gardenevery.tag;

import com.gardenevery.tag.key.BlockKey;
import com.gardenevery.tag.key.FluidKey;
import com.gardenevery.tag.key.ItemKey;

final class TagManager {

    private TagManager() {
    }

    public static final Tag<ItemKey> ITEM_TAGS = new Tag<>();
    public static final Tag<FluidKey> FLUID_TAGS = new Tag<>();
    public static final Tag<BlockKey> BLOCK_TAGS = new Tag<>();
}
