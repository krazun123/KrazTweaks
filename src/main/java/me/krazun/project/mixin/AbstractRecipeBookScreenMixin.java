package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRecipeBookScreen.class)
public class AbstractRecipeBookScreenMixin {

    @Inject(method = "initButton", at = @At(value = "HEAD"), cancellable = true)
    public void kraztweaks$initButton$hideRecipeBook(CallbackInfo ci) {
        final var hideRecipeBook = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.hideRecipeBook;

        if (hideRecipeBook) {
            ci.cancel();
        }
    }
}