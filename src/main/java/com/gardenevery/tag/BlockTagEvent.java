package com.gardenevery.tag;

import net.minecraft.block.Block;

public class BlockTagEvent extends TagEvent {
    private final Block block;

    public BlockTagEvent(String tagName, Block block) {
        super(tagName, TagType.BLOCK);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean isItemEvent() {
        return false;
    }

    @Override
    public boolean isFluidEvent() {
        return false;
    }

    @Override
    public boolean isBlockEvent() {
        return true;
    }

    static BlockTagEvent create(String tagName, Block block) {
        return new BlockTagEvent(tagName, block);
    }
}
