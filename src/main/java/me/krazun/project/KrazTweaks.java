package me.krazun.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moulberry.lattice.element.LatticeElements;
import me.krazun.project.config.KrazConfig;
import me.krazun.project.config.api.ConfigInstance;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class KrazTweaks implements ClientModInitializer {

    public static Gson GSON;

    public static Path FOLDER_PATH;

    public static Path CONFIG_PATH;
    public static ConfigInstance<KrazConfig> CONFIG;
    public static LatticeElements CONFIG_ELEMENTS;

    public static LocalDateTime LOCAL_TIME;

    @Override
    public void onInitializeClient() {
        GSON = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();

        FOLDER_PATH = FabricLoader.getInstance().getConfigDir().resolve("kraztweaks");

        CONFIG_PATH = FOLDER_PATH.resolve("kraztweaks.json");
        CONFIG = ConfigInstance.load(CONFIG_PATH, KrazConfig.class);
        CONFIG_ELEMENTS = LatticeElements.fromAnnotations(Component.literal("KrazTweaks"), CONFIG.configInstance());

        LOCAL_TIME = LocalDateTime.now();
    }
}