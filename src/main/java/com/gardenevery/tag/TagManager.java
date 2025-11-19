package com.gardenevery.tag;

final class TagManager {

    private TagManager() {
    }

    public static final Tag<Key.ItemKey> ITEM_TAGS = new Tag<>();
    public static final Tag<Key.FluidKey> FLUID_TAGS = new Tag<>();
    public static final Tag<Key.BlockKey> BLOCK_TAGS = new Tag<>();
}
