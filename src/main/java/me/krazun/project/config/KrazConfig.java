package me.krazun.project.config;

import com.moulberry.lattice.annotation.LatticeCategory;
import me.krazun.project.config.categories.HypixelCategory;
import me.krazun.project.config.categories.MiscCategory;
import me.krazun.project.config.categories.VisualCategory;

public class KrazConfig {

    @LatticeCategory(name = "krazconfig.visual")
    public VisualCategory visualCategory = new VisualCategory();

    @LatticeCategory(name = "krazconfig.hypixel")
    public HypixelCategory hypixelCategory = new HypixelCategory();

    @LatticeCategory(name = "krazconfig.misc")
    public MiscCategory miscCategory = new MiscCategory();

}