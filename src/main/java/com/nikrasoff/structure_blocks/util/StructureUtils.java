package com.nikrasoff.structure_blocks.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.nikrasoff.structure_blocks.menus.StructureGroupsMenu;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import dev.crmodders.flux.api.v5.gui.ButtonElement;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
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

    public static Array<FileHandle> getAllFiles(FileHandle root){
        Array<FileHandle> dirsToCheck = new Array<>();
        dirsToCheck.add(root);
        Array<FileHandle> result = new Array<>();

        while (!dirsToCheck.isEmpty()){
            Array<FileHandle> nextDirs = new Array<>();
            for (FileHandle dir : dirsToCheck){
                for (FileHandle f : dir.list()){
                    if (f.isDirectory()){
                        nextDirs.add(f);
                    }
                    else{
                        result.add(f);
                    }
                }
            }
            dirsToCheck = nextDirs;
        }

        return result;
    }
}
