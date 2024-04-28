package com.nikrasoff.structure_blocks.mixins;

import com.nikrasoff.structure_blocks.util.StructureUtils;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.OptionsMenu;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsMenu.class)
public abstract class OptionsMenuMixin extends GameState {
    @Inject(method = "<init>", at = @At("TAIL"))
    void onCreate(GameState previousState, CallbackInfo ci){
        UIElement newButton = StructureUtils.createStructureGroupButton(-5, -60, 250, 50);
        newButton.hAnchor = HorizontalAnchor.RIGHT_ALIGNED;
        newButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
        this.uiObjects.add(newButton);
    }
}
