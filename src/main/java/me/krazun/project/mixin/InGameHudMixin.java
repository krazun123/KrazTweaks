package me.krazun.project.mixin;

import me.krazun.project.utils.MiscUtils;
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
        final var shouldHideEffects = MiscUtils.shouldHideStatusEffectsForHUD();

        if(shouldHideEffects) {
            ci.cancel();
        }
    }
}