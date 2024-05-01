package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextBoxElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public abstract class BaseStructureBlockMenu extends BlockEntityMenu<StructureBlockEntity> {
    protected TextElement blockOutputLabel;

    private final TextBoxElement[] offsetInput;
    public final TextBoxElement structureIDInput;

    public BaseStructureBlockMenu(){
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

    @Override
    protected void copyFromEntity() {
        if (this.reflectedEntity == null) return;

        this.offsetInput[0].setContent(String.valueOf(this.reflectedEntity.offset.x));
        this.offsetInput[0].updateText();
        this.offsetInput[1].setContent(String.valueOf(this.reflectedEntity.offset.y));
        this.offsetInput[1].updateText();
        this.offsetInput[2].setContent(String.valueOf(this.reflectedEntity.offset.z));
        this.offsetInput[2].updateText();

        this.structureIDInput.setContent(this.reflectedEntity.structureId);
        this.structureIDInput.updateText();
    }

    @Override
    protected void copyToEntity() {
        if (this.reflectedEntity == null) return;

        if (StructureUtils.isValidInt(this.offsetInput[0].getContent())){
            this.reflectedEntity.offset.x = Integer.parseInt(this.offsetInput[0].getContent());
        }
        if (StructureUtils.isValidInt(this.offsetInput[1].getContent())){
            this.reflectedEntity.offset.y = Integer.parseInt(this.offsetInput[1].getContent());
        }
        if (StructureUtils.isValidInt(this.offsetInput[2].getContent())){
            this.reflectedEntity.offset.z = Integer.parseInt(this.offsetInput[2].getContent());
        }

        this.reflectedEntity.structureId = this.structureIDInput.getContent();
    }
}
