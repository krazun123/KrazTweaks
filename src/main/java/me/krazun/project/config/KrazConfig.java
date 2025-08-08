package me.krazun.project.config;

import com.moulberry.lattice.annotation.LatticeCategory;
import me.krazun.project.config.categories.VisualCategory;

public class KrazConfig {

    @LatticeCategory(name = "krazconfig.visual")
    public VisualCategory visualCategory = new VisualCategory();

}