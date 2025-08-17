package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import me.krazun.project.utils.ChatHelper;
import me.krazun.project.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {

    @Shadow
    private int chatScrollbarPos;

    @Shadow
    private boolean newMessageSinceScroll;

    @Shadow
    public abstract boolean isChatFocused();

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract double getScale();

    @Shadow
    public abstract void scrollChat(int i);

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$render$getVisibleMessagesSize$includeChatTimestamps(List<GuiMessage.Line> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "forEachLine", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$render$getVisibleMessage$includeChatTimestamps(List<GuiMessage.Line> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Inject(method = "addMessageToDisplayQueue", at = @At("HEAD"))
    public void kraztweaks$addMessageToDisplayQueue$includeChatTimestamps(@NotNull GuiMessage guiMessage, CallbackInfo ci) {
        int i = (int) Math.floor((double) this.getWidth() / getScale());

        GuiMessageTag.Icon icon = guiMessage.icon();
        if (icon != null) {
            i -= icon.width + 4 + 2;
        }

        final var pattern = KrazTweaks.CONFIG.configInstance().chatCategory.chatTimestampPattern;
        final var widthOfPattern = Minecraft.getInstance().font.width("[" + pattern + "] ");

        List<FormattedCharSequence> listForPseusdo = ComponentRenderUtils.wrapComponents(
                guiMessage.content(),
                i - widthOfPattern,
                Minecraft.getInstance().font
        );

        boolean bl = this.isChatFocused();

        for (int j = 0; j < listForPseusdo.size(); ++j) {
            FormattedCharSequence formattedCharSequence = listForPseusdo.get(j);

            if (bl && this.chatScrollbarPos > 0) {
                this.newMessageSinceScroll = true;
                this.scrollChat(1);
            }

            boolean endOfEntry = j == listForPseusdo.size() - 1;
            FormattedCharSequence fullLine = j == 0
                    ? FormattedCharSequence.composite(kraztweaks$getTimestampText(guiMessage.addedTime(), pattern), formattedCharSequence)
                    : formattedCharSequence;

            if (guiMessage.content().getString().isBlank()) {
                ChatHelper.pseudoVisibleMessages.addFirst(new GuiMessage.Line(guiMessage.addedTime(), formattedCharSequence, guiMessage.tag(), endOfEntry));
            } else
                ChatHelper.pseudoVisibleMessages.addFirst(new GuiMessage.Line(guiMessage.addedTime(), fullLine, guiMessage.tag(), endOfEntry));
        }
        while (ChatHelper.pseudoVisibleMessages.size() > 100) {
            ChatHelper.pseudoVisibleMessages.removeLast();
        }
    }

    @Inject(method = "clearMessages", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V"))
    public void kraztweaks$clearMessages$includeChatTimestamps(boolean clearHistory, CallbackInfo ci) {
        ChatHelper.pseudoVisibleMessages.clear();
    }

    @Inject(method = "refreshTrimmedMessages", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V"))
    public void kraztweaks$refreshTrimmedMessages$includeChatTimestamps(CallbackInfo ci) {
        ChatHelper.pseudoVisibleMessages.clear();
    }

    @Redirect(method = "scrollChat", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$scrollChat$includeChatTimestamps(List<GuiMessage.Line> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getClickedComponentStyleAt", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$getClickedComponentStyleAt$size$includeChatTimestamps(List<GuiMessage.Line> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getClickedComponentStyleAt", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$getClickedComponentStyleAt$get$includeChatTimestamps(List<GuiMessage.Line> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Redirect(method = "getMessageTagAt", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    public int kraztweaks$getMessageTagAt$size$includeChatTimestamps(List<GuiMessage.Line> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getMessageTagAt", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$getMessageTagAt$get$includeChatTimestamps(List<GuiMessage.Line> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Redirect(method = "getMessageEndIndexAt", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    public Object kraztweaks$getMessageEndIndexAt$get$includeChatTimestamps(List<GuiMessage.Line> instance, int i) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.get(i);
        }

        return instance.get(i);
    }

    @Redirect(method = "getMessageLineIndexAt", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    public int kraztweaks$getMessageLineIndexAt$size$includeChatTimestamps(List<GuiMessage.Line> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Redirect(method = "getMessageLineIndexAt", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 1))
    public int kraztweaks$getMessageLineIndexAt$size2$includeChatTimestamps(List<GuiMessage.Line> instance) {
        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;

        if (includeChatTimestamps) {
            return ChatHelper.pseudoVisibleMessages.size();
        }

        return instance.size();
    }

    @Unique
    private @NotNull FormattedCharSequence kraztweaks$getTimestampText(int tick, String pattern) {
        var time = MiscUtils.getLocalTimeByForwardedTicks(tick);
        var formatted = MiscUtils.parseLocalDateTimeOfPattern(time, pattern);

        if (formatted == null) {
            formatted = MiscUtils.parseLocalDateTimeOfPattern(time, "HH:mm:ss");
            if (formatted == null) throw new RuntimeException("Unreachable");
        }

        return FormattedCharSequence.forward("[%s] ".formatted(formatted), Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
    }
}