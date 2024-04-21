package com.nikrasoff.structure_blocks.util;

import dev.crmodders.flux.api.v5.gui.base.BaseButton;

public class FixedToggleElement extends BaseButton {
    public boolean value;

    public FixedToggleElement(boolean defaultValue) {
        super();
        this.value = defaultValue;
        this.updateText();
    }

    @Override
    public void onMouseReleased() {
        super.onMouseReleased();
        this.value = !this.value;
        this.updateText();
    }

    @Override
    public void updateText() {
        this.text = this.value ? "Yes" : "No";
        super.updateText();
    }
}
