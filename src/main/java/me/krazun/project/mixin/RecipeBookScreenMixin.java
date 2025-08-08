package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookScreen.class)
public class RecipeBookScreenMixin {

    @Inject(method = "addRecipeBook", at = @At(value = "HEAD"), cancellable = true)
    public void kraztweaks$addRecipeBook$hideRecipeBook(CallbackInfo ci) {
        final var hideRecipeBook = KrazTweaks.CONFIG.configInstance().visualCategory.hideRecipeBook;

        if(hideRecipeBook) {
            ci.cancel();
        }
    }
}