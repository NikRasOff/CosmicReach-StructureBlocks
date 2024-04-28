package com.nikrasoff.structure_blocks.menus;

public class JigsawBlockMenuCollection extends BlockMenuCollection {
    public JigsawBlockMenuCollection() {
        super();
        this.addMenu("jigsaw", new JigsawBlockMenu());
    }
}
