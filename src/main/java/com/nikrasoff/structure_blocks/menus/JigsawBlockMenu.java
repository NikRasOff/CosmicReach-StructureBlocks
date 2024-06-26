package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.block_entities.JigsawBlockEntity;
import com.nikrasoff.structure_blocks.menus.elements.BetterIntSlider;
import com.nikrasoff.structure_blocks.menus.elements.ElementSelectButton;
import com.nikrasoff.structure_blocks.menus.elements.FixedTextBoxElement;
import com.nikrasoff.structure_blocks.menus.elements.FixedToggleElement;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class JigsawBlockMenu extends BlockEntityMenu<JigsawBlockEntity> {
    FixedTextBoxElement replaceWith;
    FixedTextBoxElement blockName;
    ElementSelectButton<StructureGroup> structureGroupID;
    FixedTextBoxElement processPriority;
    FixedTextBoxElement attachmentPriority;
    BetterIntSlider chainLength;
    FixedToggleElement allowIntersectionsToggle;

    public JigsawBlockMenu(){
        super();

        TextElement blockLabel = this.createLabel(0, 15, 250, 50, "Jigsaw block");
        blockLabel.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);

        this.blockName = this.createTextInput(0, 70, 700, 50, "Name: ", "");
        this.blockName.hAnchor = HorizontalAnchor.CENTERED;

        this.structureGroupID = new ElementSelectButton<>(0, 130, 700, 50, HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED, this, StructureGroup.ALL_STRUCTURE_GROUPS){
            @Override
            public void onMouseReleased() {
                if (FixedTextBoxElement.currentTextBoxElement != null) FixedTextBoxElement.currentTextBoxElement.deactivate();
                super.onMouseReleased();
            }
        };
        this.addFluxElement(this.structureGroupID);

        this.replaceWith = this.createTextInput(0, 190, 700, 50, "Replace with: ", "");
        this.replaceWith.hAnchor = HorizontalAnchor.CENTERED;

        this.processPriority = this.createTextInput(-177.5F, 250, 345, 50, "Process priority: ", "");
        this.processPriority.hAnchor = HorizontalAnchor.CENTERED;
        this.attachmentPriority = this.createTextInput(177.5F, 250, 345, 50, "Attachment priority: ", "");
        this.attachmentPriority.hAnchor = HorizontalAnchor.CENTERED;
        this.chainLength = this.createIntSlider(-177.5F, 310, 345, 50, 1, 20, 1, "Chain Length: ");
        this.chainLength.hAnchor = HorizontalAnchor.CENTERED;
        this.allowIntersectionsToggle = this.createToggle(177.5F, 310, 345, 50, false);
        this.allowIntersectionsToggle.hAnchor = HorizontalAnchor.CENTERED;
        this.allowIntersectionsToggle.label = "Allow intersections? ";
        this.allowIntersectionsToggle.updateText();


        ButtonElement backButton = this.createButton(-150, -15, 250, 50, this::onBack, "Back");
        backButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);

        ButtonElement applyButton = this.createButton(150, -15, 250, 50, this::onSave, "Save changes");
        applyButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
    }

    @Override
    protected void onSave() {
        this.copyToEntity();
        super.onSave();
    }

    @Override
    protected void copyToEntity() {
        if (this.reflectedEntity == null) return;

        this.reflectedEntity.name = this.blockName.getContent();
        if (this.structureGroupID.elementID != null){
            this.reflectedEntity.structureGroupID = this.structureGroupID.elementID.toString();
        }
        this.reflectedEntity.replaceWith = this.replaceWith.getContent();

        if (StructureUtils.isValidInt(this.processPriority.getContent())){
            this.reflectedEntity.processPriority = Integer.parseInt(this.processPriority.getContent());
        }
        if (StructureUtils.isValidInt(this.attachmentPriority.getContent())){
            this.reflectedEntity.attachmentPriority = Integer.parseInt(this.attachmentPriority.getContent());
        }

        this.reflectedEntity.chainLength = this.chainLength.getValue();
        this.reflectedEntity.allowIntersections = this.allowIntersectionsToggle.value;
    }

    @Override
    protected void copyFromEntity() {
        if (this.reflectedEntity == null) return;

        this.blockName.setContent(this.reflectedEntity.name);
        this.blockName.updateText();

        if (!this.reflectedEntity.structureGroupID.isEmpty()){
            this.structureGroupID.elementID = Identifier.fromString(this.reflectedEntity.structureGroupID);
            this.structureGroupID.text = this.reflectedEntity.structureGroupID;
            this.structureGroupID.updateText();
        }
        else {
            this.structureGroupID.elementID = null;
            this.structureGroupID.text = "Select structure group";
            this.structureGroupID.updateText();
        }

        this.replaceWith.setContent(this.reflectedEntity.replaceWith);
        this.replaceWith.updateText();

        this.processPriority.setContent(String.valueOf(this.reflectedEntity.processPriority));
        this.processPriority.updateText();

        this.attachmentPriority.setContent(String.valueOf(this.reflectedEntity.attachmentPriority));
        this.attachmentPriority.updateText();

        this.chainLength.setValue(this.reflectedEntity.chainLength);
        this.chainLength.updateText();

        this.allowIntersectionsToggle.value = this.reflectedEntity.allowIntersections;
        this.allowIntersectionsToggle.updateText();
    }
}
