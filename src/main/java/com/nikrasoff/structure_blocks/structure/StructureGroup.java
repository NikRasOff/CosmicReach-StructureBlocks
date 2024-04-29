package com.nikrasoff.structure_blocks.structure;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.api.v5.resource.AssetLoader;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StructureGroup {
    public Array<StructureGroupEntry> structures = new Array<>();
    public transient Identifier identifier;

    public static String getFileString(Identifier id){
        return id.namespace + "/structure_groups/" + id.name + ".json";
    }
    public static String getAssetString(Identifier id){return id.namespace + ":structure_groups/" + id.name + ".json";}

    private StructureGroup(){}

    public StructureGroup(String id){
        this(Identifier.fromString(id));
    }
    public StructureGroup(Identifier id){
        this.identifier = id;
    }

    public boolean hasStructure(Identifier structureID){
        for (StructureGroupEntry i : this.structures) {
            if (i.structure.id.equals(structureID)) return true;
        }
        return false;
    }

    public boolean hasStructure(Structure str){
        return this.hasStructure(str.id);
    }

    public void saveStructureGroup(){
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonString = json.prettyPrint(this);

        try{
            File file = new File(SaveLocation.getSaveFolderLocation() + "/mods/assets/" + getFileString(this.identifier));
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonString.getBytes());
            fos.close();
        }
        catch (IOException e){
            StructureBlocks.LOGGER.info("Something went wrong when saving structure group \"" + this.identifier + "\": " + e);
        }
    }

    public static StructureGroup loadStructureGroup(Identifier id){
        if (!StructureUtils.structureGroupExists(id)) return null;
        FileHandle assetFile;
        if (StructureUtils.isStructureGroupDataMod(id)){
            assetFile = GameAssetLoader.loadAsset(getFileString(id));
        }
        else {
            assetFile = GameAssetLoader.loadAsset(getAssetString(id));
        }

        try {
            FileInputStream fis = new FileInputStream(assetFile.file());
            Json json = new Json();
            json.setIgnoreUnknownFields(true);
            StructureGroup group = json.fromJson(StructureGroup.class, fis);
            group.identifier = id;
            fis.close();
            return group;
        }
        catch (IOException e){
            StructureBlocks.LOGGER.info("Something went wrong when loading structure group \"" + id + "\": " + e);
            return null;
        }
    }

    public static class StructureGroupEntry implements Comparable<StructureGroupEntry>, Json.Serializable {
        public Structure structure;
        public int weight;

        private StructureGroupEntry(){}

        public StructureGroupEntry(Structure structure, int weight){
            this.structure = structure;
            this.weight = weight;
        }

        @Override
        public int compareTo(@NotNull StructureGroup.StructureGroupEntry o) {
            return Integer.compare(weight, o.weight);
        }

        @Override
        public void write(Json json) {
            json.writeValue(this.structure.id.toString(), this.weight);
        }

        @Override
        public void read(Json json, JsonValue jsonValue) {
            this.structure = Structure.loadStructure(Identifier.fromString(jsonValue.child().name()));
            this.weight = jsonValue.child().asInt();
        }
    }
}
