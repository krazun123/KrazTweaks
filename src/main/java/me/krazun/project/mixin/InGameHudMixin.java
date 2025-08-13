package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.config.categories.VisualCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    public void kraztweaks$renderStatusEffectOverlay$hideStatusEffects(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        final var shouldHideEffects = hideStatusEffects == VisualCategory.HideStatusEffects.BOTH ||
                        hideStatusEffects == VisualCategory.HideStatusEffects.HUD;

        if(shouldHideEffects) {
            ci.cancel();
        }
    }
}