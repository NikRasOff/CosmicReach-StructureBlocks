package com.nikrasoff.structure_blocks.menus;

import com.nikrasoff.structure_blocks.menus.elements.ElementSelectButton;
import com.nikrasoff.structure_blocks.menus.elements.FixedTextBoxElement;
import com.nikrasoff.structure_blocks.structure.SearchElement;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.FluxConstants;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.base.BaseText;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementSelectMenu<T extends SearchElement> extends FixedScrollMenu {
    private final Map<Identifier, T> elementMap;
    public ElementSelectMenu(Identifier selected, ElementSelectButton<T> parentButton, Map<Identifier, T> elementMap){
        super();
        this.upDistance = 2.35F;
        this.elementMap = elementMap;
        this.previousState = parentButton.parentState;
        this.updateElementSelection("", selected);

        FixedTextBoxElement searchBar = new FixedTextBoxElement();
        searchBar.addOnDeactivateAction(() -> this.updateElementSelection(searchBar.getContent(), this.getCurrentElementID()));
        searchBar.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.TOP_ALIGNED);
        searchBar.setBounds(0, 10, 500, 50);
        searchBar.label = "Search: ";
        searchBar.setContent("");
        searchBar.updateText();
        this.addFluxElement(searchBar);

        ButtonElement backButton = new ButtonElement(() -> {
            this.deactivate();
            parentButton.selectElement(selected);
        });
        backButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        backButton.setBounds(-130, -10, 250, 50);
        backButton.translation = FluxConstants.TextCancel;
        backButton.updateText();
        this.addFluxElement(backButton);

        ButtonElement selectButton = new ButtonElement(() -> {
            this.deactivate();
            parentButton.selectElement(this.getCurrentElementID());
        });
        selectButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        selectButton.setBounds(130, -10, 250, 50);
        selectButton.text = "Select";
        selectButton.updateText();
        this.addFluxElement(selectButton);
    }

    private void addElementButton(Identifier id){
        BaseText newText = new BaseText();
        newText.text = id.toString();
        newText.backgroundEnabled = false;
        newText.updateText();
        this.addScrollElement(newText);
    }

    public void updateElementSelection(String searchQuery, Identifier selected){
        List<BaseText> elCopy = new ArrayList<>(this.elements);
        for (BaseText t : elCopy) this.removeScrollElement(t);

        int currentIndex = 0;
        int selectedIndex = 0;
        for (Identifier elementID : this.elementMap.keySet()){
            if (elementID.toString().equalsIgnoreCase(searchQuery)) {
                this.addElementButton(elementID);
                if (elementID.equals(selected)) selectedIndex = currentIndex;
                ++currentIndex;
                continue;
            }
            if (this.elementMap.get(elementID).isHiddenFromCatalog()) continue;
            String elementString = elementID.toString();
            if (StructureUtils.stringFitsQuery(elementString, searchQuery)){
                this.addElementButton(elementID);
                if (elementID.equals(selected)) selectedIndex = currentIndex;
                ++currentIndex;
            }
        }
        this.setSelectedIndex(selectedIndex);
    }

    public Identifier getCurrentElementID(){
        int curIndex = this.getSelectedIndex();
        if (curIndex < 0) curIndex = 0;
        if (this.elements.size() <= curIndex) return null;
        BaseText curElement = this.elements.get(curIndex);
        return Identifier.fromString(curElement.text);
    }
}
