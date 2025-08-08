package me.krazun.project.config;

import com.moulberry.lattice.Lattice;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.krazun.project.KrazTweaks;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> Lattice.createConfigScreen(
                KrazTweaks.CONFIG_ELEMENTS,
                KrazTweaks.CONFIG::save,
                screen
        );
    }
}