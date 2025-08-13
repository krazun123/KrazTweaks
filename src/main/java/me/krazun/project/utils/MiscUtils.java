package me.krazun.project.utils;

import me.krazun.project.KrazTweaks;
import me.krazun.project.config.categories.VisualCategory;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public final class MiscUtils {

    public static boolean shouldHideStatusEffectsForHUD() {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        return hideStatusEffects == VisualCategory.HideStatusEffects.BOTH || hideStatusEffects == VisualCategory.HideStatusEffects.HUD;
    }

    public static boolean shouldHideStatusEffectsForInventory() {
        final var hideStatusEffects = KrazTweaks.CONFIG.configInstance().visualCategory.hideStatusEffects;
        return hideStatusEffects == VisualCategory.HideStatusEffects.BOTH || hideStatusEffects == VisualCategory.HideStatusEffects.INVENTORY;
    }

    public static @NotNull LocalDateTime getLocalTimeByForwardedTicks(int ticks) {
        return KrazTweaks.LOCAL_TIME.plusSeconds(Math.max(ticks / 20, 0));
    }
}