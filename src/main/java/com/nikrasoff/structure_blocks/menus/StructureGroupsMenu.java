package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.menus.elements.ButtonTextBox;
import com.nikrasoff.structure_blocks.menus.elements.ElementSelectButton;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import dev.crmodders.flux.menus.BasicMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureGroupsMenu extends BasicMenu {
    private final ButtonTextBox newGroup;
    public void switchTo(){
        this.previousState = GameState.currentGameState;
        GameState.switchToGameState(this);
    }

    public StructureGroupsMenu(){
        super();

        this.addBackButton();

        ElementSelectButton<StructureGroup> loadGroup = new ElementSelectButton<>(0, -30, 250, 50, HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED, this, StructureGroup.ALL_STRUCTURE_GROUPS){
            @Override
            public void selectElement(Identifier groupID) {
                if (groupID == null) {
                    super.selectElement(null);
                    return;
                }
                super.selectElement(groupID);
                StructureGroup newStructureGroup = StructureGroup.getStructureGroup(groupID);
                if (newStructureGroup != null) GameState.switchToGameState(new StructureGroupEditMenu(newStructureGroup, this.parentState));
            }
        };
        loadGroup.text = "Load structure group";
        loadGroup.updateText();
        loadGroup.changeText = false;
        this.addFluxElement(loadGroup);

        this.newGroup = new ButtonTextBox(this::createNewGroup);
        newGroup.setBounds(0, 30, 500, 50);
        newGroup.buttonLabel = "New structure group";
        newGroup.label = "StructureID: ";
        newGroup.updateText();
        this.addFluxElement(newGroup);
    }

    private void createNewGroup(){
        if (this.newGroup.getContent().isEmpty()) return;
        StructureGroup newStructureGroup = new StructureGroup(this.newGroup.getContent());
        GameState.switchToGameState(new StructureGroupEditMenu(newStructureGroup, this));
    }
}
