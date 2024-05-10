package com.nikrasoff.structure_blocks.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.nikrasoff.structure_blocks.menus.elements.StructureSelectButton;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import com.nikrasoff.structure_blocks.menus.elements.ButtonTextBox;
import com.nikrasoff.structure_blocks.menus.elements.FixedTextBoxElement;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import dev.crmodders.flux.menus.ScrollMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureGroupEditMenu extends FixedScrollMenu {
    StructureGroup editedGroup;
    StructureSelectButton addButton;

    public boolean markedForDeletion = false;

    protected StructureGroupEditMenu(StructureGroup group, GameState prevGameState){
        super(prevGameState);
        this.editedGroup = group;

        TextElement groupIDDisplay = new TextElement(group.idString);
        groupIDDisplay.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);
        groupIDDisplay.setBounds(0, 0, 250, 50);
        groupIDDisplay.backgroundEnabled = false;
        this.addFluxElement(groupIDDisplay);

        ButtonElement deleteButton = new ButtonElement(() -> this.markedForDeletion = true);
        deleteButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        deleteButton.text = "Remove Structure";
        deleteButton.setBounds(-130, -10, 250, 50);
        deleteButton.updateText();
        this.addFluxElement(deleteButton);

        this.addButton = new StructureSelectButton(130, -10, 250, 50, HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED, this){
            @Override
            public void selectStructure(Identifier structureID) {
                super.selectStructure(structureID);
                StructureGroupEditMenu editMenu = (StructureGroupEditMenu) this.parentState;
                editMenu.addNewElement();
            }
        };
        this.addButton.changeText = false;
        this.addButton.text = "Add Structure";
        this.addButton.updateText();
        this.addFluxElement(this.addButton);

        ButtonElement saveChangesButton = new ButtonElement(() -> {
            this.editedGroup.saveStructureGroup();
            this.onEscape();
        });
        saveChangesButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);
        saveChangesButton.setBounds(130, 50, 250, 50);
        saveChangesButton.text = "Save Changes";
        saveChangesButton.updateText();
        this.addFluxElement(saveChangesButton);

        ButtonElement cancelButton = new ButtonElement(this::onEscape);
        cancelButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);
        cancelButton.setBounds(-130, 50, 250, 50);
        cancelButton.text = "Cancel";
        cancelButton.updateText();
        this.addFluxElement(cancelButton);

        for (StructureGroup.StructureGroupEntry structure : this.editedGroup.structures){
            StructureDisplay structureEdit = new StructureDisplay(structure);
            this.addScrollElement(structureEdit);
        }
        this.setSelectedIndex(0);
    }

    @Override
    public void drawUIElements() {
        if (this.markedForDeletion){
            this.markedForDeletion = false;
            this.removeCurrentElement();
        }
        super.drawUIElements();
    }

    public void removeCurrentElement(){
        if (this.elements.isEmpty()) return;

        StructureDisplay currentElement = (StructureDisplay) this.elements.get(this.getSelectedIndex());
        if (currentElement == null) return;

        this.editedGroup.structures.removeValue(currentElement.trackedEntry, true);
        this.removeScrollElement(currentElement);
        this.setSelectedIndex(this.getSelectedIndex());
    }

    public void addNewElement(){
        if (this.addButton.structureID == null) return;

        Identifier newIdentifier = this.addButton.structureID;
        if (this.editedGroup.hasStructure(newIdentifier.toString())) return;

        Structure newStructure = Structure.getStructure(newIdentifier);
        if (newStructure == null) return;

        StructureGroup.StructureGroupEntry newEntry = new StructureGroup.StructureGroupEntry(newIdentifier.toString(), 1);
        this.editedGroup.structures.add(newEntry);

        StructureDisplay newDisplay = new StructureDisplay(newEntry);
        this.addScrollElement(newDisplay);

        if (this.getSelectedIndex() < 0) this.setSelectedIndex(0);
    }

    private static class StructureDisplay extends FixedTextBoxElement {
        StructureGroup.StructureGroupEntry trackedEntry;
        public StructureDisplay(StructureGroup.StructureGroupEntry entry){
            super();
            this.trackedEntry = entry;

            this.setContent(String.valueOf(entry.weight));
            this.label = entry.structureID + " | Weight: ";
        }

        @Override
        public void onDeactivate() {
            super.onDeactivate();
            String content = this.getContent();
            if (StructureUtils.isValidInt(content)){
                int setTo = Math.max(1, Integer.parseInt(content));
                this.setContent(String.valueOf(setTo));
                this.trackedEntry.weight = setTo;
            }
            else {
                this.setContent(String.valueOf(this.trackedEntry.weight));
            }
        }
    }
}
