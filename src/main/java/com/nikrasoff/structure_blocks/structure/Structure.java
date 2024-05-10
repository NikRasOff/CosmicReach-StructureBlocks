package com.nikrasoff.structure_blocks.structure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;
import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.StructureBlocksRegistries;
import com.nikrasoff.structure_blocks.block_entities.JigsawBlockEntity;
import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.structure.block_replacements.BlockReplacementFull;
import com.nikrasoff.structure_blocks.structure.rules.*;
import com.nikrasoff.structure_blocks.util.BlockEntitySaver;
import com.nikrasoff.structure_blocks.util.IntVector3;
import com.nikrasoff.structure_blocks.util.StructureUtils;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.flux.util.BlockPositionUtil;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.handlers.BlockEntityLoadHandler;
import ru.nern.becraft.bed.utils.BEUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Structure {
    private static final Map<Identifier, Structure> ALL_STRUCTURES = new HashMap<>();
    public IntVector3 size = new IntVector3(0, 0, 0);
    public int version = 0;
    private FileHandle assetFile;

    private ListTag<CompoundTag> jigsawCache;

    public Structure(){}

    public void place(String zoneID, BlockPosition pos, boolean processJigsaw){
        Array<BlockState> blockStatePalette = new Array<>();
        Zone zone = InGame.world.getZone(zoneID);
        try {
            ZipInputStream zipInputStream = new ZipInputStream(assetFile.read());
            ZipEntry entry = zipInputStream.getNextEntry();

            if (version < 1 || version > 3) {
                return;
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
                        BlockState blockState = blockStatePalette.get(blockId);
                        if (blockState != StructureBlocks.STRUCTURE_VOID){
                            BlockSetter.replaceBlock(zone, blockState, BlockPositionUtil.getBlockPositionAtGlobalPos(globalPos), new Queue<>());
                        }
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
        }
        catch (IOException ignored){
            return;
        }

        if (!processJigsaw) return;

        IntVector3 pos1 = new IntVector3(pos.getGlobalX(), pos.getGlobalY(), pos.getGlobalZ());

        processJigsaw(pos1, new Array<>(), -1);
    }

    public void processJigsaw(IntVector3 origin, Array<IntVector3> notProcessBlocks, int chain){
        ListTag<CompoundTag> jigsawData = this.getBlockEntityData(JigsawBlockEntity.BE_TYPE);
        Array<JigsawBlockEntity> ent = new Array<>();
        jigsawData.forEach((beData) -> {
            IntVector3 jigsawPositionVector = new IntVector3(beData.getInt("localX"), beData.getInt("localY"), beData.getInt("localZ"));
            jigsawPositionVector.add(origin);
            BlockPosition jigsawPosition = jigsawPositionVector.toBlockPosition();
            BlockEntity be = BEUtils.getBlockEntity(jigsawPosition);
            if (be instanceof JigsawBlockEntity jigsawEntity){
                if (notProcessBlocks.contains(jigsawPositionVector, false)){
                    jigsawEntity.disappear();
                }
                else{
                    ent.add(jigsawEntity);
                }
            }
        });

        ent.sort((o1, o2) -> -Integer.compare(o1.processPriority, o2.processPriority));
        for (JigsawBlockEntity e : ent){
            if (chain < 0) e.process();
            else e.process(chain);
            e.disappear();
        }
        this.clearJigsawCache();
    }

    public static boolean structureExists(Identifier structureID){
        AccessableRegistry<Structure> modStructures = StructureBlocksRegistries.STRUCTURES.access();
        if (modStructures.contains(structureID)){
            return true;
        }
        return ALL_STRUCTURES.containsKey(structureID);
    }

    public ListTag<CompoundTag> getJigsaws(String facing, String jigsawName){
        ListTag<CompoundTag> result = (ListTag<CompoundTag>) ListTag.createUnchecked(CompoundTag.class);

        if (this.jigsawCache == null){
            this.jigsawCache = this.getBlockEntityData(JigsawBlockEntity.BE_TYPE);
        }
        this.jigsawCache.forEach((beTag) -> {
            if (beTag.getString("facing").equals(facing) && (beTag.getString("blockName").equals(jigsawName) || jigsawName.equals("any"))){
                result.add(beTag);
            }
        });

        return result;
    }

    public void clearJigsawCache(){
        this.jigsawCache = null;
    }

    public ListTag<CompoundTag> getBlockEntityData(BlockEntityType<? extends BlockEntity> type){
        ListTag<CompoundTag> result = (ListTag<CompoundTag>) ListTag.createUnchecked(CompoundTag.class);

        if (version != 3) {
            return result;
        }
        try{
            ZipInputStream zipInputStream = new ZipInputStream(assetFile.read());
            ZipEntry entry = zipInputStream.getNextEntry();

            while (entry != null){
                if (entry.isDirectory()) {
                    entry = zipInputStream.getNextEntry();
                    continue;
                }

                if (entry.getName().equalsIgnoreCase("bed.bed")){
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, len);
                    }

                    FileHandle bedFile = new FileHandle(SaveLocation.getSaveFolderLocation() + "/temp.bed");
                    bedFile.write(false).write(outputStream.toByteArray());

                    CompoundTag bedCompound = BlockEntityLoadHandler.readBED(bedFile.file());
                    Tag<?> chunkTag = bedCompound.get("area");
                    if (!(chunkTag instanceof ListTag)) {
                        break;
                    }
                    ListTag<CompoundTag> beTagList = ((ListTag)chunkTag).asCompoundTagList();

                    beTagList.forEach((beTag) -> {
                        Identifier id = Identifier.fromString(beTag.getString("id"));

                        if (id.equals(type.getId())){
                            result.add(beTag);
                        }
                    });

                    bedFile.delete();
                    break;
                }
                entry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        }
        catch (IOException e){
            return result;
        }
        return result;
    }

    public static Structure getStructure(Identifier structureID){
        AccessableRegistry<Structure> modStructures = StructureBlocksRegistries.STRUCTURES.access();
        if (modStructures.contains(structureID)){
            return modStructures.get(structureID);
        }
        return ALL_STRUCTURES.get(structureID);
    }

    public static void saveStructure(StructureBlockEntity entity){
        BlockPosition entityPos = entity.getBlockPos();
        IntVector3 pos1 = new IntVector3(entityPos.getGlobalX(), entityPos.getGlobalY(), entityPos.getGlobalZ()).add(entity.offset);
        IntVector3 pos2 = pos1.cpy().add(entity.size);
        if (entity.size.x < 0) {
            pos1.x += 1;
            pos2.x += 1;
        }
        if (entity.size.y < 0) {
            pos1.y += 1;
            pos1.x += 1;
        }
        if (entity.size.z < 0) {
            pos1.z += 1;
            pos2.z += 1;
        }

        BlockReplaceRuleset ruleset = new BlockReplaceRuleset();
        ruleset.add(new BlockReplaceRulePosition(new BlockReplacementFull(BlockState.getInstance(entity.replaceWith)), entityPos));

        if (entity.airToVoid){
            ruleset.add(new BlockReplaceRuleBlock(new BlockReplacementFull(StructureBlocks.STRUCTURE_VOID), Block.getInstance("block_air")));
        }

        ruleset.add(new BlockReplaceRuleBlockState(new BlockReplacementFull(Block.AIR.getDefaultBlockState()), StructureBlocks.STRUCTURE_AIR));

        saveStructure(pos1, pos2, entity.getZone().zoneId, Identifier.fromString(entity.structureId), ruleset);
    }

    public static void saveStructure(IntVector3 pos1, IntVector3 pos2, String zoneID, Identifier structureID, BlockReplaceRuleset ruleset){
        // A lot of this is taken from/inspired by Cosmatica

        IntVector3 minPos = IntVector3.lesserVector(pos1, pos2);
        IntVector3 maxPos = IntVector3.greaterVector(pos1, pos2);
        Array<String> seenBlockStateStrings = new Array<>();
        Array<Integer> palletizedBlocks = new Array<>();

        for (int z = minPos.z; z < maxPos.z; ++z){
            for (int y = minPos.y; y < maxPos.y; ++y){
                for (int x = minPos.x; x < maxPos.x; ++x){
                    BlockState blockState = InGame.world.getZone(zoneID).getBlockState(x, y, z);
                    blockState = ruleset.checkRules(blockState, BlockPositionUtil.getBlockPositionAtGlobalPos(x, y, z));
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
        structureInfo.addChild("idString", new JsonValue(structureID.toString()));

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

        String saveFilePath = SaveLocation.getSaveFolderLocation() + "/mods/assets/structures/" + structureID.toString().replaceAll(":", "/") + ".zip";
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
            bedFile.delete();
            return;
        }
        bedFile.delete();

        loadStructure(new FileHandle(saveFile));

    }

    public static Structure loadStructure(Identifier structureID){
        return loadStructure(GameAssetLoader.loadAsset("%s:structures/%s.zip".formatted(structureID.namespace, structureID.name)));
    }

    private static Structure loadStructure(FileHandle structureFile){
        try {
            ZipInputStream zipInputStream = new ZipInputStream(structureFile.read());
            ZipEntry entry = zipInputStream.getNextEntry();

            Structure newStructure = new Structure();
            newStructure.assetFile = structureFile;

            while (entry != null) {
                if (entry.isDirectory()) continue;

                if (entry.getName().equalsIgnoreCase("structure_info.json")) {
                    JsonValue structureInfo = new JsonReader().parse(zipInputStream);
                    if (!structureInfo.has("structureVersion")) {
                        return null;
                    }
                    int version = structureInfo.getInt("structureVersion");
                    newStructure.version = version;

                    Identifier newStructureID;

                    switch (version) {
                        case 3 -> {
                            newStructure.size.x = structureInfo.getInt("sizeX");
                            newStructure.size.y = structureInfo.getInt("sizeY");
                            newStructure.size.z = structureInfo.getInt("sizeZ");
                            newStructureID = Identifier.fromString(structureInfo.getString("idString"));
                        }
                        case 1, 2 -> {
                            newStructure.size.x = structureInfo.getInt("sizeX");
                            newStructure.size.y = structureInfo.getInt("sizeY");
                            newStructure.size.z = structureInfo.getInt("sizeZ");
                            newStructureID = new Identifier("old", structureFile.nameWithoutExtension());
                        }
                        default -> {
                            return null;
                        }
                    }

                    ALL_STRUCTURES.put(newStructureID, newStructure);

                    zipInputStream.close();
                    return newStructure;
                }

                entry = zipInputStream.getNextEntry();
            }
        }
        catch (IOException exception){
            return null;
        }
        return null;
    }

    static {
        Array<FileHandle> structureFiles = StructureUtils.getAllFiles(Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/structures"));
        for (FileHandle f : structureFiles){
            if (f.extension().equals("zip")){
                loadStructure(f);
            }
        }
    }
}
