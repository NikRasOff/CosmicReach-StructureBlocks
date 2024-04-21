package com.nikrasoff.structure_blocks.structure.block_replacements;

import finalforeach.cosmicreach.blocks.BlockState;

public class BlockReplacementFull implements BlockReplacement{
    private final BlockState replaceWith;

    public BlockReplacementFull(BlockState replaceWith){
        this.replaceWith = replaceWith;
    }

    @Override
    public BlockState replace(BlockState input) {
        return replaceWith;
    }
}
