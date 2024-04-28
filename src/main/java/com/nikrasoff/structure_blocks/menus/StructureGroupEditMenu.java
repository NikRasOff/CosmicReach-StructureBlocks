package com.nikrasoff.structure_blocks.menus;

import com.badlogic.gdx.utils.Array;
import com.nikrasoff.structure_blocks.structure.StructureGroup;
import dev.crmodders.flux.menus.ScrollMenu;

public class StructureGroupEditMenu extends ScrollMenu {
    StructureGroup editedGroup;

    public StructureGroupEditMenu(StructureGroup group){
        this.editedGroup = group;

    }
}
