package com.gardenevery.tag;

import com.gardenevery.tag.Key.BlockKey;
import com.gardenevery.tag.Key.FluidKey;
import com.gardenevery.tag.Key.ItemKey;

final class TagManager {

    private TagManager() {}

    public static final Tag<ItemKey> ITEM = new Tag<>();
    public static final Tag<FluidKey> FLUID = new Tag<>();
    public static final Tag<BlockKey> BLOCK = new Tag<>();
}
