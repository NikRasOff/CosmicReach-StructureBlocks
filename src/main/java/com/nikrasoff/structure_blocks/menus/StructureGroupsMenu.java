package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.structure.StructureGroup;
import com.nikrasoff.structure_blocks.util.FixedTextBoxElement;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.menus.BasicMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;

public class StructureGroupsMenu extends BasicMenu {
    private FixedTextBoxElement idInput;

    public void switchTo(){
        this.previousState = GameState.currentGameState;
        GameState.switchToGameState(this);
    }

    public StructureGroupsMenu(){
        super();

        this.addBackButton();

        this.idInput = new FixedTextBoxElement();
        this.idInput.setBounds(0, -70, 500, 50);
        this.idInput.label = "";
        this.idInput.setContent("structure_blocks:example_group");
        this.idInput.updateText();
        this.addFluxElement(idInput);

        ButtonElement loadGroup = new ButtonElement(() -> {
            StructureGroup newStructureGroup = StructureGroup.loadStructureGroup(Identifier.fromString(this.idInput.getContent()));
            if (newStructureGroup != null) GameState.switchToGameState(new StructureGroupEditMenu(newStructureGroup, this));
        });
        loadGroup.setBounds(0, 0, 250, 50);
        loadGroup.text = "Load structure group";
        loadGroup.updateText();
        this.addFluxElement(loadGroup);

        ButtonElement newGroup = new ButtonElement(() -> {
            StructureGroup newStructureGroup = new StructureGroup(this.idInput.getContent());
            GameState.switchToGameState(new StructureGroupEditMenu(newStructureGroup, this));
        });
        newGroup.setBounds(0, 60, 250, 50);
        newGroup.text = "New structure group";
        newGroup.updateText();
        this.addFluxElement(newGroup);
    }
}
