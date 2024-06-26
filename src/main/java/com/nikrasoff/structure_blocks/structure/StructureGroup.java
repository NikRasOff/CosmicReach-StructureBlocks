package com.nikrasoff.structure_blocks.structure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.StructureBlocksRegistries;
import com.nikrasoff.structure_blocks.block_entities.JigsawBlockEntity;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class StructureGroup implements SearchElement {
    public static final Map<Identifier, StructureGroup> ALL_STRUCTURE_GROUPS = new HashMap<>();

    public Array<StructureGroupEntry> structures = new Array<>();
    public String idString;
    public boolean hiddenFromCatalogue = false;

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

    public Array<StructureGroupEntry> getStructuresByConnection(String connection, String jigsawName){
        Array<StructureGroupEntry> resultArray = new Array<>();
        for (StructureGroupEntry entry : this.structures){
            Structure entryStructure = Structure.getStructure(Identifier.fromString(entry.structureID));
            if (entryStructure == null) continue;
            ListTag<CompoundTag> structureJigsawEntities = entryStructure.getJigsaws(connection, jigsawName);
            if (structureJigsawEntities.size() != 0) resultArray.add(entry);
        }

        return resultArray;
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

    public void loadAllStructuresInGroup(boolean hideStructures){
        for (StructureGroupEntry entry : this.structures){
            Identifier structureID = Identifier.fromString(entry.structureID);
            if (Structure.structureExists(structureID)) continue;
            Structure loadedStructure = Structure.loadStructure(structureID);
            if (loadedStructure == null) {
                StructureBlocks.LOGGER.error("Couldn't load structure " + entry.structureID + " from group " + this.idString);
                continue;
            }
            if (hideStructures) loadedStructure.hiddenFromCatalogue = true;
            StructureBlocksRegistries.STRUCTURES.register(structureID, loadedStructure);
        }
    }

    public static StructureGroup loadStructureGroup(Identifier groupID){
        return loadStructureGroup(GameAssetLoader.loadAsset("%s:structure_groups/%s.json".formatted(groupID.namespace, groupID.name)));
    }

    private static StructureGroup loadStructureGroup(FileHandle groupFile){
        if (!groupFile.exists()) StructureBlocks.LOGGER.error("wtf, it doesn't exist apparently");
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        StructureGroup group = json.fromJson(StructureGroup.class, groupFile);

        ALL_STRUCTURE_GROUPS.put(Identifier.fromString(group.idString), group);

        return group;
    }

    static {
        Array<FileHandle> structureGroupFiles = StructureUtils.getAllFiles(Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/structure_groups"));
        for (FileHandle f : structureGroupFiles){
            if (f.extension().equals("json")){
                loadStructureGroup(f);
            }
        }
    }

    @Override
    public boolean isHiddenFromCatalog() {
        return this.hiddenFromCatalogue;
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
