package com.nikrasoff.structure_blocks.menus;

import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import ru.nern.becraft.bed.api.BlockEntity;

public class ConfirmWindow extends IngameBackgroundMenu{
    private final TextElement confirmMessage;
    private ButtonElement cancelButton;
    private ButtonElement confirmButton;
    public ConfirmWindow(){
        super();
        this.confirmMessage = this.createLabel(0, -55, 500, 100, "placeholder");
        this.confirmMessage.backgroundEnabled = true;
        this.confirmMessage.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);
    }

    private void updateCancelButton(Runnable action){
        if (this.cancelButton != null){
            this.removeFluxElement(this.cancelButton);
            this.cancelButton = null;
        }

        this.cancelButton = this.createButton(-255, 55, 250, 50, action, "Cancel");
        this.cancelButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);
    }

    private void updateConfirmButton(Runnable action){
        if (this.confirmButton != null){
            this.removeFluxElement(this.confirmButton);
            this.confirmButton = null;
        }

        this.confirmButton = this.createButton(255, 55, 250, 50, action, "Confirm");
        this.confirmButton.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);
    }

    public void closeConfirmWindow(){
        this.onBack();
    }

    public void switchToThisState(String message, Runnable onConfirm, Runnable onCancel) {
        super.switchToThisState();
        this.confirmMessage.text = message;
        this.confirmMessage.updateText();

        this.updateConfirmButton(onConfirm);
        this.updateCancelButton(onCancel);
    }
}
