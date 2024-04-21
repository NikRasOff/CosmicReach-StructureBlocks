package com.nikrasoff.structure_blocks.structure.rules;

import com.badlogic.gdx.utils.Array;
import com.nikrasoff.structure_blocks.structure.block_replacements.BlockReplacement;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;

public class BlockReplaceRuleBlock implements BlockReplaceRule{
    private final Array<Block> replaceBlocks = new Array<>();
    private final BlockReplacement replaceWith;

    public BlockReplaceRuleBlock(BlockReplacement replaceWith, Block... replaceBlocks){
        this.replaceWith = replaceWith;
        this.replaceBlocks.addAll(replaceBlocks);
    }

    @Override
    public BlockState checkRule(BlockState input, BlockPosition position) {
        if (this.replaceBlocks.contains(input.getBlock(), true)) return this.replaceWith.replace(input);
        return input;
    }
}
