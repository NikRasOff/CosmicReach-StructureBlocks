package com.nikrasoff.structure_blocks.blocks;

import com.nikrasoff.structure_blocks.block_entities.JigsawBlockEntity;
import dev.crmodders.flux.api.v5.generators.BlockGenerator;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockWithEntity;
import ru.nern.becraft.bed.utils.BEUtils;

import static com.nikrasoff.structure_blocks.StructureBlocks.MOD_ID;

public class JigsawBlock extends BlockWithEntity {
    BlockGenerator generator = BlockGenerator.createGenerator();
    public static Identifier IDENTIFIER = new Identifier(MOD_ID, "jigsaw_block");

    @Override
    public BlockEntity createBlockEntity(Zone zone, BlockPosition blockPosition) {
        return new JigsawBlockEntity(zone, blockPosition);
    }

    @Override
    public void onInteract(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        BlockEntity entity = BEUtils.getBlockEntity(position);
        if (entity instanceof JigsawBlockEntity be){
            be.openBlockMenu();
        }
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }
}
