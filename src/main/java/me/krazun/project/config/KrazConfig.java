package me.krazun.project.config;

import com.moulberry.lattice.annotation.LatticeCategory;
import me.krazun.project.config.categories.ChatCategory;
import me.krazun.project.config.categories.MiscCategory;
import me.krazun.project.config.categories.VisualCategory;

public class KrazConfig {

    @LatticeCategory(name = "krazconfig.visual")
    public VisualCategory visualCategory = new VisualCategory();

    @LatticeCategory(name = "krazconfig.chat")
    public ChatCategory chatCategory = new ChatCategory();

    @LatticeCategory(name = "krazconfig.misc")
    public MiscCategory miscCategory = new MiscCategory();

}