package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import com.nikrasoff.structure_blocks.util.ButtonTextBox;
import com.nikrasoff.structure_blocks.util.FixedTextBoxElement;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.menus.ScrollMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureGroupEditMenu extends ScrollMenu {
    StructureGroup editedGroup;

    ButtonTextBox addButton;

    public boolean markedForDeletion = false;

    public StructureGroupEditMenu(StructureGroup group, GameState prevGameState){
        super(prevGameState);
        this.editedGroup = group;

        ButtonElement deleteButton = new ButtonElement(() -> this.markedForDeletion = true);
        deleteButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        deleteButton.text = "Remove Structure";
        deleteButton.setBounds(-255, -10, 250, 50);
        deleteButton.updateText();
        this.addFluxElement(deleteButton);

        this.addButton = new ButtonTextBox(this::addNewElement);
        this.addButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        this.addButton.setBounds(130, -10, 500, 50);
        this.addButton.buttonLabel = "Add Structure";
        this.addButton.label = "Structure ID: ";
        this.addButton.setContent("");
        this.addButton.updateText();
        this.addFluxElement(this.addButton);

        ButtonElement saveChangesButton = new ButtonElement(() -> {
            this.editedGroup.saveStructureGroup();
            this.onEscape();
        });
        saveChangesButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);
        saveChangesButton.setBounds(130, 10, 250, 50);
        saveChangesButton.text = "Save Changes";
        saveChangesButton.updateText();
        this.addFluxElement(saveChangesButton);

        ButtonElement cancelButton = new ButtonElement(this::onEscape);
        cancelButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);
        cancelButton.setBounds(-130, 10, 250, 50);
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
        if (this.addButton.getContent().isEmpty()) return;

        Identifier newIdentifier = Identifier.fromString(this.addButton.getContent());
        if (this.editedGroup.hasStructure(newIdentifier)) return;

        Structure newStructure = Structure.loadStructure(newIdentifier);
        if (newStructure == null) return;

        StructureGroup.StructureGroupEntry newEntry = new StructureGroup.StructureGroupEntry(newStructure, 1);
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
            this.label = entry.structure.id.toString() + " | Weight: ";
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
