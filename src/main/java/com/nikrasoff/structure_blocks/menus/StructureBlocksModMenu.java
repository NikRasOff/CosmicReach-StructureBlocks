package com.nikrasoff.structure_blocks.menus;

import dev.crmodders.modmenu.api.ConfigScreenFactory;
import dev.crmodders.modmenu.api.ModMenuApi;

public class StructureBlocksModMenu implements ModMenuApi {
    public StructureBlocksModMenu(){}

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return StructureGroupsMenu::new;
    }
}
