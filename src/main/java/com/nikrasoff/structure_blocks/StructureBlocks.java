package com.nikrasoff.structure_blocks;

import com.nikrasoff.structure_blocks.block_entities.StructureBlockEntity;
import com.nikrasoff.structure_blocks.block_entities.StructureBlockRenderer;
import com.nikrasoff.structure_blocks.blocks.StructureBlock;
import com.nikrasoff.structure_blocks.menus.ConfirmWindow;
import com.nikrasoff.structure_blocks.menus.StructureBlockLoadMenu;
import com.nikrasoff.structure_blocks.menus.StructureBlockSaveMenu;
import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import dev.crmodders.flux.api.v5.events.GameEvents;
import dev.crmodders.flux.registry.FluxRegistries;
import org.quiltmc.loader.api.ModContainer;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.utils.BEUtils;

public class StructureBlocks implements ModInitializer {
	public static final String MOD_ID = "structure_blocks";
	public static final int STRUCTURE_SAVE_VERSION = 1;

	@Override
	public void onInitialize(ModContainer modContainer) {
		System.out.println("Initialising Structure Blocks!");

		FluxRegistries.BLOCKS.register(StructureBlock.IDENTIFIER, new StructureBlock());
		BlockEntityRegistries.register(StructureBlockEntity.BE_TYPE);

		GameEvents.ON_POST_INIT.register(() -> {
			StructureBlockEntity.STRUCTURE_SAVE_MENU = new StructureBlockSaveMenu();
			StructureBlockEntity.STRUCTURE_LOAD_MENU = new StructureBlockLoadMenu();
			StructureBlockEntity.CONFIRM_WINDOW = new ConfirmWindow();
			BEUtils.renderDispatcher.registerRender(StructureBlockEntity.BE_TYPE, new StructureBlockRenderer());
		});
	}
}

