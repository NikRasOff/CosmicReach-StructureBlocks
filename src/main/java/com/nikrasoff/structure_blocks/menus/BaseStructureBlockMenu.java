package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextBoxElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public abstract class BaseStructureBlockMenu extends BlockMenu {
    protected TextElement blockOutputLabel;
    protected TextElement blockOutput;

    private final TextBoxElement[] offsetInput;
    public final TextBoxElement structureIDInput;

    public BaseStructureBlockMenu(){
        this.blockOutputLabel = this.createLabel(15, -130, 250, 50, "Structure Block output: ");
        this.blockOutputLabel.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.BOTTOM_ALIGNED);

        this.blockOutput = this.createLabel(280, -130, 500, 50, "");
        this.blockOutput.backgroundEnabled = true;
        this.blockOutput.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.BOTTOM_ALIGNED);

        TextElement blockLabel = this.createLabel(0, 15, 250, 50, "Structure block");
        blockLabel.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);

        this.createLabel(15, 70, 250, 50, "Structure Block Mode: ");

        this.createLabel(15, 130, 250, 50, "Structure name:");
        this.structureIDInput = this.createTextInput(280, 130, 500, 50, "", "default");

        this.createLabel(15, 190, 250, 50, "Structure offset:");
        this.offsetInput = this.createVectorInput(280, 190);

        ButtonElement backButton = this.createButton(-260, -15, 250, 50, this::onBack, "Back");
        backButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);

        ButtonElement applyButton = this.createButton(0, -15, 250, 50, this::onApply, "Apply");
        applyButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);

    }

    protected void onApply(){
        this.copyToEntity();
    }

    public void outputInfo(String info){
        this.blockOutput.text = info;
        this.blockOutput.updateText();

        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        entity.structureOutput = info;
    }

    @Override
    protected void copyFromEntity() {
        super.copyFromEntity();
        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        if (entity == null) return;

        this.outputInfo(entity.structureOutput);

        this.offsetInput[0].setContent(String.valueOf(entity.offset.x));
        this.offsetInput[0].updateText();
        this.offsetInput[1].setContent(String.valueOf(entity.offset.y));
        this.offsetInput[1].updateText();
        this.offsetInput[2].setContent(String.valueOf(entity.offset.z));
        this.offsetInput[2].updateText();

        this.structureIDInput.setContent(entity.structureId);
        this.structureIDInput.updateText();
    }

    @Override
    protected void copyToEntity() {
        super.copyToEntity();
        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        if (entity == null) return;

        if (StructureUtils.isValidInt(this.offsetInput[0].getContent())){
            entity.offset.x = Integer.parseInt(this.offsetInput[0].getContent());
        }
        else {
            System.out.println(this.offsetInput[0].getContent());
            this.outputInfo("Offset X: \"" + this.offsetInput[0].getContent() + "\" is not a valid integer");
        }
        if (StructureUtils.isValidInt(this.offsetInput[1].getContent())){
            entity.offset.y = Integer.parseInt(this.offsetInput[1].getContent());
        }
        else {
            this.outputInfo("Offset Y: \"" + this.offsetInput[1].getContent() + "\" is not a valid integer");
        }
        if (StructureUtils.isValidInt(this.offsetInput[2].getContent())){
            entity.offset.z = Integer.parseInt(this.offsetInput[2].getContent());
        }
        else {
            this.outputInfo("Offset Z: \"" + this.offsetInput[2].getContent() + "\" is not a valid integer");
        }

        entity.structureId = this.structureIDInput.getContent();
    }
}
