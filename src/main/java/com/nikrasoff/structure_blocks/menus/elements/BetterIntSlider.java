package com.nikrasoff.structure_blocks.menus.elements;

import dev.crmodders.flux.api.v5.gui.base.BaseSlider;

public class BetterIntSlider extends BaseSlider {
    public String label = "";
    public BetterIntSlider(int min, int max) {
        super(min, max);
        this.updateText();
    }

    public int getValue() {
        return (int) this.value;
    }

    public void setValue(int value){
        this.value = this.validate(value);
    }

    @Override
    public void updateText() {
        this.text = this.label + (int) this.value;
        this.repaint();
    }

    @Override
    public float validate(float value) {
        return (float) ((int) super.validate(value));
    }
}
