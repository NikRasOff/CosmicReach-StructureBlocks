package com.nikrasoff.structure_blocks.menus.elements;

import com.badlogic.gdx.Input;

public class ButtonTextBox extends FixedTextBoxElement {
    public String buttonLabel = "";
    private final Runnable buttonAction;

    public ButtonTextBox(Runnable action){
        this.buttonAction = action;
    }

    public void updateText() {
        if (currentTextBoxElement != this){
            this.text = this.buttonLabel;
        }
        else {
            this.text = this.updateTranslation(this.translation);
        }
        this.repaint();
    }

    public boolean keyDown(int keycode) {
        if (currentTextBoxElement == this){
            if (keycode == Input.Keys.ENTER){
                this.buttonAction.run();
            }
        }
        return super.keyDown(keycode);
    }
}
