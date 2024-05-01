package com.nikrasoff.structure_blocks.menus.collections;

import com.nikrasoff.structure_blocks.menus.JigsawBlockMenu;

public class JigsawBlockMenuCollection extends BlockMenuCollection {
    public JigsawBlockMenuCollection() {
        super();
        this.addMenu("jigsaw", new JigsawBlockMenu());
    }
}
