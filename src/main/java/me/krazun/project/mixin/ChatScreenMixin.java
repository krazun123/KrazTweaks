package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.utils.ChatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Shadow
    protected EditBox input;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ChatScreen;addWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public void kraztweaks$init$compactInputBox(CallbackInfo ci) {
        final var compactInputBox = KrazTweaks.CONFIG.configInstance().chatCategory.compactInputBox;

        if (compactInputBox) {
            input.setWidth(kraztweaks$compactInputBox$width() - 8);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
    public void kraztweaks$render$fill$CompactInputBox(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
        final var compactInputBox = KrazTweaks.CONFIG.configInstance().chatCategory.compactInputBox;

        if (compactInputBox) {
            guiGraphics.fill(x1, y1, kraztweaks$compactInputBox$width() + 4, y2, color);
        } else {
            guiGraphics.fill(x1, y1, x2, y2, color);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void kraztweaks$mouseClicked$copyChat(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        final var keybindMatch = KrazTweaks.CONFIG.configInstance().chatCategory.copyChat.matches(button, true);

        if (keybindMatch) {
            final var visible = ChatHelper.getLineAt(mouseX, mouseY);
            if (visible == null) return;

            Minecraft.getInstance().getToastManager().addToast(new SystemToast(
                    new SystemToast.SystemToastId(),
                    Component.literal("KrazTweaks"),
                    Component.literal("     Copied Chat Message").withColor(5592405)
            ));
            Minecraft.getInstance().keyboardHandler.setClipboard(visible.content().getString().replaceAll("§", "&"));
        }
    }

    @Unique
    public int kraztweaks$compactInputBox$width() {
        final var client = Minecraft.getInstance();

        final double chatScale = client.options.chatScale().get();
        final double chatWidth = client.options.chatWidth().get();

        final int calculatedWith = (int) Math.ceil((float) ChatComponent.getWidth(chatWidth / chatScale));
        final var minimumWidth = KrazTweaks.CONFIG.configInstance().chatCategory.compactInputBoxMinimumWidth;

        if (calculatedWith < minimumWidth) {
            return minimumWidth + 8;
        }

        return calculatedWith + 8;
    }
}