package com.gardenevery.tag.key;

import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.block.state.IBlockState;

@Desugar
public record BlockStateKey(IBlockState blockState) implements Key{
    @Nullable
    public static BlockStateKey from(IBlockState blockState) {
        if (blockState == null) {
            return null;
        }
        return new BlockStateKey(blockState);
    }
}
