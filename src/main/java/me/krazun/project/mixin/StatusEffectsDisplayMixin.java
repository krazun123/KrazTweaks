package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.config.categories.VisualCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffectsDisplay.class)
public class StatusEffectsDisplayMixin {

    @Inject(method = "drawStatusEffects(Lnet/minecraft/client/gui/DrawContext;II)V", at = @At("HEAD"), cancellable = true)
    public void kraztweaks$drawStatusEffects$hideStatusEffects(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        final var shouldHideEffects = hideStatusEffects == VisualCategory.HideStatusEffects.BOTH ||
                hideStatusEffects == VisualCategory.HideStatusEffects.INVENTORY;

        if (shouldHideEffects) {
            ci.cancel();
        }
    }
}