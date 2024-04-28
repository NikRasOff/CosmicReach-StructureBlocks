package com.nikrasoff.structure_blocks;

import com.nikrasoff.structure_blocks.block_entities.JigsawBlockEntity;
import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.block_entities.StructureBlockRenderer;
import com.nikrasoff.structure_blocks.blocks.JigsawBlock;
import com.nikrasoff.structure_blocks.blocks.StructureBlock;
import com.nikrasoff.structure_blocks.menus.*;
import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import dev.crmodders.flux.api.v5.events.GameEvents;
import dev.crmodders.flux.api.v5.generators.BlockGenerator;
import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockState;
import org.quiltmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.utils.BEUtils;

public class StructureBlocks implements ModInitializer {
	public static ConfirmWindow CONFIRM_WINDOW;
	public static final String MOD_ID = "structure_blocks";
	public static final Logger LOGGER = LoggerFactory.getLogger("Structure Blocks");
	public static final int STRUCTURE_SAVE_VERSION = 2;

	public static BlockState STRUCTURE_VOID;
	public static BlockState STRUCTURE_AIR;

	@Override
	public void onInitialize(ModContainer modContainer) {
		System.out.println("Initialising Structure Blocks!");

		FluxRegistries.BLOCKS.register(StructureBlock.IDENTIFIER, new StructureBlock());
		FluxRegistries.BLOCKS.register(JigsawBlock.IDENTIFIER, new JigsawBlock());
		String[] blockIds = {
				"structure_void",
				"structure_air"
		};
		for (String block : blockIds){
			FluxRegistries.BLOCKS.register(new Identifier(MOD_ID, block), BlockGenerator::createGenerator);
		}

		BlockEntityRegistries.register(StructureBlockEntity.BE_TYPE);
		BlockEntityRegistries.register(JigsawBlockEntity.BE_TYPE);

		GameEvents.ON_POST_INIT.register(() -> {
			CONFIRM_WINDOW = new ConfirmWindow();
			STRUCTURE_VOID = BlockState.getInstance("structure_blocks:structure_void[default]");
			STRUCTURE_AIR = BlockState.getInstance("structure_blocks:structure_air[default]");
			StructureBlockEntity.blockMenus = new StructureBlockMenuCollection();
			JigsawBlockEntity.blockMenus = new JigsawBlockMenuCollection();
			BEUtils.renderDispatcher.registerRender(StructureBlockEntity.BE_TYPE, new StructureBlockRenderer());
		});
	}
}

