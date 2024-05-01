package com.nikrasoff.structure_blocks.menus.collections;

import com.nikrasoff.structure_blocks.menus.StructureBlockLoadMenu;
import com.nikrasoff.structure_blocks.menus.StructureBlockSaveMenu;

public class StructureBlockMenuCollection extends BlockMenuCollection {
    public StructureBlockMenuCollection() {
        super();
        this.addMenu("save", new StructureBlockSaveMenu());
        this.addMenu("load", new StructureBlockLoadMenu());
    }
}
