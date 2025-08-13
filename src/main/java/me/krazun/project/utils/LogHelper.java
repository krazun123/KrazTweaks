package me.krazun.project.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LogHelper {

    private static final Logger __LOGGER__ = LogManager.getLogger("KrazTweaks");

    private static void log(@NotNull Level level, @NotNull String message, @Nullable Throwable throwable) {
        __LOGGER__.log(level, message, throwable);
    }

    public static void info(@NotNull String message, @Nullable Throwable throwable) {
        log(Level.INFO, message, throwable);
    }

    public static void warn(@NotNull String message, @Nullable Throwable throwable) {
        log(Level.WARN, message, throwable);
    }

    public static void debug(@NotNull String message, @Nullable Throwable throwable) {
        log(Level.DEBUG, message, throwable);
    }

    public static void trace(@NotNull String message, @Nullable Throwable throwable) {
        log(Level.TRACE, message, throwable);
    }

    public static void error(@NotNull String message, @Nullable Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    public static void fatal(@NotNull String message, @Nullable Throwable throwable) {
        log(Level.FATAL, message, throwable);
    }
}