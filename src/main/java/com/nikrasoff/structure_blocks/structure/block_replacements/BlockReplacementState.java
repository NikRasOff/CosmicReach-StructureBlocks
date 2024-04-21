package com.nikrasoff.structure_blocks.structure.block_replacements;

import finalforeach.cosmicreach.blocks.BlockState;

public class BlockReplacementState implements BlockReplacement {
    private final String replaceWith;

    public BlockReplacementState(String stateID){
        this.replaceWith = stateID;
    }

    @Override
    public BlockState replace(BlockState input) {
        return BlockState.getInstance(input.getBlockId() + "[" +this.replaceWith + "]");
    }
}
