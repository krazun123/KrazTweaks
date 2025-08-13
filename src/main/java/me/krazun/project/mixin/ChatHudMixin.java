package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.utils.MiscUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Unique
    private final List<ChatHudLine.Visible> pseudoVisibleMessages = Lists.newArrayList();

    @Shadow
    private int scrolledLines;
    @Shadow
    private boolean hasUnreadNewMessages;

    @Shadow
    public abstract boolean isChatFocused();

    @Shadow
    public abstract void scroll(int scroll);

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract double getChatScale();

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$render$getVisibleMessagesSize$includeChatTimestamps(List<ChatHudLine.Visible> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$render$getVisibleMessage$includeChatTimestamps(List<ChatHudLine.Visible> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Inject(method = "addVisibleMessage", at = @At("HEAD"))
    public void kraztweaks$addVisibleMessage$includeChatTimestamps(@NotNull ChatHudLine message, CallbackInfo ci) {
        int i = MathHelper.floor((double) this.getWidth() / this.getChatScale());

        MessageIndicator.Icon icon = message.getIcon();
        if (icon != null) {
            i -= icon.width + 4 + 2;
        }

        final var pattern = KrazTweaks.CONFIG.configInstance().chatCategory.chatTimestampPattern;
        final var widthOfPattern = MinecraftClient.getInstance().textRenderer.getWidth("[" + pattern + "] ");

        List<OrderedText> listForPseusdo = ChatMessages.breakRenderedChatMessageLines(
                message.content(),
                i - widthOfPattern,
                MinecraftClient.getInstance().textRenderer
        );

        boolean bl = this.isChatFocused();

        for (int j = 0; j < listForPseusdo.size(); ++j) {
            OrderedText orderedText = listForPseusdo.get(j);

            if (bl && this.scrolledLines > 0) {
                this.hasUnreadNewMessages = true;
                this.scroll(1);
            }

            boolean endOfEntry = j == listForPseusdo.size() - 1;
            OrderedText fullLine = j == 0
                    ? OrderedText.concat(kraztweaks$getTimestampText(message.creationTick(), pattern), orderedText)
                    : orderedText;

            if(message.content().getString().isBlank()) {
                this.pseudoVisibleMessages.addFirst(new ChatHudLine.Visible(message.creationTick(), orderedText, message.indicator(), endOfEntry));
            } else this.pseudoVisibleMessages.addFirst(new ChatHudLine.Visible(message.creationTick(), fullLine, message.indicator(), endOfEntry));
        }
        while (this.pseudoVisibleMessages.size() > 100) {
            this.pseudoVisibleMessages.removeLast();
        }
    }

    @Inject(method = "clear", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V"))
    public void kraztweaks$clear$includeChatTimestamps(boolean clearHistory, CallbackInfo ci) {
        pseudoVisibleMessages.clear();
    }

    @Inject(method = "refresh", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V"))
    public void kraztweaks$refresh$includeChatTimestamps(CallbackInfo ci) {
        pseudoVisibleMessages.clear();
    }

    @Redirect(method = "scroll", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$scroll$includeChatTimestamps(List<ChatHudLine.Visible> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getTextStyleAt", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$getTextStyleAt$size$includeChatTimestamps(List<ChatHudLine.Visible> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getTextStyleAt", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$getTextStyleAt$get$includeChatTimestamps(List<ChatHudLine.Visible> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Redirect(method = "getIndicatorAt", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$getIndicatorAt$size$includeChatTimestamps(List<ChatHudLine.Visible> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getIndicatorAt", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$getIndicatorAt$get$includeChatTimestamps(List<ChatHudLine.Visible> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Redirect(method = "getMessageIndex", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$getMessageIndex$get$includeChatTimestamps(List<ChatHudLine.Visible> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Redirect(method = "getMessageLineIndex", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    public int kraztweaks$getMessageLineIndex$size$includeChatTimestamps(List<ChatHudLine.Visible> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getMessageLineIndex", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 1))
    public int kraztweaks$getMessageLineIndex$size2$includeChatTimestamps(List<ChatHudLine.Visible> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Unique
    private OrderedText kraztweaks$getTimestampText(int tick, String pattern) {
        var time = MiscUtils.getLocalTimeByForwardedTicks(tick);
        var formatted = MiscUtils.parseLocalDateTimeOfPattern(time, pattern);

        if (formatted == null) {
            formatted = MiscUtils.parseLocalDateTimeOfPattern(time, "HH:mm:ss");
            if (formatted == null) throw new RuntimeException("Unreachable");
        }

        return OrderedText.styledForwardsVisitedString("[%s] ".formatted(formatted), Style.EMPTY.withColor(Formatting.DARK_GRAY));
    }
}