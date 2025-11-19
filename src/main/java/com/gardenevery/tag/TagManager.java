package com.gardenevery.tag;

final class TagManager {

    private TagManager() {
    }

    public static final Tag<Key.ItemKey> ITEM = new Tag<>();
    public static final Tag<Key.FluidKey> FLUID = new Tag<>();
    public static final Tag<Key.BlockKey> BLOCK = new Tag<>();
}
