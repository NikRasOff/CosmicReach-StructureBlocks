package com.nikrasoff.structure_blocks.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.nikrasoff.structure_blocks.menus.elements.BetterIntSlider;
import com.nikrasoff.structure_blocks.menus.elements.FixedTextBoxElement;
import com.nikrasoff.structure_blocks.menus.elements.FixedToggleElement;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.api.v5.gui.TextElement;
import dev.crmodders.flux.menus.BasicMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public abstract class IngameBackgroundMenu extends BasicMenu {
    private boolean cursorCaught = true;
    public IngameBackgroundMenu() {
        super();
    }

    public void switchToThisState(){
        UI.itemCatalog.setShown(false);
        this.previousState = GameState.currentGameState;
        this.cursorCaught = Gdx.input.isCursorCatched();
        Gdx.input.setCursorCatched(false);
        switchToGameState(this);
    }

    public void switchFromThisState(GameState to){
        this.previousState = null;
        Gdx.input.setCursorCatched(this.cursorCaught);
        switchToGameState(to);
    }

    @Override
    protected void onEscape() {
        if (this.previousState != null) {
            switchFromThisState(this.previousState);
        }
    }

    protected BetterIntSlider createIntSlider(float x, float y, float w, float h, int min, int max, int defaultValue, String label){
        BetterIntSlider newSlider = new BetterIntSlider(min, max);
        newSlider.setValue(defaultValue);
        newSlider.setBounds(x, y, w, h);
        newSlider.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.TOP_ALIGNED);
        newSlider.label = label;
        newSlider.updateText();
        this.addFluxElement(newSlider);
        return newSlider;
    }

    protected FixedToggleElement createToggle(float x, float y, float w, float h, boolean defaultValue){
        FixedToggleElement newToggle = new FixedToggleElement(defaultValue);
        newToggle.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.TOP_ALIGNED);
        newToggle.setBounds(x, y, w, h);
        this.addFluxElement(newToggle);
        return newToggle;
    }

    protected ButtonElement createButton(float x, float y, float w, float h, Runnable action, String text){
        ButtonElement newButton = new ButtonElement(action);
        newButton.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.TOP_ALIGNED);
        newButton.setBounds(x, y, w, h);
        newButton.text = text;
        newButton.updateText();
        this.addFluxElement(newButton);
        return newButton;
    }

    protected TextElement createLabel(float x, float y, float w, float h, String text){
        TextElement newLabel = new TextElement(text);
        newLabel.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.TOP_ALIGNED);
        newLabel.setBounds(x, y, w, h);
        newLabel.backgroundEnabled = false;
        this.addFluxElement(newLabel);
        return newLabel;
    }

    protected FixedTextBoxElement createTextInput(float x, float y, float w, float h, String label, String startingText){
        FixedTextBoxElement newTextInput = new FixedTextBoxElement();
        newTextInput.setBounds(x, y, w, h);
        newTextInput.setAnchors(HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.TOP_ALIGNED);
        newTextInput.label = label;
        newTextInput.setContent(startingText);
        newTextInput.updateText();
        this.addFluxElement(newTextInput);
        return newTextInput;
    }

    protected FixedTextBoxElement[] createVectorInput(float x, float y){
        FixedTextBoxElement inputX = this.createTextInput(x, y, 80, 50, "X:", "0");
        FixedTextBoxElement inputY =this.createTextInput(x + 90, y, 80, 50, "Y:", "0");
        FixedTextBoxElement inputZ =this.createTextInput(x + 180, y, 80, 50, "Z:", "0");

        FixedTextBoxElement[] vector = new FixedTextBoxElement[3];
        vector[0] = inputX;
        vector[1] = inputY;
        vector[2] = inputZ;
        return vector;
    }

    @Override
    public void render(float partTime) {
        super.render(partTime);

        ScreenUtils.clear(0.1F, 0.1F, 0.2F, 1.0F, true);
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        if (Controls.inventoryPressed()) UI.itemCatalog.toggleShown();
        IN_GAME.render(partTime);
        this.drawUIElements();
    }
}
