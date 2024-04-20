package com.nikrasoff.structure_blocks.util;

import dev.crmodders.flux.api.v5.gui.TextBoxElement;

public class FixedTextBoxElement extends TextBoxElement {
    private static FixedTextBoxElement currentTextBoxElement;

    public FixedTextBoxElement(){
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
        super.deactivate();
    }
}
