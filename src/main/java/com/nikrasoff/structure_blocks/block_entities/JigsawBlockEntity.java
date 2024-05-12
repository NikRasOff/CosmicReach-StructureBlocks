package com.nikrasoff.structure_blocks.block_entities;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;
import com.nikrasoff.structure_blocks.blocks.JigsawBlock;
import com.nikrasoff.structure_blocks.menus.collections.BlockMenuCollection;
import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import com.nikrasoff.structure_blocks.util.IntVector3;
import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.flux.util.BlockPositionUtil;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;

import java.util.Comparator;

import static com.nikrasoff.structure_blocks.StructureBlocks.MOD_ID;

public class JigsawBlockEntity extends BlockEntity {
    public static BlockMenuCollection blockMenus;
    public static BlockEntityType<JigsawBlockEntity> BE_TYPE = new BlockEntityType<>(new Identifier(MOD_ID, "jigsaw_block_entity"), JigsawBlockEntity::new, JigsawBlock.IDENTIFIER.toString());

    public String replaceWith = "base:air[default]";
    public String name = "any";
    public String structureGroupID = "";
    public int processPriority = 1;
    public int attachmentPriority = 1;
    public int chainLength = 1;
    public boolean allowIntersections = false;

    public String facing;

    public JigsawBlockEntity(Zone zone, BlockPosition blockPos) {
        super(BE_TYPE, zone, blockPos);
        this.facing = blockPos.getBlockState().getStateParamsStr().split("=")[1];
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
        compound.putString("facing", this.facing);
        compound.putBoolean("allowIntersections", this.allowIntersections);
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
        this.allowIntersections = compound.getBoolean("allowIntersections");
    }
    public void process(Array<BoundingBox> intersectionChecks){
        this.process(this.chainLength, intersectionChecks);
    }

    public String getTargetDirection(){
        switch (this.facing){
            case "posZ" -> {
                return "negZ";
            }
            case "negZ" -> {
                return "posZ";
            }
            case "posX" -> {
                return "negX";
            }
            case "negX" -> {
                return "posX";
            }
            case "posY" -> {
                return "negY";
            }
            case "negY" -> {
                return "posY";
            }
            default -> {
                return this.facing;
            }
        }
    }

    public IntVector3 getOriginBlock(){
        IntVector3 thisPos = new IntVector3(this.getBlockPos());
        switch (this.facing){
            case "posZ" -> {
                return thisPos.add(0, 0, 1);
            }
            case "negZ" -> {
                return thisPos.add(0, 0, -1);
            }
            case "posX" -> {
                return thisPos.add(1, 0, 0);
            }
            case "negX" -> {
                return thisPos.add(-1, 0, 0);
            }
            case "posY" -> {
                return thisPos.add(0, 1, 0);
            }
            case "negY" -> {
                return thisPos.add(0, -1, 0);
            }
            default -> {
                return thisPos;
            }
        }
    }

    public void process(int chain, Array<BoundingBox> intersectChecks){
        // Magic!
        if (chain <= 0) return;

        StructureGroup targetGroup = StructureGroup.getStructureGroup(Identifier.fromString(this.structureGroupID));
        if (targetGroup == null) return;

        Array<StructureGroup.StructureGroupEntry> structureArray = targetGroup.getStructuresByConnection(this.getTargetDirection(), this.name);
        if (structureArray.isEmpty()) return;

        // Getting structures that won't intersect
        Array<StructureGroup.StructureGroupEntry> randomArray = new Array<>();
        int sumOfWeights = 0;
        for (StructureGroup.StructureGroupEntry entry : structureArray){
            if (!Structure.getStructure(Identifier.fromString(entry.structureID)).canConnectToJigsaw(this, intersectChecks)){
                continue;
            }
            randomArray.add(entry);
            sumOfWeights += entry.weight;
        }
        if (sumOfWeights == 0) return;

        // Randomly selecting a structure
        Structure targetStructure = null;
        RandomXS128 random = new RandomXS128(TimeUtils.millis());
        int randomNumber = random.nextInt(sumOfWeights);
        for (StructureGroup.StructureGroupEntry entry : randomArray){
            if (randomNumber < entry.weight) {
                targetStructure = Structure.getStructure(Identifier.fromString(entry.structureID));
                break;
            }
            randomNumber -= entry.weight;
        }
        if (targetStructure == null) return;

        ListTag<CompoundTag> jigsawTags = targetStructure.getJigsaws(this.getTargetDirection(), this.name);
        jigsawTags.sort(Comparator.comparingInt(o -> -o.getInt("attachmentPriority")));

        // Choosing a jigsaw
        IntVector3 structureOrigin = new IntVector3();
        for (CompoundTag jigsawTag : jigsawTags){
            structureOrigin = this.getOriginBlock();
            structureOrigin.x -= jigsawTag.getInt("localX");
            structureOrigin.y -= jigsawTag.getInt("localY");
            structureOrigin.z -= jigsawTag.getInt("localZ");
            if (this.allowIntersections) break;

            BoundingBox structureBB = targetStructure.getBoundingBox(structureOrigin);
            boolean flag = false;
            for (BoundingBox i : intersectChecks){
                if (i.intersects(structureBB)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) break;
        }

        targetStructure.place(this.getZone().zoneId, BlockPositionUtil.getBlockPositionAtGlobalPos(structureOrigin.toVector3()), false);
        Array<IntVector3> exceptionArray = new Array<>();
        exceptionArray.add(this.getOriginBlock());
        if (!this.allowIntersections) intersectChecks.add(targetStructure.getBoundingBox(structureOrigin));
        targetStructure.processJigsaw(structureOrigin, exceptionArray, chain - 1, intersectChecks);
        targetStructure.clearJigsawCache();
    }

    public void disappear(){
        BlockSetter.replaceBlock(this.getZone(), BlockState.getInstance(this.replaceWith), this.getBlockPos(), new Queue<>());

    }

    public void openBlockMenu(){
        blockMenus.activate(this.getBlockPos());
    }
}
