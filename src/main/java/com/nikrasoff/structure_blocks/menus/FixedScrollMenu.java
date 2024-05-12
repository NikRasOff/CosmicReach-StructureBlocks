package com.nikrasoff.structure_blocks.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import dev.crmodders.flux.api.v5.gui.base.BaseText;
import dev.crmodders.flux.menus.BasicMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.settings.SoundSettings;
import finalforeach.cosmicreach.ui.UIElement;

import java.util.ArrayList;
import java.util.List;

public class FixedScrollMenu extends BasicMenu implements InputProcessor {
    protected InputProcessor previousInputProcessor;
    protected final List<BaseText> elements;
    private float currentIndex;
    private float targetIndex;
    public boolean playSound;
    private int lastIndex;

    public float upDistance = 3.25F;
    public float downDistance = 3.25F;

    public FixedScrollMenu(GameState previousState) {
        super(previousState);
        this.playSound = true;
        this.previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(this);
        this.elements = new ArrayList<>();
        this.currentIndex = 0.0F;
        this.targetIndex = 0.0F;
    }

    public FixedScrollMenu() {
        this((GameState)null);
    }

    public int getSelectedIndex() {
        float distance = this.targetIndex - this.currentIndex;
        return (double)Math.abs(distance) < 0.5 ? (int)this.targetIndex : MathUtils.clamp(Math.round(this.currentIndex), 0, this.elements.size() - 1);
    }

    public void setSelectedIndex(int index) {
        this.targetIndex = (float)MathUtils.clamp(index, 0, this.elements.size() - 1);
        this.currentIndex = this.targetIndex;
    }

    public void addScrollElement(BaseText element) {
        this.elements.add(element);
        this.addFluxElement(element);
    }

    public void removeScrollElement(BaseText element) {
        this.elements.remove(element);
        this.removeFluxElement(element);
    }

    public void deactivate(){
        Gdx.input.setInputProcessor(this.previousInputProcessor);
    }

    @Override
    protected void onEscape() {
        super.onEscape();
        this.deactivate();
    }

    public void render(float partTime) {
        float distance = this.targetIndex - this.currentIndex;
        float increment = distance * 0.05F;
        if ((double)Math.abs(increment) < 0.001) {
            this.currentIndex = this.targetIndex;
        } else {
            this.currentIndex += increment;
        }

        int index;
        for(index = 0; index < this.elements.size(); ++index) {
            float distance2 = (float)index - this.currentIndex;
            float y = distance2 * 75.0F;
            float inverseDistance = 1.0F;
            float distance3 = Math.abs(distance2) * 1.35F;
            if (distance3 > 1.0F) {
                inverseDistance = 1.0F / distance3;
            }

            float size = Math.max(0.6F, inverseDistance);
            BaseText button = this.elements.get(index);
            button.x = 0.0F;
            button.y = y - 50.0F;
            button.width = 250.0F * size;
            button.height = 50.0F * size;
            button.fontSize = 18.0F * size;
            button.visible = distance2 < this.downDistance && distance2 > -this.upDistance;
            button.updateText();
        }

        index = this.getSelectedIndex();
        if (index != this.lastIndex) {
            if (this.playSound && SoundSettings.isSoundEnabled()) {
                UIElement.onHoverSound.play();
            }

            this.lastIndex = index;
        }

        super.render(partTime);
    }

    public boolean keyDown(int i) {
        if (i == 20) {
            return this.scrolled(0.0F, 1.0F);
        } else {
            return i == 19 && this.scrolled(0.0F, -1.0F);
        }
    }

    public boolean keyUp(int i) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    public boolean scrolled(float x, float y) {
        this.targetIndex += y;
        this.targetIndex = (float)((int)this.targetIndex);
        this.targetIndex = Math.max(this.targetIndex, 0.0F);
        this.targetIndex = Math.min(this.targetIndex, (float)(this.elements.size() - 1));
        return true;
    }
}
