package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.config.categories.VisualCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectsInInventory.class)
public class EffectsInInventoryMixin {

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    public void kraztweaks$renderEffects$hideStatusEffects(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        final var shouldHideEffects = hideStatusEffects == VisualCategory.HideStatusEffects.BOTH ||
                hideStatusEffects == VisualCategory.HideStatusEffects.INVENTORY;

        if (shouldHideEffects) {
            ci.cancel();
        }
    }
}