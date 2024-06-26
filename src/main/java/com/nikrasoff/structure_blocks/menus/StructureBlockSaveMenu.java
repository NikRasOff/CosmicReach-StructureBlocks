package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.menus.elements.FixedToggleElement;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextBoxElement;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureBlockSaveMenu extends BaseStructureBlockMenu {
    private final TextBoxElement[] sizeInput;
    private final TextBoxElement replaceWithInput;

    private final FixedToggleElement airToVoidInput;

    public final TextBoxElement structureIDInput;

    public StructureBlockSaveMenu(){
        super();

        this.createLabel(15, 130, 250, 50, "Structure name:");
        this.structureIDInput = this.createTextInput(280, 130, 500, 50, "", "default");

        this.createButton(280, 70, 250, 50, () -> {
            StructureBlockEntity entity = this.reflectedEntity;
            entity.structureBlockMode = 1;
            this.copyToEntity();
            this.collection.switchToMenu("load");
            }, "Save structure");

        this.createLabel(15, 250, 250, 50, "Structure size:");
        this.sizeInput = this.createVectorInput(280, 250);

        this.createLabel(15, 310, 250, 50, "Replace with:");
        this.replaceWithInput = this.createTextInput(280, 310, 500, 50, "", "base:cheese[default]");

        this.createLabel(15, 370, 250, 50, "Convert air to void?");
        this.airToVoidInput = this.createToggle(280, 370, 250, 50, false);

        ButtonElement saveButton = this.createButton(260, -15, 250, 50, this::onSave, "Save structure");
        saveButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
    }

    @Override
    protected void onSave() {
        this.copyToEntity();
        Identifier structureID = Identifier.fromString(this.structureIDInput.getContent());
        if (Structure.structureExists(structureID)){
            ConfirmWindow window = StructureBlocks.CONFIRM_WINDOW;
            window.switchToThisState("Structure already exists. Overwrite?", () -> {
                window.closeConfirmWindow();
                this.saveStructure();
                }, window::closeConfirmWindow);
        }
        else {
            this.saveStructure();
        }
    }

    protected void saveStructure(){
        Structure.saveStructure(this.reflectedEntity);
    }

    @Override
    protected void copyFromEntity() {
        super.copyFromEntity();
        if (this.reflectedEntity == null) return;

        this.sizeInput[0].setContent(String.valueOf(this.reflectedEntity.size.x));
        this.sizeInput[0].updateText();
        this.sizeInput[1].setContent(String.valueOf(this.reflectedEntity.size.y));
        this.sizeInput[1].updateText();
        this.sizeInput[2].setContent(String.valueOf(this.reflectedEntity.size.z));
        this.sizeInput[2].updateText();

        this.replaceWithInput.setContent(this.reflectedEntity.replaceWith);
        this.replaceWithInput.updateText();
        this.airToVoidInput.value = this.reflectedEntity.airToVoid;
        this.airToVoidInput.updateText();
        this.structureIDInput.setContent(this.reflectedEntity.structureId);
        this.structureIDInput.updateText();
    }

    @Override
    protected void copyToEntity() {
        super.copyToEntity();
        if (this.reflectedEntity == null) return;

        if (StructureUtils.isValidInt(this.sizeInput[0].getContent())){
            this.reflectedEntity.size.x = Integer.parseInt(this.sizeInput[0].getContent());
        }
        if (StructureUtils.isValidInt(this.sizeInput[1].getContent())){
            this.reflectedEntity.size.y = Integer.parseInt(this.sizeInput[1].getContent());
        }
        if (StructureUtils.isValidInt(this.sizeInput[2].getContent())){
            this.reflectedEntity.size.z = Integer.parseInt(this.sizeInput[2].getContent());
        }

        this.reflectedEntity.replaceWith = this.replaceWithInput.getContent();
        this.reflectedEntity.airToVoid = this.airToVoidInput.value;
        this.reflectedEntity.structureId = this.structureIDInput.getContent();
    }
}
