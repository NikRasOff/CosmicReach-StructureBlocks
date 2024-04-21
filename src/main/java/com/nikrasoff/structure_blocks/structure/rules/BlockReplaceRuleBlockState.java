package com.nikrasoff.structure_blocks.structure.rules;

import com.badlogic.gdx.utils.Array;
import com.nikrasoff.structure_blocks.structure.block_replacements.BlockReplacement;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.Arrays;

public class BlockReplaceRuleBlockState implements BlockReplaceRule {
    private final Array<BlockState> blockStates = new Array<>();
    private final BlockReplacement replaceWith;

    public BlockReplaceRuleBlockState(BlockReplacement replaceWith, BlockState... replaceStates){
        this.replaceWith = replaceWith;
        blockStates.addAll(replaceStates);
    }

    @Override
    public BlockState checkRule(BlockState input, BlockPosition position) {
        if (blockStates.contains(input, true)) return this.replaceWith.replace(input);
        return input;
    }
}
