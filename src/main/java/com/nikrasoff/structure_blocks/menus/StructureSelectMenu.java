package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.menus.elements.StructureSelectButton;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.FluxConstants;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.base.BaseText;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureSelectMenu extends FixedScrollMenu {
    protected StructureSelectMenu(Identifier selected, StructureSelectButton parentButton){
        this.previousState = parentButton.parentState;
        int currentIndex = 0;
        for (Identifier id : Structure.ALL_STRUCTURES.keySet()){
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
            parentButton.selectStructure(selected);
        });
        backButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        backButton.setBounds(-130, -10, 250, 50);
        backButton.translation = FluxConstants.TextCancel;
        backButton.updateText();
        this.addFluxElement(backButton);

        ButtonElement selectButton = new ButtonElement(() -> {
            this.deactivate();
            parentButton.selectStructure(this.getCurrentStructureID());
        });
        selectButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        selectButton.setBounds(130, -10, 250, 50);
        selectButton.text = "Select";
        selectButton.updateText();
        this.addFluxElement(selectButton);
    }

    public Identifier getCurrentStructureID(){
        int curIndex = this.getSelectedIndex();
        BaseText curElement = this.elements.get(curIndex);
        return Identifier.fromString(curElement.text);
    }
}
