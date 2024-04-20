package com.nikrasoff.structure_blocks.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.io.SaveLocation;

public class StructureUtils {
    public static boolean isValidInt(String input){
        return input.matches("[\\-+]?(?:0|[123456789]\\d*)");
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
