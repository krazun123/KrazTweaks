package me.krazun.project.utils;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChatHelper {

    public static final List<ChatHudLine.Visible> pseudoVisibleMessages = Lists.newArrayList();

    // TODO: Correctify this
    public static @Nullable ChatHudLine getLineAt(double mouseX, double mouseY) {
        final var chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();

        double e = chatHud.toChatLineY(mouseY);
        double d = chatHud.toChatLineX(mouseX);

        int i = chatHud.getMessageLineIndex(d, e);

        final var includeChatTimestamps = KrazTweaks.CONFIG.configInstance().chatCategory.includeChatTimestamps;
        final List<ChatHudLine.Visible> list = includeChatTimestamps ? pseudoVisibleMessages : chatHud.visibleMessages;

        if (i >= 0 && i < list.size()) {
            ChatHudLine.Visible visible = list.get(i);

            final var optional = MinecraftClient.getInstance().inGameHud.getChatHud().messages
                    .stream()
                    .filter(line -> line.creationTick() == visible.addedTime())
                    .filter(line -> {
                        if(line.indicator() != null) {
                            return line.indicator().equals(visible.indicator());
                        } else return true;
                    })
                    .findAny();

            if(optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }
}