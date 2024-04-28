package com.nikrasoff.structure_blocks.menus;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.gamestates.GameState;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.utils.BEUtils;

public abstract class BlockEntityMenu<T extends BlockEntity> extends BlockMenu {
    protected T reflectedEntity;

    public BlockEntityMenu(){
        super();
    }

    public void switchToThisState(BlockPosition pos){
        BlockEntity curEntity = BEUtils.getBlockEntity(pos);
        this.reflectedEntity = (T) curEntity;
        this.copyFromEntity();
        super.switchToThisState();
    }

    public void switchFromThisState(GameState to){
        this.reflectedEntity = null;
        super.switchFromThisState(to);
    }

    protected abstract void copyToEntity();
    protected abstract void copyFromEntity();
}
