package com.nikrasoff.structure_blocks.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.nikrasoff.structure_blocks.util.FixedTextBoxElement;
import com.nikrasoff.structure_blocks.util.FixedToggleElement;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import dev.crmodders.flux.menus.BasicMenu;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import ru.nern.becraft.bed.api.BlockEntity;

public abstract class BlockMenu extends IngameBackgroundMenu {
    protected BlockMenuCollection collection;
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
