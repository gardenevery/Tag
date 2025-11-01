package com.gardenevery.tag.key;

import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.block.Block;

@Desugar
public record BlockKey(Block block) implements Key {
    @Nullable
    public static BlockKey from(@Nullable Block block) {
        if (block == null) {
            return null;
        }
        return new BlockKey(block);
    }
}
