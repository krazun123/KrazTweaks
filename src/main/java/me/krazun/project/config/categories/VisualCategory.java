package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public final class VisualCategory {

    @LatticeOption(title = "krazconfig.visual.hideRecipeBook", description = "!!.description")
    @LatticeWidgetButton
    public boolean hideRecipeBook = false;

}
