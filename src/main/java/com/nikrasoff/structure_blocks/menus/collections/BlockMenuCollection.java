package com.nikrasoff.structure_blocks.menus.collections;

import com.badlogic.gdx.utils.OrderedMap;
import com.nikrasoff.structure_blocks.menus.BlockMenu;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.gamestates.GameState;
import ru.nern.becraft.bed.api.BlockEntity;

public abstract class BlockMenuCollection {
    public final OrderedMap<String, BlockMenu> blockMenus = new OrderedMap<>();
    private BlockMenu currentMenu;
    private GameState previousState;
    protected BlockPosition blockPosition;
    private boolean active = false;

    public BlockMenuCollection(){
    }

    public void addMenu(String key, BlockMenu menu){
        menu.collection = this;
        this.blockMenus.put(key, menu);
    }

    public void activate(BlockPosition pos){
        if (this.active) return;
        this.blockPosition = pos;
        this.previousState = GameState.currentGameState;
        if (this.currentMenu == null) this.switchToMenuByIndex(0);
        this.active = true;
        this.currentMenu.switchToThisState(this.blockPosition);
    }

    public void deactivate(){
        if (!this.active) return;
        this.blockPosition = null;
        this.active = false;
        this.currentMenu.switchFromThisState(this.previousState);
    }
    public void switchToMenu(String menuID){
        if (!this.active) {
            this.currentMenu = blockMenus.get(menuID);
            return;
        }
        this.currentMenu.switchFromThisState(this.previousState);
        this.currentMenu = blockMenus.get(menuID);
        this.currentMenu.switchToThisState(this.blockPosition);
    }

    public void switchToMenuByIndex(int index){
        this.switchToMenu(this.blockMenus.orderedKeys().get(index));
    }

}
