package com.nikrasoff.structure_blocks.block_entities;

import com.nikrasoff.structure_blocks.blocks.StructureBlock;
import com.nikrasoff.structure_blocks.menus.ConfirmWindow;
import com.nikrasoff.structure_blocks.menus.StructureBlockLoadMenu;
import com.nikrasoff.structure_blocks.menus.StructureBlockSaveMenu;
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
    public static StructureBlockSaveMenu STRUCTURE_SAVE_MENU;
    public static StructureBlockLoadMenu STRUCTURE_LOAD_MENU;
    public static ConfirmWindow CONFIRM_WINDOW;
    public IntVector3 offset = new IntVector3();
    public IntVector3 size = new IntVector3();
    public String replaceWith = "base:air[default]";
    public String structureId = "structure_blocks:example/house_small";
    public boolean saveMode = true;
    public boolean airToVoid = false;

    public Structure currentStructure;
    public String structureOutput = "";

    public static BlockEntityType<StructureBlockEntity> BE_TYPE = new BlockEntityType<>(new Identifier(MOD_ID, "structure_block_entity"), StructureBlockEntity::new, StructureBlock.IDENTIFIER.toString());
    public StructureBlockEntity(Zone zone, BlockPosition blockPos) {
        super(BE_TYPE, zone, blockPos);
    }

    @Override
    public CompoundTag writeData(CompoundTag compound) {
        super.writeData(compound);
        compound.putInt("offsetX", this.offset.x);
        compound.putInt("offsetY", this.offset.y);
        compound.putInt("offsetZ", this.offset.z);
        compound.putInt("sizeX", this.size.x);
        compound.putInt("sizeY", this.size.y);
        compound.putInt("sizeZ", this.size.z);
        compound.putString("replaceWith", this.replaceWith);
        compound.putString("structureId", this.structureId);
        compound.putBoolean("saveMode", this.saveMode);
        compound.putBoolean("airToVoid", this.airToVoid);
        return compound;
    }

    @Override
    public void readData(CompoundTag compound) {
        super.readData(compound);
        this.offset.x = compound.getInt("offsetX");
        this.offset.y = compound.getInt("offsetY");
        this.offset.z = compound.getInt("offsetZ");
        this.size.x = compound.getInt("sizeX");
        this.size.y = compound.getInt("sizeY");
        this.size.z = compound.getInt("sizeZ");
        this.replaceWith = compound.getString("replaceWith");
        this.structureId = compound.getString("structureId");
        this.saveMode = compound.getBoolean("saveMode");
        this.airToVoid = compound.getBoolean("airToVoid");
    }

    public void loadStructure(){
        this.currentStructure = Structure.loadStructure(Identifier.fromString(this.structureId));
        if (this.currentStructure != null){
            this.size = this.currentStructure.size.cpy();
        }
    }

    public void placeStructure(){
        if (this.currentStructure == null){
            this.loadStructure();
            if (this.currentStructure == null) return;
        }
        this.currentStructure.place(this.getZone().zoneId, this.getBlockPos().getOffsetBlockPos(this.getZone(), this.offset.x, this.offset.y, this.offset.z));
    }

    public void openBlockMenu(){
        if (this.saveMode) STRUCTURE_SAVE_MENU.switchToThisState(this);
        else STRUCTURE_LOAD_MENU.switchToThisState(this);
    }
}
