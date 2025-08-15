package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeCategory;

public final class HypixelCategory {

    @LatticeCategory(name = "krazconfig.hypixel.skyblock")
    public HypixelSkyblockCategory hypixelSkyBlockCategory = new HypixelSkyblockCategory();

    public final static class HypixelSkyblockCategory {

    }
}