package com.nikrasoff.structure_blocks.util;

import com.badlogic.gdx.files.FileHandle;
import com.nikrasoff.structure_blocks.StructureBlocks;
import com.nikrasoff.structure_blocks.structure.Structure;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.flux.util.BlockPositionUtil;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.handlers.BlockEntityLoadHandler;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.bed.utils.DataFixerUtil;

import java.io.IOException;
public class BlockEntitySaver {
    private static final CompoundTag saveCompound = new CompoundTag();

    static public FileHandle saveBlockEntities(IntVector3 pos1, IntVector3 pos2, String filePath){
        IntVector3 minPos = IntVector3.lesserVector(pos1, pos2);
        IntVector3 maxPos = IntVector3.greaterVector(pos1, pos2);

        ListTag<CompoundTag> areaBlockEntityRoot = new ListTag<>(CompoundTag.class);

        for (int x = minPos.x; x < maxPos.x; ++x){
            for (int y = minPos.y; y < maxPos.y; ++y){
                for (int z = minPos.z; z < maxPos.z; ++z){
                    BlockPosition curBlockPos = BlockPositionUtil.getBlockPositionAtGlobalPos(x, y, z);
                    BlockEntity blockEntity = BEUtils.getBlockEntity(curBlockPos);

                    if (blockEntity == null) continue;

                    if (!blockEntity.getType().isBlockSupported(curBlockPos.getBlockState().getBlockId())) continue;

                    CompoundTag entityCompond = blockEntity.writeData(new CompoundTag());
                    entityCompond.putInt("localX", x - minPos.x);
                    entityCompond.putInt("localY", y - minPos.y);
                    entityCompond.putInt("localZ", z - minPos.z);
                    areaBlockEntityRoot.add(entityCompond);
                }
            }
        }

        if (areaBlockEntityRoot.size() > 0){
            saveCompound.put("area", areaBlockEntityRoot);
        }

        FileHandle saveFile = new FileHandle(filePath);
        saveFile.parent().mkdirs();
        saveCompound.putInt("version", 1);
        try {
            NBTUtil.write(saveCompound, saveFile.file(), true);
        }
        catch (IOException exception){
            StructureBlocks.LOGGER.info("Failed to save Block Entities: " + exception);
        }
        saveCompound.clear();
        return saveFile;
    }

    public static void loadBlockEntities(Zone zone, IntVector3 origin, FileHandle file){
        if (!file.exists()) return;
        CompoundTag bedCompound = BlockEntityLoadHandler.readBED(file.file());

        Tag<?> chunkTag = bedCompound.get("area");
        if (!(chunkTag instanceof ListTag)) {
            return;
        }
        ListTag<CompoundTag> beTagList = ((ListTag)chunkTag).asCompoundTagList();

        int formatVersion = bedCompound.getInt("version");
        AccessableRegistry<BlockEntityType<?>> registry = BlockEntityRegistries.BLOCK_ENTITIES.access();
        beTagList.forEach((beTag) -> {
            Identifier id = Identifier.fromString(beTag.getString("id"));
            if (registry.contains(id)) {
                if (formatVersion < 1) {
                    DataFixerUtil.applyFixes(bedCompound, beTagList, beTag, formatVersion);
                }

                BlockEntityType<?> type = registry.get(id);
                int lx = beTag.getInt("localX");
                int ly = beTag.getInt("localY");
                int lz = beTag.getInt("localZ");
                IntVector3 globalPos = new IntVector3(lx, ly, lz).add(origin);
                BlockPosition bePos = BlockPositionUtil.getBlockPositionAtGlobalPos(globalPos.toVector3());
                if (type.isBlockSupported(zone.getBlockState(globalPos.toVector3()).getBlock().getStringId())) {
                    BlockEntity blockEntity = type.instantiate(zone, bePos);

                    try {
                        blockEntity.readData(beTag);
                    } catch (ClassCastException var16) {
                        BECraft.LOGGER.error("ClastCastException occurred during nbt parsing of " + blockEntity.getClass().getSimpleName() + " block entity. The value type probably doesn't much the one in the writeData()");
                    }

                    blockEntity.setWasSaved(true);
                    BEUtils.addBlockEntity(zone, blockEntity);
                }
            } else {
                BECraft.LOGGER.warn("The block entity registry doesn't contain " + id + ". It was removed?");
            }
        });
    }
}
