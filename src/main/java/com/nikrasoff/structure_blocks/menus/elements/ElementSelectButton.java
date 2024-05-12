package com.nikrasoff.structure_blocks.menus.elements;

import com.nikrasoff.structure_blocks.menus.ElementSelectMenu;
import com.nikrasoff.structure_blocks.menus.FixedScrollMenu;
import com.nikrasoff.structure_blocks.structure.SearchElement;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.api.v5.gui.base.BaseButton;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

import java.util.Map;

public class ElementSelectButton<T extends SearchElement> extends BaseButton {
    public Identifier elementID;
    public final GameState parentState;
    private final Map<Identifier, T> elementMap;

    public boolean changeText = true;

    public ElementSelectButton(float x, float y, float w, float h, HorizontalAnchor hAnchor, VerticalAnchor vAnchor, GameState parentState, Map<Identifier, T> elementMap){
        this.text = "Select structure";
        this.elementMap = elementMap;
        this.updateText();
        this.setAnchors(hAnchor, vAnchor);
        this.setBounds(x, y, w, h);
        this.parentState = parentState;
    }

    @Override
    public void onMouseReleased() {
        super.onMouseReleased();
        GameState.switchToGameState(new ElementSelectMenu<>(this.elementID, this, this.elementMap));
    }

    public void selectElement(Identifier elementID){
        this.elementID = elementID;
        if (this.changeText){
            this.text = elementID.toString();
            this.updateText();
        }
        GameState.switchToGameState(this.parentState);
    }
}
