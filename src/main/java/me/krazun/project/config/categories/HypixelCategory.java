package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeCategory;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public final class HypixelCategory {

    @LatticeCategory(name = "krazconfig.hypixel.skyblock")
    public HypixelSkyblockCategory hypixelSkyBlockCategory = new HypixelSkyblockCategory();

    @LatticeOption(title = "krazconfig.hypixel.hideNPCSFromTab", description = "!!.description")
    @LatticeWidgetButton
    public boolean hideNPCSFromTab = false;

    public final static class HypixelSkyblockCategory {
        @LatticeOption(title = "krazconfig.hypixel.skyblock.hideEnchantmentDescriptions", description = "!!.description")
        @LatticeWidgetButton
        public boolean hideEnchantmentDescriptions = false;
    }
}