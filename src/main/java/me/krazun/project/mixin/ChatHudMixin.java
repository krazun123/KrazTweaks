package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.utils.MiscUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    public int kraztweaks$render$includeChatTimestamps(DrawContext context,
                                                       TextRenderer textRenderer,
                                                       OrderedText text,
                                                       int x,
                                                       int y,
                                                       int color)
    {
        if(KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps) {
            visibleMessages.stream().filter(visible -> visible.content().equals(text)).findAny().ifPresentOrElse(visible -> {
                final var localTime = MiscUtils.getLocalTimeByForwardedTicks(visible.addedTime());
                final var formattedTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                final var timestamp = OrderedText.styledForwardsVisitedString("[" + formattedTime + "] ", Style.of(
                        Optional.ofNullable(TextColor.fromFormatting(Formatting.DARK_GRAY)),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ));
                context.drawTextWithShadow(textRenderer, OrderedText.concat(timestamp, text), x, y, color);
            }, () -> context.drawTextWithShadow(textRenderer, text, x, y, color));
            return 0;
        } else {
            return context.drawTextWithShadow(textRenderer, text, x, y, color);
        }
    }

    // TODO: Breaks old messages, when a message is sent before feature enabled.
    @Redirect(method = "addVisibleMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;"))
    public List<OrderedText> kraztweaks$addVisibleMessage$includeChatTimestamps(StringVisitable message, int width, TextRenderer textRenderer) {
        if(KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps) {
            return ChatMessages.breakRenderedChatMessageLines(message, width - textRenderer.getWidth("[HH:mm:ss] "), textRenderer);
        } else {
            return ChatMessages.breakRenderedChatMessageLines(message, width, textRenderer);
        }
    }
}
