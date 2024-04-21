package com.nikrasoff.structure_blocks.structure.rules;

import com.nikrasoff.structure_blocks.structure.block_replacements.BlockReplacement;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.Arrays;

public class BlockReplaceRulePosition implements BlockReplaceRule {
    private final BlockPosition[] replacePositions;
    private final BlockReplacement replaceWith;

    public BlockReplaceRulePosition(BlockReplacement replaceWith, BlockPosition... replacePositions){
        this.replaceWith = replaceWith;
        this.replacePositions = replacePositions;
    }

    @Override
    public BlockState checkRule(BlockState input, BlockPosition position) {
        if (Arrays.asList(this.replacePositions).contains(position)) return this.replaceWith.replace(input);
        return input;
    }
}
