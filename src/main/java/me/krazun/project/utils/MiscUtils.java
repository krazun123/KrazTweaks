package me.krazun.project.utils;

import com.google.gson.JsonObject;
import me.krazun.project.KrazTweaks;
import me.krazun.project.config.categories.VisualCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public final class MiscUtils {

    public static final Timer TIMER = new Timer();
    public static final Random RANDOM = new Random();

    public static JsonObject fetchAchievementsFromHypixel()  {
        final var inputStream = getInputStreamFromURL("https://api.hypixel.net/v2/resources/achievements");
        final var bufferedReader = getBufferedReaderFromStream(inputStream);

        return KrazTweaks.GSON.fromJson(bufferedReader, JsonObject.class);
    }

    @Contract("_ -> new")
    public static @NotNull BufferedReader getBufferedReaderFromStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static InputStream getInputStreamFromURL(@NotNull String url) {
        try {
            return new URI("https://api.hypixel.net/v2/resources/achievements").toURL().openStream();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Failed to open inputStream for url '%s'"
                    .formatted(url), e);
        }
    }

    public static boolean shouldHideStatusEffectsForHUD() {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        return hideStatusEffects == VisualCategory.HideStatusEffects.BOTH || hideStatusEffects == VisualCategory.HideStatusEffects.HUD;
    }

    public static boolean shouldHideStatusEffectsForInventory() {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        return hideStatusEffects == VisualCategory.HideStatusEffects.BOTH || hideStatusEffects == VisualCategory.HideStatusEffects.INVENTORY;
    }

    @Nullable
    public static Hand getHandWithItem(@NotNull ClientPlayerEntity player, @NotNull Item item) {
        final var mainHandItem = player.getMainHandStack();
        final var secondHandItem = player.getOffHandStack();

        if(mainHandItem.getItem() == item) {
            return Hand.MAIN_HAND;
        } else if(secondHandItem.getItem() == item) {
            return Hand.OFF_HAND;
        } else return null;
    }

    public static long getLatencyWithServer(@NotNull MinecraftClient client) {
        final var serverInfo = client.getCurrentServerEntry();
        if (serverInfo == null) return 0;

        return serverInfo.ping;
    }

    public static void schedule(@NotNull TimerTask task, long delay) {
        TIMER.schedule(task, delay);
    }

    public static int randomInteger(int min, int max) {
        return RANDOM.nextInt(min, max);
    }

    public static long randomLong(long min, long max) {
        return RANDOM.nextLong(min, max);
    }
}
