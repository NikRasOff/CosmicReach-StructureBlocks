package com.nikrasoff.structure_blocks.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.nikrasoff.structure_blocks.menus.elements.StructureGroupSelectButton;
import com.nikrasoff.structure_blocks.menus.elements.StructureSelectButton;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import dev.crmodders.flux.menus.ScrollMenu;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;

public class FixedScrollMenu extends ScrollMenu {
    protected InputProcessor previousInputProcessor;

    public static StructureGroupEditMenu createGroupEditMenu(StructureGroup group, GameState prevState){
        InputProcessor prevInput = Gdx.input.getInputProcessor();
        StructureGroupEditMenu newScrollMenu = new StructureGroupEditMenu(group, prevState);
        newScrollMenu.previousInputProcessor = prevInput;
        return newScrollMenu;
    }

    public static StructureSelectMenu createStructureSelectMenu(Identifier structureID, StructureSelectButton parentButton){
        InputProcessor prevInput = Gdx.input.getInputProcessor();
        StructureSelectMenu newScrollMenu = new StructureSelectMenu(structureID, parentButton);
        newScrollMenu.previousInputProcessor = prevInput;
        return newScrollMenu;
    }

    public static StructureGroupSelectMenu createStructureGroupSelectMenu(Identifier groupID, StructureGroupSelectButton parentButton){
        InputProcessor prevInput = Gdx.input.getInputProcessor();
        StructureGroupSelectMenu newScrollMenu = new StructureGroupSelectMenu(groupID, parentButton);
        newScrollMenu.previousInputProcessor = prevInput;
        return newScrollMenu;
    }

    public void deactivate(){
        if (this.previousInputProcessor != null) Gdx.input.setInputProcessor(this.previousInputProcessor);
    }

    @Override
    protected void onEscape() {
        this.deactivate();
        super.onEscape();
    }

    protected FixedScrollMenu(){
        super();
    }
    protected FixedScrollMenu(GameState previousState){
        super(previousState);
    }
}
