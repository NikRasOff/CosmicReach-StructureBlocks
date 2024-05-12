package com.nikrasoff.structure_blocks.menus.elements;

import dev.crmodders.flux.api.v5.gui.TextBoxElement;

public class FixedTextBoxElement extends TextBoxElement {
    protected static FixedTextBoxElement currentTextBoxElement;
    protected Runnable onDeactivateAction;

    public FixedTextBoxElement(){
    }

    public void addOnDeactivateAction(Runnable action){
        this.onDeactivateAction = action;
    }

    @Override
    public void onMouseEnter() {
        if (!this.visible) return;
        super.onMouseEnter();
    }

    @Override
    public void onMouseReleased() {
        if (!this.visible) return;
        super.onMouseReleased();
    }

    @Override
    public void onMousePressed() {
        if (!this.visible) return;
        super.onMousePressed();
    }

    @Override
    public void activate() {
        if (currentTextBoxElement == this) return;
        if (currentTextBoxElement != null) currentTextBoxElement.deactivate();
        currentTextBoxElement = this;
        this.editor.setCursor(this.getContent().length());
        super.activate();
    }

    @Override
    public void deactivate() {
        if (currentTextBoxElement != this) return;
        currentTextBoxElement = null;
        this.onDeactivate();
        super.deactivate();
    }

    public void onDeactivate(){
        if (this.onDeactivateAction != null) this.onDeactivateAction.run();
    }
}
