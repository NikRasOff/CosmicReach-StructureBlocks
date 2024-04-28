package com.nikrasoff.structure_blocks.menus;

public class StructureBlockMenuCollection extends BlockMenuCollection {
    public StructureBlockMenuCollection() {
        super();
        this.addMenu("save", new StructureBlockSaveMenu());
        this.addMenu("load", new StructureBlockLoadMenu());
    }
}
