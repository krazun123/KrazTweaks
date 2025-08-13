package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;addSelectableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    public void kraztweaks$init$compactInputBox(CallbackInfo ci) {
        final var compactInputBox = KrazTweaks.CONFIG.configInstance().chatCategory.compactInputBox;

        if (compactInputBox) {
            chatField.setWidth(kraztweaks$compactInputBox$width() - 8);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    public void kraztweaks$render$fill$CompactInputBox(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        final var compactInputBox = KrazTweaks.CONFIG.configInstance().chatCategory.compactInputBox;

        if (compactInputBox) {
            instance.fill(x1, y1, kraztweaks$compactInputBox$width() + 4, y2, color);
        } else {
            instance.fill(x1, y1, x2, y2, color);
        }
    }

    @Unique
    public int kraztweaks$compactInputBox$width() {
        final var client = MinecraftClient.getInstance();

        final double chatScale = client.options.getChatScale().getValue();
        final double chatWidth = client.options.getChatWidth().getValue();

        final int calculatedWith = MathHelper.ceil((float) ChatHud.getWidth(chatWidth / chatScale));

        return calculatedWith + 8;
    }
}