package me.krazun.project.config;

import com.google.gson.annotations.Expose;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public class KrazConfig {

    @Expose
    @LatticeOption(title = "Root", description = "Whatever", translate = false)
    @LatticeWidgetButton
    public boolean testVariable = false;

}