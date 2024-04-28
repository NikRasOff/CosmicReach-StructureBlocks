package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureBlockLoadMenu extends BaseStructureBlockMenu {
    public StructureBlockLoadMenu(){
        super();

        this.createButton(280, 70, 250, 50, () -> {
            StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
            entity.structureBlockMode = 0;
            this.copyToEntity();
            this.collection.switchToMenu("save");
            }, "Load structure");

        ButtonElement loadButton = this.createButton(260, -15, 250, 50, this::onLoad, "Load structure");
        loadButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
    }

    @Override
    protected void onApply() {
        this.copyToEntity();
        this.reflectedEntity.loadStructure();
        this.outputInfo(Structure.output);
    }

    protected void onLoad(){
        this.onApply();
        this.reflectedEntity.placeStructure();
        this.outputInfo(Structure.output);
    }

}
