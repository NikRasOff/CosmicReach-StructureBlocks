package com.nikrasoff.structure_blocks.block_entities;

import com.nikrasoff.structure_blocks.blocks.JigsawBlock;
import com.nikrasoff.structure_blocks.menus.collections.BlockMenuCollection;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;

import static com.nikrasoff.structure_blocks.StructureBlocks.MOD_ID;

public class JigsawBlockEntity extends BlockEntity {
    public static BlockMenuCollection blockMenus;
    public static BlockEntityType<JigsawBlockEntity> BE_TYPE = new BlockEntityType<>(new Identifier(MOD_ID, "jigsaw_block_entity"), JigsawBlockEntity::new, JigsawBlock.IDENTIFIER.toString());

    public String replaceWith = "base:air[default]";
    public String name = "default";
    public String structureGroupID = "structure_blocks:example_group";
    public int processPriority = 1;
    public int attachmentPriority = 1;
    public int chainLength = 1;

    public JigsawBlockEntity(Zone zone, BlockPosition blockPos) {
        super(BE_TYPE, zone, blockPos);
    }

    @Override
    public CompoundTag writeData(CompoundTag compound) {
        super.writeData(compound);
        compound.putInt("processPriority", this.processPriority);
        compound.putInt("attachmentPriority", this.attachmentPriority);
        compound.putInt("chainLength", this.chainLength);
        compound.putString("blockName", this.name);
        compound.putString("replaceWith", this.replaceWith);
        compound.putString("structureGroupID", this.structureGroupID);
        return compound;
    }

    @Override
    public void readData(CompoundTag compound) {
        super.readData(compound);
        this.processPriority = compound.getInt("processPriority");
        this.attachmentPriority = compound.getInt("attachmentPriority");
        this.chainLength = compound.getInt("chainLength");
        this.name = compound.getString("blockName");
        this.replaceWith = compound.getString("replaceWith");
        this.structureGroupID = compound.getString("structureGroupID");
    }

    public void openBlockMenu(){
        blockMenus.activate(this.getBlockPos());
    }
}
