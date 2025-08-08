package me.krazun.project.config.api;

import com.moulberry.lattice.Lattice;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.krazun.project.KrazTweaks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ModMenuIntegration implements ModMenuApi {

    @Contract(pure = true)
    @Override
    public @NotNull ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> Lattice.createConfigScreen(
                KrazTweaks.CONFIG_ELEMENTS,
                KrazTweaks.CONFIG::save,
                screen
        );
    }
}