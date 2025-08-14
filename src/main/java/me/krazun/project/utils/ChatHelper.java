package me.krazun.project.utils;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public class ChatHelper {

    public static final List<GuiMessage.Line> pseudoVisibleMessages = Lists.newArrayList();

    public static @Nullable GuiMessage getLineAt(double mouseX, double mouseY) {
        final var chatHud = Minecraft.getInstance().gui.getChat();
        final var lineSelected = chatHud.getMessageLineIndexAt(chatHud.screenToChatX(mouseX), chatHud.screenToChatY(mouseY));
        if (lineSelected == -1) return null;

        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;
        final var list = includeChatTimestamps ? pseudoVisibleMessages : chatHud.trimmedMessages;

        final var indexesOfEntryEnds = IntStream.range(0, list.size())
                .filter(index -> list.get(index).endOfEntry())
                .boxed()
                .toList();

        final var indexOfMessageEntryEnd = indexesOfEntryEnds
                .stream()
                .filter(index -> index <= lineSelected)
                .reduce((a, b) -> b)
                .orElse(-1);
        if (indexOfMessageEntryEnd == -1) return null;

        final var indexOfMessage = indexesOfEntryEnds.indexOf(indexOfMessageEntryEnd);
        return chatHud.allMessages.get(indexOfMessage);
    }
}