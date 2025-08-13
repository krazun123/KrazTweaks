package me.krazun.project.config.api;

import me.krazun.project.KrazTweaks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public record ConfigInstance<T>(Path configPath, Class<T> configClass, T configInstance) {

    @Contract("_, _ -> new")
    public static <T> @NotNull ConfigInstance<T> load(Path configPath, Class<T> clazz) {
        try {
            if(Files.exists(configPath)) {
                final String data = readFile(configPath);
                return new ConfigInstance<>(configPath, clazz, KrazTweaks.GSON.fromJson(data, clazz));
            } else {
                try {
                    return new ConfigInstance<>(configPath, clazz, clazz.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException("Failed to construct config class '%s'"
                            .formatted(clazz.getSimpleName()), e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config '%s'"
                    .formatted(clazz.getSimpleName()), e);
        }
    }

    public synchronized void save() {
        if(!Files.exists(configPath.getParent())) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create parental directories for %s"
                        .formatted(configClass.getSimpleName()), e);
            }
        }

        try {
            final String data = KrazTweaks.GSON.toJson(configInstance, configClass);

            Files.writeString(configPath, data,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.DSYNC);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save config '%s'"
                    .formatted(configClass.getSimpleName()), e);
        }
    }

    private static @NotNull String readFile(Path configPath) {
        try {
            return Files.readString(configPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from '%s'"
                    .formatted(configPath.toString()), e);
        }
    }
}