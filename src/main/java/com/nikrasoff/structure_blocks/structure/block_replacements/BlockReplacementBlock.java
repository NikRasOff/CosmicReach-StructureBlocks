package com.nikrasoff.structure_blocks.structure.block_replacements;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;

public class BlockReplacementBlock implements BlockReplacement {
    private final Block replaceWith;

    public BlockReplacementBlock(Block replaceWith){
        this.replaceWith = replaceWith;
    }

    @Override
    public BlockState replace(BlockState input) {
        String result = this.replaceWith.getStringId() + "[" + input.stringId + "]";
        return BlockState.getInstance(result);
    }
}
