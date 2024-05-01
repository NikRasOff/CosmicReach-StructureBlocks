package com.nikrasoff.structure_blocks.structure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.StructureBlocksRegistries;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StructureGroup {
    private static final Map<Identifier, StructureGroup> ALL_STRUCTURE_GROUPS = new HashMap<>();

    public Array<StructureGroupEntry> structures = new Array<>();
    public String idString;

    private StructureGroup(){}

    public StructureGroup(String id){
        this(Identifier.fromString(id));
    }
    public StructureGroup(Identifier id){
        this.idString = id.toString();
    }

    public boolean hasStructure(String structureID){
        for (StructureGroupEntry i : this.structures) {
            if (i.structureID.equals(structureID)) return true;
        }
        return false;
    }

    public void saveStructureGroup(){
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonString = json.prettyPrint(this);

        try{
            File file = new File(SaveLocation.getSaveFolderLocation() + "/mods/assets/structure_groups/" + this.idString.replaceAll(":", "/") + ".json");
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonString.getBytes());
            fos.close();

            loadStructureGroup(new FileHandle(file));
        }
        catch (IOException e){
            StructureBlocks.LOGGER.info("Something went wrong when saving structure group \"" + this.idString + "\": " + e);
        }
    }

    public static StructureGroup getStructureGroup(Identifier groupID){
        AccessableRegistry<StructureGroup> modGroups = StructureBlocksRegistries.STRUCTURE_GROUPS.access();
        if (modGroups.contains(groupID)){
            return modGroups.get(groupID);
        }
        return ALL_STRUCTURE_GROUPS.get(groupID);
    }

    public static StructureGroup loadStructureGroup(Identifier groupID){
        return loadStructureGroup(GameAssetLoader.loadAsset("%s:structure_groups/%s.json".formatted(groupID.namespace, groupID.name)));
    }

    private static StructureGroup loadStructureGroup(FileHandle groupFile){
        try {
            FileInputStream fis = new FileInputStream(groupFile.file());
            Json json = new Json();
            json.setIgnoreUnknownFields(true);
            StructureGroup group = json.fromJson(StructureGroup.class, fis);
            fis.close();

            ALL_STRUCTURE_GROUPS.put(Identifier.fromString(group.idString), group);

            return group;
        }
        catch (IOException e){
            StructureBlocks.LOGGER.info("Something went wrong when loading structure group \"" + groupFile + "\": " + e);
            return null;
        }
    }

    static {
        Array<FileHandle> structureGroupFiles = StructureUtils.getAllFiles(Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/structure_groups"));
        for (FileHandle f : structureGroupFiles){
            if (f.extension().equals("json")){
                loadStructureGroup(f);
            }
        }
    }

    public static class StructureGroupEntry implements Comparable<StructureGroupEntry>, Json.Serializable {
        public String structureID;
        public int weight;

        private StructureGroupEntry(){}

        public StructureGroupEntry(String structureID, int weight){
            this.structureID = structureID;
            this.weight = weight;
        }

        @Override
        public int compareTo(@NotNull StructureGroup.StructureGroupEntry o) {
            return Integer.compare(weight, o.weight);
        }

        @Override
        public void write(Json json) {
            json.writeValue(this.structureID, this.weight);
        }

        @Override
        public void read(Json json, JsonValue jsonValue) {
            this.structureID = jsonValue.child().name();
            this.weight = jsonValue.child().asInt();
        }
    }
}
