package com.nikrasoff.structure_blocks.menus.elements;

import com.nikrasoff.structure_blocks.menus.FixedScrollMenu;
import com.nikrasoff.structure_blocks.menus.StructureSelectMenu;
import dev.crmodders.flux.api.v5.gui.base.BaseButton;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureSelectButton extends BaseButton {
    public Identifier structureID;
    public final GameState parentState;

    public boolean changeText = true;

    public StructureSelectButton(float x, float y, float w, float h, HorizontalAnchor hAnchor, VerticalAnchor vAnchor, GameState parentState){
        this.text = "Select structure";
        this.updateText();
        this.setAnchors(hAnchor, vAnchor);
        this.setBounds(x, y, w, h);
        this.parentState = parentState;
    }

    @Override
    public void onMouseReleased() {
        super.onMouseReleased();
        GameState.switchToGameState(FixedScrollMenu.createStructureSelectMenu(this.structureID, this));
    }

    public void selectStructure(Identifier structureID){
        this.structureID = structureID;
        if (this.changeText){
            this.text = structureID.toString();
            this.updateText();
        }
        GameState.switchToGameState(this.parentState);
    }
}
