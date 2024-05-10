package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.menus.elements.ButtonTextBox;
import com.nikrasoff.structure_blocks.menus.elements.StructureGroupSelectButton;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import com.nikrasoff.structure_blocks.menus.elements.FixedTextBoxElement;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.menus.BasicMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureGroupsMenu extends BasicMenu {
    private ButtonTextBox newGroup;
    public void switchTo(){
        this.previousState = GameState.currentGameState;
        GameState.switchToGameState(this);
    }

    public StructureGroupsMenu(){
        super();

        this.addBackButton();

        StructureGroupSelectButton loadGroup = new StructureGroupSelectButton(0, -30, 250, 50, HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED, this){
            @Override
            public void selectStructureGroup(Identifier groupID) {
                if (groupID == null) {
                    super.selectStructureGroup(null);
                    return;
                }
                super.selectStructureGroup(groupID);
                StructureGroup newStructureGroup = StructureGroup.getStructureGroup(groupID);
                if (newStructureGroup != null) GameState.switchToGameState(FixedScrollMenu.createGroupEditMenu(newStructureGroup, this.parentState));
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
        StructureGroup newStructureGroup = new StructureGroup(this.newGroup.getContent());
        GameState.switchToGameState(FixedScrollMenu.createGroupEditMenu(newStructureGroup, this));
    }
}
