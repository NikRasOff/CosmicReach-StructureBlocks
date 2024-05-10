package com.nikrasoff.structure_blocks.block_entities;

import com.nikrasoff.structure_blocks.blocks.StructureBlock;
import com.nikrasoff.structure_blocks.menus.collections.BlockMenuCollection;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.util.IntVector3;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;

import static com.nikrasoff.structure_blocks.StructureBlocks.MOD_ID;

public class StructureBlockEntity extends BlockEntity {
    public static BlockMenuCollection blockMenus;
    public IntVector3 offset = new IntVector3();
    public IntVector3 size = new IntVector3();
    public String replaceWith = "base:air[default]";
    public String structureId = "";
    public int structureBlockMode = 0;
    public boolean airToVoid = false;
    public boolean processJigsawBlocks = true;

    public Structure currentStructure;

    public static BlockEntityType<StructureBlockEntity> BE_TYPE = new BlockEntityType<>(new Identifier(MOD_ID, "structure_block_entity"), StructureBlockEntity::new, StructureBlock.IDENTIFIER.toString());
    public StructureBlockEntity(Zone zone, BlockPosition blockPos) {
        super(BE_TYPE, zone, blockPos);
    }

    @Override
    public CompoundTag writeData(CompoundTag compound) {
        super.writeData(compound);
        compound.putInt("blockMode", this.structureBlockMode);
        switch (this.structureBlockMode){
            case 0 -> {
                compound.putInt("offsetX", this.offset.x);
                compound.putInt("offsetY", this.offset.y);
                compound.putInt("offsetZ", this.offset.z);
                compound.putInt("sizeX", this.size.x);
                compound.putInt("sizeY", this.size.y);
                compound.putInt("sizeZ", this.size.z);
                compound.putString("replaceWith", this.replaceWith);
                compound.putString("structureId", this.structureId);
                compound.putBoolean("airToVoid", this.airToVoid);
            }
            case 1 -> {
                compound.putInt("offsetX", this.offset.x);
                compound.putInt("offsetY", this.offset.y);
                compound.putInt("offsetZ", this.offset.z);
                compound.putString("structureId", this.structureId);
                compound.putInt("sizeX", this.size.x);
                compound.putInt("sizeY", this.size.y);
                compound.putInt("sizeZ", this.size.z);
                compound.putBoolean("processJigsaw", this.processJigsawBlocks);
            }
        }
        return compound;
    }

    @Override
    public void readData(CompoundTag compound) {
        super.readData(compound);
        this.structureBlockMode = compound.getInt("blockMode");
        switch (this.structureBlockMode){
            case 0 -> {
                this.offset.x = compound.getInt("offsetX");
                this.offset.y = compound.getInt("offsetY");
                this.offset.z = compound.getInt("offsetZ");
                this.size.x = compound.getInt("sizeX");
                this.size.y = compound.getInt("sizeY");
                this.size.z = compound.getInt("sizeZ");
                this.replaceWith = compound.getString("replaceWith");
                this.structureId = compound.getString("structureId");
                this.airToVoid = compound.getBoolean("airToVoid");
            }
            case 1 -> {
                this.offset.x = compound.getInt("offsetX");
                this.offset.y = compound.getInt("offsetY");
                this.offset.z = compound.getInt("offsetZ");
                this.structureId = compound.getString("structureId");
                this.size.x = compound.getInt("sizeX");
                this.size.y = compound.getInt("sizeY");
                this.size.z = compound.getInt("sizeZ");
                this.processJigsawBlocks = compound.getBoolean("processJigsaw");
            }
        }
    }

    public void loadStructure(){
        this.currentStructure = Structure.getStructure(Identifier.fromString(this.structureId));
        if (this.currentStructure != null){
            this.size = this.currentStructure.size.cpy();
        }
        else {
            this.size = new IntVector3();
        }
    }

    public void placeStructure(){
        this.loadStructure();
        if (this.currentStructure == null) return;
        this.currentStructure.place(this.getZone().zoneId, this.getBlockPos().getOffsetBlockPos(this.getZone(), this.offset.x, this.offset.y, this.offset.z), this.processJigsawBlocks);
    }

    public void openBlockMenu(){
        blockMenus.switchToMenuByIndex(this.structureBlockMode);
        blockMenus.activate(this.getBlockPos());
    }
}
