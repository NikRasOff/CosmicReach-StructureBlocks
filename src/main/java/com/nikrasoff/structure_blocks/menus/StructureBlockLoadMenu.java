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
            entity.saveMode = true;
            this.copyToEntity();
            this.switchToSubstate(StructureBlockEntity.STRUCTURE_SAVE_MENU);
            }, "Save structure");
        TextElement saveStructure = this.createLabel(540, 70, 250, 50, ">Load structure<");
        saveStructure.backgroundEnabled = true;



        ButtonElement loadButton = this.createButton(260, -15, 250, 50, this::onLoad, "Load structure");
        loadButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
    }

    @Override
    protected void onApply() {
        this.copyToEntity();
        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        entity.loadStructure();
        this.outputInfo(Structure.output);
    }

    protected void onLoad(){
        this.onApply();
        StructureBlockEntity entity = (StructureBlockEntity) this.reflectedEntity;
        entity.placeStructure();
        this.outputInfo(Structure.output);
    }

}
