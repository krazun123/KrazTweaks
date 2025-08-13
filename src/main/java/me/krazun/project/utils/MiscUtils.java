package me.krazun.project.utils;

import me.krazun.project.KrazTweaks;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public final class MiscUtils {

    public static @NotNull String readFileAsString(Path filePath) {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from '%s'"
                    .formatted(filePath.toString()), e);
        }
    }

    public static @NotNull LocalDateTime getLocalTimeByForwardedTicks(int ticks) {
        return KrazTweaks.LOCAL_TIME.plusSeconds(Math.max(ticks / 20, 0));
    }
}