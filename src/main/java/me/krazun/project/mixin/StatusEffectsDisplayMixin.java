package me.krazun.project.mixin;

import me.krazun.project.utils.MiscUtils;
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
        final var shouldHideEffects = MiscUtils.shouldHideStatusEffectsForInventory();

        if(shouldHideEffects) {
            ci.cancel();
        }
    }
}