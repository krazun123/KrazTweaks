package me.krazun.project.utils;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public class ChatHelper {

    public static final List<ChatHudLine.Visible> pseudoVisibleMessages = Lists.newArrayList();

    public static @Nullable ChatHudLine getLineAt(double mouseX, double mouseY) {
        final var chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();
        final var lineSelected = chatHud.getMessageLineIndex(chatHud.toChatLineX(mouseX), chatHud.toChatLineY(mouseY));
        if (lineSelected == -1) return null;

        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;
        final var list = includeChatTimestamps ? pseudoVisibleMessages : chatHud.visibleMessages;

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

        int indexOfMessage = indexesOfEntryEnds.indexOf(indexOfMessageEntryEnd);
        return chatHud.messages.get(indexOfMessage);
    }
}