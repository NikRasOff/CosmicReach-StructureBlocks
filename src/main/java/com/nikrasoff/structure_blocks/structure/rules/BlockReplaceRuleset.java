package com.nikrasoff.structure_blocks.structure.rules;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;

public class BlockReplaceRuleset {
    private final Array<BlockReplaceRule> rules = new Array<>();

    public BlockReplaceRuleset(){}

    public void add(BlockReplaceRule rule){
        this.rules.add(rule);
    }

    public BlockState checkRules(BlockState input, BlockPosition position){
        BlockState output = input;
        for (BlockReplaceRule rule : this.rules){
            output = rule.checkRule(output, position);
        }
        return output;
    }
}
