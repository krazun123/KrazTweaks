package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.utils.ChatHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
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

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void kraztweaks$mouseClicked$copyChat(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        final var keybindMatch = KrazTweaks.CONFIG.configInstance().chatCategory.copyChat.matches(button, true);

        if(keybindMatch) {
            final var visible = ChatHelper.getLineAt(mouseX, mouseY);
            if(visible == null) return;

            MinecraftClient.getInstance().getToastManager().add(new SystemToast(
                    new SystemToast.Type(),
                    Text.literal("KrazTweaks"),
                    Text.literal("     Copied Chat Message").withColor(5592405)
            ));
            MinecraftClient.getInstance().keyboard.setClipboard(visible.content().getString().replaceAll("§", "&"));
        }
    }

    @Unique
    public int kraztweaks$compactInputBox$width() {
        final var client = MinecraftClient.getInstance();

        final double chatScale = client.options.getChatScale().getValue();
        final double chatWidth = client.options.getChatWidth().getValue();

        final int calculatedWith = MathHelper.ceil((float) ChatHud.getWidth(chatWidth / chatScale));
        final var minimumWidth = KrazTweaks.CONFIG.configInstance().chatCategory.compactInputBoxMinimumWidth;

        if(calculatedWith < minimumWidth) {
            return minimumWidth + 8;
        }

        return calculatedWith + 8;
    }
}