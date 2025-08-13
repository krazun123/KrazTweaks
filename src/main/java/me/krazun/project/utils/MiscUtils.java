package me.krazun.project.utils;

import me.krazun.project.KrazTweaks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Nullable
    public static String parseLocalDateTimePattern(@NotNull LocalDateTime localDateTime,
                                                   @NotNull String format)
    {
        try {
            return localDateTime.format(DateTimeFormatter.ofPattern(format));
        } catch (IllegalArgumentException e) {

        }
        return null;
    }
}