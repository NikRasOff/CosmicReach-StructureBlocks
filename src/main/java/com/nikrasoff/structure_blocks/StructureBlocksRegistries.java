package com.nikrasoff.structure_blocks;

import com.nikrasoff.structure_blocks.structure.Structure;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import dev.crmodders.flux.registry.registries.FreezingRegistry;

public class StructureBlocksRegistries {
    public static FreezingRegistry<Structure> STRUCTURES = FreezingRegistry.create();
    public static FreezingRegistry<StructureGroup> STRUCTURE_GROUPS = FreezingRegistry.create();
}
