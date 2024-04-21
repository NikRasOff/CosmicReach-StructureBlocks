package com.nikrasoff.structure_blocks.structure.rules;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;

public interface BlockReplaceRule {
    BlockState checkRule(BlockState input, BlockPosition position);
}
