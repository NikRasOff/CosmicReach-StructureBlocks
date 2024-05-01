package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.menus.collections.BlockMenuCollection;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.gamestates.GameState;

public abstract class BlockMenu extends IngameBackgroundMenu {
    public BlockMenuCollection collection;
    protected BlockPosition blockPosition;

    public BlockMenu(){
        super();
    }

    public void switchToThisState(BlockPosition pos){
        this.blockPosition = pos;
        super.switchToThisState();
    }

    public void switchFromThisState(GameState to){
        this.blockPosition = null;
        super.switchFromThisState(to);
    }

    @Override
    protected void onEscape() {
        this.collection.deactivate();
    }

    @Override
    protected void onBack() {
        this.collection.deactivate();
    }

    @Override
    protected void onSave() {
        this.collection.deactivate();
    }
}
