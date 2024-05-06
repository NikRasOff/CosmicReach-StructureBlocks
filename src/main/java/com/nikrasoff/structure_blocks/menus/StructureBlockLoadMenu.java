package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.menus.elements.FixedToggleElement;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureBlockLoadMenu extends BaseStructureBlockMenu {
    FixedToggleElement processJigsaw;
    public StructureBlockLoadMenu(){
        super();

        this.createButton(280, 70, 250, 50, () -> {
            StructureBlockEntity entity = this.reflectedEntity;
            entity.structureBlockMode = 0;
            this.copyToEntity();
            this.collection.switchToMenu("save");
            }, "Load structure");

        ButtonElement loadButton = this.createButton(260, -15, 250, 50, this::onLoad, "Load structure");
        loadButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);

        this.createLabel(15, 250, 250, 50, "Process jigsaw blocks?");
        this.processJigsaw = this.createToggle(280, 250, 250, 50, true);
    }

    @Override
    protected void onApply() {
        this.copyToEntity();
        this.reflectedEntity.loadStructure();
    }

    protected void onLoad(){
        this.onApply();
        this.reflectedEntity.placeStructure();
    }

    @Override
    protected void copyFromEntity() {
        super.copyFromEntity();
        if (this.reflectedEntity == null) return;
        this.processJigsaw.value = this.reflectedEntity.processJigsawBlocks;
        this.processJigsaw.updateText();
    }

    @Override
    protected void copyToEntity() {
        super.copyToEntity();
        if (this.reflectedEntity == null) return;
        this.reflectedEntity.processJigsawBlocks = this.processJigsaw.value;
    }
}
