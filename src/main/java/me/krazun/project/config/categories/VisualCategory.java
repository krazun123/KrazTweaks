package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeCategory;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public final class VisualCategory {

    @LatticeCategory(name = "krazconfig.visual.inventory")
    public InventoryCategory inventoryCategory = new InventoryCategory();

    public static class InventoryCategory {
        @LatticeOption(title = "krazconfig.visual.inventory.hideRecipeBook", description = "!!.description")
        @LatticeWidgetButton
        public boolean hideRecipeBook = false;

        @LatticeOption(title = "krazconfig.visual.inventory.hideSlotHighlightingWhenTooltipIsEmpty", description = "!!.description")
        @LatticeWidgetButton
        public boolean hideSlotHighlightingWhenTooltipIsEmpty = false;
    }
}