package me.krazun.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.krazun.project.config.ConfigInstance;
import me.krazun.project.config.KrazConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class KrazTweaks implements ClientModInitializer {

    public static Gson GSON;
    public static Path CONFIG_PATH;
    public static ConfigInstance<KrazConfig> CONFIG;

    @Override
    public void onInitializeClient() {
        GSON = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("kraztweaks.json");
        CONFIG = ConfigInstance.load(CONFIG_PATH, KrazConfig.class);
    }
}