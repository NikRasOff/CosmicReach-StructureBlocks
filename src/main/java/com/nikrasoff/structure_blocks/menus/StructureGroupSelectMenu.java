package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.menus.elements.StructureGroupSelectButton;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import dev.crmodders.flux.FluxConstants;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.base.BaseText;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureGroupSelectMenu extends FixedScrollMenu {
    private StructureGroupSelectButton parentButton;

    protected StructureGroupSelectMenu(Identifier selected, StructureGroupSelectButton parentButton){
        this.parentButton = parentButton;
        this.previousState = parentButton.parentState;
        int currentIndex = 0;
        for (Identifier id : StructureGroup.ALL_STRUCTURE_GROUPS.keySet()){
            BaseText newText = new BaseText();
            newText.text = id.toString();
            newText.backgroundEnabled = false;
            newText.updateText();
            this.addScrollElement(newText);
            if (id.equals(selected)) this.setSelectedIndex(currentIndex);
            currentIndex += 1;
        }

        ButtonElement backButton = new ButtonElement(() -> {
            this.deactivate();
            parentButton.selectStructureGroup(null);
        });
        backButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        backButton.setBounds(-130, -10, 250, 50);
        backButton.translation = FluxConstants.TextCancel;
        backButton.updateText();
        this.addFluxElement(backButton);

        ButtonElement selectButton = new ButtonElement(() -> {
            this.deactivate();
            parentButton.selectStructureGroup(this.getCurrentGroupID());
        });
        selectButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        selectButton.setBounds(130, -10, 250, 50);
        selectButton.text = "Select";
        selectButton.updateText();
        this.addFluxElement(selectButton);
    }

    @Override
    protected void onEscape() {
        this.deactivate();
        this.parentButton.selectStructureGroup(null);
    }

    public Identifier getCurrentGroupID(){
        int curIndex = this.getSelectedIndex();
        BaseText curElement = this.elements.get(curIndex);
        return Identifier.fromString(curElement.text);
    }
}
