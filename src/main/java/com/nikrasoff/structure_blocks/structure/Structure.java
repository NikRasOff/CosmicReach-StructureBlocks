package com.nikrasoff.structure_blocks.structure;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;
import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.util.BlockEntitySaver;
import com.nikrasoff.structure_blocks.util.IntVector3;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.flux.util.BlockPositionUtil;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Structure {
    public static String output = "";
    public IntVector3 size = new IntVector3(0, 0, 0);
    public Identifier id = new Identifier(StructureBlocks.MOD_ID, "placeholder");
    public int version = 0;

    public Structure(){}

    public static String getFileString(Identifier id){
        return id.namespace + "/structures/" + id.name + ".zip";
    }
    public static String getAssetString(Identifier id){return id.namespace + ":structures/" + id.name + ".zip";}

    public boolean place(String zoneID, BlockPosition pos){
        Array<BlockState> blockStatePalette = new Array<>();
        Zone zone = InGame.world.getZone(zoneID);
        try {
            FileHandle assetFile;
            if (StructureUtils.isStructureDataMod(this.id)){
                assetFile = GameAssetLoader.loadAsset(getFileString(this.id));
            }
            else {
                assetFile = GameAssetLoader.loadAsset(getAssetString(this.id));
            }
            ZipInputStream zipInputStream = new ZipInputStream(assetFile.read());
            ZipEntry entry = zipInputStream.getNextEntry();

            if (version < 1 || version > 2) {
                output = "Unsupported version";
                return false;
            }
            while (entry != null) {
                if (entry.isDirectory()) continue;

                if (entry.getName().equalsIgnoreCase("block_palette.json")) {
                    byte[] buffer = new byte[1024];
                    int len;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    while ((len = zipInputStream.read(buffer)) > -1) {
                        outputStream.write(buffer, 0, len);
                    }

                    outputStream.close();
                    String text = outputStream.toString(StandardCharsets.UTF_8);

                    JsonValue blockPalette = new JsonReader().parse(text);
                    int size = blockPalette.size;
                    for (int i = 0; i < size; ++i){
                        BlockState nbs = BlockState.getInstance(blockPalette.getString(i));
                        blockStatePalette.add(nbs);
                    }
                }

                if (entry.getName().equalsIgnoreCase("structure")){
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, len);
                    }

                    ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    IntVector3 origin = new IntVector3(pos.getGlobalX(), pos.getGlobalY(), pos.getGlobalZ());
                    IntVector3 offset = new IntVector3();

                    while (buffer.hasRemaining()){
                        if (offset.x >= this.size.x){
                            offset.x = 0;
                            offset.y += 1;
                        }
                        if (offset.y >= this.size.y){
                            offset.y = 0;
                            offset.z += 1;
                        }

                        int blockId = buffer.getInt();
                        Vector3 globalPos = origin.cpy().add(offset).toVector3();
                        BlockSetter.replaceBlock(zone, blockStatePalette.get(blockId), BlockPositionUtil.getBlockPositionAtGlobalPos(globalPos), new Queue<>());
                        offset.x += 1;
                    }
                }

                if (version >= 2 && entry.getName().equalsIgnoreCase("bed.bed")){
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, len);
                    }

                    FileHandle bedFile = new FileHandle(SaveLocation.getSaveFolderLocation() + "/temp.bed");
                    bedFile.write(false).write(outputStream.toByteArray());

                    BlockEntitySaver.loadBlockEntities(zone, new IntVector3(pos.getGlobalX(), pos.getGlobalY(), pos.getGlobalZ()), bedFile);
                    bedFile.delete();
                }

                entry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
            output = "Structure placed successfully";
            return true;
        }
        catch (IOException exception){
            output = exception.getMessage();
            return false;
        }
    }

    public static void saveStructure(StructureBlockEntity entity){
        BlockPosition entityPos = entity.getBlockPos();
        IntVector3 pos1 = new IntVector3(entityPos.getGlobalX(), entityPos.getGlobalY(), entityPos.getGlobalZ()).add(entity.offset);
        IntVector3 pos2 = pos1.cpy().add(entity.size);

        saveStructure(pos1, pos2, entity.getZone().zoneId, Identifier.fromString(entity.structureId), new BlockReplaceRule(BlockState.getInstance(entity.replaceWith), entityPos));
    }

    public static boolean saveStructure(IntVector3 pos1, IntVector3 pos2, String zoneID, Identifier structureID, BlockReplaceRule... replaceRules){
        // A lot of this is taken from/inspired by Cosmatica

        IntVector3 minPos = IntVector3.lesserVector(pos1, pos2);
        IntVector3 maxPos = IntVector3.greaterVector(pos1, pos2);
        Array<String> seenBlockStateStrings = new Array<>();
        Array<Integer> palletizedBlocks = new Array<>();

        for (int z = minPos.z; z < maxPos.z; ++z){
            for (int y = minPos.y; y < maxPos.y; ++y){
                for (int x = minPos.x; x < maxPos.x; ++x){
                    BlockState blockState = InGame.world.getZone(zoneID).getBlockState(x, y, z);
                    for (BlockReplaceRule brr : replaceRules){
                        BlockPosition blockPos = BlockPositionUtil.getBlockPositionAtGlobalPos(x, y, z);
                        for (BlockPosition bp : brr.replacePositions){
                            if (blockPos.equals(bp)) {
                                blockState = brr.replaceWithBlock;
                                break;
                            }
                        }
                    }
                    String blockStateString = blockState.toString();
                    int index = seenBlockStateStrings.indexOf(blockStateString, false);

                    if (index == -1){
                        seenBlockStateStrings.add(blockStateString);
                        index = seenBlockStateStrings.size - 1;
                    }

                    palletizedBlocks.add(index);
                }
            }
        }

        FileHandle bedFile = BlockEntitySaver.saveBlockEntities(minPos, maxPos, SaveLocation.getSaveFolderLocation() + "temp.bed");

        JsonValue structureInfo = new JsonValue(JsonValue.ValueType.object);
        structureInfo.addChild("structureVersion", new JsonValue(StructureBlocks.STRUCTURE_SAVE_VERSION));
        maxPos.sub(minPos);
        structureInfo.addChild("sizeX", new JsonValue(maxPos.x));
        structureInfo.addChild("sizeY", new JsonValue(maxPos.y));
        structureInfo.addChild("sizeZ", new JsonValue(maxPos.z));

        JsonValue blockStatesJsonArray = new JsonValue(JsonValue.ValueType.array);
        for (String blockStateString : seenBlockStateStrings){
            blockStatesJsonArray.addChild(new JsonValue(blockStateString));
        }

        ByteBuffer palletizedBlockBytes = ByteBuffer.allocate(palletizedBlocks.size * 4);
        palletizedBlockBytes.order(ByteOrder.BIG_ENDIAN);
        for (int block : palletizedBlocks){
            palletizedBlockBytes.putInt(block);
        }

        ZipEntry infoEntry = new ZipEntry("structure_info.json");
        ZipEntry blockPaletteEntry = new ZipEntry("block_palette.json");
        ZipEntry structureEntry = new ZipEntry("structure");
        ZipEntry bedEntry = new ZipEntry("bed.bed");

        String saveFilePath = SaveLocation.getSaveFolderLocation() + "/mods/assets/" + structureID.namespace + "/structures/" + structureID.name + ".zip";
        File saveFile = new File(saveFilePath);
        saveFile.getParentFile().mkdirs();

        try {
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(saveFile));
            outputStream.putNextEntry(infoEntry);
            outputStream.write(structureInfo.toJson(JsonWriter.OutputType.json).getBytes(StandardCharsets.UTF_8));
            outputStream.closeEntry();
            outputStream.putNextEntry(blockPaletteEntry);
            outputStream.write(blockStatesJsonArray.toJson(JsonWriter.OutputType.json).getBytes(StandardCharsets.UTF_8));
            outputStream.closeEntry();
            outputStream.putNextEntry(structureEntry);
            outputStream.write(palletizedBlockBytes.array());
            outputStream.closeEntry();
            outputStream.putNextEntry(bedEntry);
            outputStream.write(bedFile.readBytes());
            outputStream.closeEntry();
            outputStream.close();
        }
        catch (IOException exception){
            output = exception.getMessage();
            bedFile.delete();
            return false;
        }
        bedFile.delete();
        output = "Structure saved successfully";
        return true;
    }

    public static Structure loadStructure(Identifier structureID){
        if (!StructureUtils.structureExists(structureID)){
            output = "Structure doesn't exist";
            return null;
        }
        try {
            FileHandle assetFile;
            if (StructureUtils.isStructureDataMod(structureID)){
                assetFile = GameAssetLoader.loadAsset(getFileString(structureID));
            }
            else {
                assetFile = GameAssetLoader.loadAsset(getAssetString(structureID));
            }
            ZipInputStream zipInputStream = new ZipInputStream(assetFile.read());
            ZipEntry entry = zipInputStream.getNextEntry();

            Structure newStructure = new Structure();
            newStructure.id = structureID;

            while (entry != null) {
                if (entry.isDirectory()) continue;

                if (entry.getName().equalsIgnoreCase("structure_info.json")) {
                    JsonValue structureInfo = new JsonReader().parse(zipInputStream);
                    if (!structureInfo.has("structureVersion")) {
                        output = "Missing version tag";
                        return null;
                    }
                    int version = structureInfo.getInt("structureVersion");
                    newStructure.version = version;
                    switch (version) {
                        case 1, 2 -> {
                            newStructure.size.x = structureInfo.getInt("sizeX");
                            newStructure.size.y = structureInfo.getInt("sizeY");
                            newStructure.size.z = structureInfo.getInt("sizeZ");
                        }
                        default -> {
                            output = "Unsupported structure version";
                            return null;
                        }
                    }

                    zipInputStream.close();
                    output = "Loaded structure successfully";
                    return newStructure;
                }

                entry = zipInputStream.getNextEntry();
            }
        }
        catch (IOException exception){
            System.out.println(exception);
            output = exception.toString();
            return null;
        }
        output = "Error";
        return null;
    }

    public static class BlockReplaceRule{
        public BlockPosition[] replacePositions;
        public BlockState replaceWithBlock;
        public BlockReplaceRule(BlockState replaceBlock, BlockPosition... replaceAt){
            this.replacePositions = replaceAt;
            this.replaceWithBlock = replaceBlock;
        }
    }
}
