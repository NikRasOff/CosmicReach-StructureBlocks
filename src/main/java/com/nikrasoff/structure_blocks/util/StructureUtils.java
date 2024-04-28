package com.nikrasoff.structure_blocks.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.nikrasoff.structure_blocks.menus.StructureGroupsMenu;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class StructureUtils {
    public static boolean isValidInt(String input){
        return input.matches("[\\-+]?(?:0|[123456789]\\d*)");
    }

    public static UIElement createStructureGroupButton(float x, float y, float w, float h){
        UIElement newButton = new UIElement(x, y, w, h) {
            private StructureGroupsMenu groupsMenu = new StructureGroupsMenu();
            public void onCreate(){
                super.onCreate();
                this.updateText();
            }

            @Override
            public void onClick() {
                super.onClick();
                groupsMenu.switchTo();
            }

            public void updateText(){
                this.setText("Structure Groups");
            }
        };
        newButton.show();
        return newButton;
    }

    public static boolean isStructureDataMod(Identifier structureID){
        Files files = Gdx.files;
        String saveLocation = SaveLocation.getSaveFolderLocation();
        FileHandle modLocationFile = files.absolute(saveLocation + "/mods/assets/" + Structure.getFileString(structureID));
        return modLocationFile.exists();
    }

    public static boolean structureExists(Identifier structureID) {
        Files files = Gdx.files;
        String saveLocation = SaveLocation.getSaveFolderLocation();
        FileHandle modLocationFile = files.absolute(saveLocation + "/mods/assets/" + Structure.getFileString(structureID));
        if (modLocationFile.exists()) {
            return true;
        } else {
            FileHandle vanillaLocationFile = Gdx.files.internal("structures/" + structureID.name + ".zip");
            if (vanillaLocationFile.exists()) {
                return true;
            } else {
                FileHandle classpathLocationFile = Gdx.files.classpath("assets/%s/structures/%s.zip".formatted(structureID.namespace, structureID.name));
                return classpathLocationFile.exists();
            }
        }
    }


}
