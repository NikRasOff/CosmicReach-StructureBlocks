package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.util.FixedToggleElement;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextBoxElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureBlockSaveMenu extends BaseStructureBlockMenu {
    private final TextBoxElement[] sizeInput;
    private final TextBoxElement replaceWithInput;

    private final FixedToggleElement airToVoidInput;

    public StructureBlockSaveMenu(){
        super();

        TextElement saveStructure = this.createLabel(280, 70, 250, 50, ">Save structure<");
        saveStructure.backgroundEnabled = true;
        this.createButton(540, 70, 250, 50, () -> {
            StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
            entity.saveMode = false;
            this.copyToEntity();
            this.switchToSubstate(StructureBlockEntity.STRUCTURE_LOAD_MENU);
            }, "Load structure");

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
        if (StructureUtils.structureExists(structureID)){
            ConfirmWindow window = StructureBlockEntity.CONFIRM_WINDOW;
            window.switchToThisState(this.reflectedEntity, "Structure already exists. Overwrite?", () -> {
                window.onBack();
                this.saveStructure();
                }, window::onBack);
        }
        else {
            this.saveStructure();
        }
    }

    protected void saveStructure(){
        Structure.saveStructure((StructureBlockEntity) this.reflectedEntity);
        this.outputInfo(Structure.output);
    }

    @Override
    protected void copyFromEntity() {
        super.copyFromEntity();
        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        if (entity == null) return;

        this.sizeInput[0].setContent(String.valueOf(entity.size.x));
        this.sizeInput[0].updateText();
        this.sizeInput[1].setContent(String.valueOf(entity.size.y));
        this.sizeInput[1].updateText();
        this.sizeInput[2].setContent(String.valueOf(entity.size.z));
        this.sizeInput[2].updateText();

        this.replaceWithInput.setContent(entity.replaceWith);
        this.replaceWithInput.updateText();
        this.airToVoidInput.value = entity.airToVoid;
        this.airToVoidInput.updateText();
    }

    @Override
    protected void copyToEntity() {
        super.copyToEntity();
        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        if (entity == null) return;

        if (StructureUtils.isValidInt(this.sizeInput[0].getContent())){
            entity.size.x = Integer.parseInt(this.sizeInput[0].getContent());
        }
        else {
            this.outputInfo("Size X: \"" + this.sizeInput[0].getContent() + "\" is not a valid integer");
        }
        if (StructureUtils.isValidInt(this.sizeInput[1].getContent())){
            entity.size.y = Integer.parseInt(this.sizeInput[1].getContent());
        }
        else {
            this.outputInfo("Size Y: \"" + this.sizeInput[1].getContent() + "\" is not a valid integer");
        }
        if (StructureUtils.isValidInt(this.sizeInput[2].getContent())){
            entity.size.z = Integer.parseInt(this.sizeInput[2].getContent());
        }
        else {
            this.outputInfo("Size Z: \"" + this.sizeInput[2].getContent() + "\" is not a valid integer");
        }

        entity.replaceWith = this.replaceWithInput.getContent();
        entity.airToVoid = this.airToVoidInput.value;
    }
}
