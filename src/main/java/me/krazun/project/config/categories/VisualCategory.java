package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeCategory;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;
import com.moulberry.lattice.annotation.widget.LatticeWidgetDropdown;

public final class VisualCategory {

    @LatticeCategory(name = "krazconfig.visual.inventory")
    public InventoryCategory inventoryCategory = new InventoryCategory();

    @LatticeOption(title = "krazconfig.visual.hideStatusEffects", description = "!!.description")
    @LatticeWidgetDropdown
    public HideStatusEffects hideStatusEffects = HideStatusEffects.NONE;

    public enum HideStatusEffects {
        HUD,
        INVENTORY,
        BOTH,
        NONE;
    }

    public static class InventoryCategory {
        @LatticeOption(title = "krazconfig.visual.inventory.hideRecipeBook", description = "!!.description")
        @LatticeWidgetButton
        public boolean hideRecipeBook = false;

        @LatticeOption(title = "krazconfig.visual.inventory.cancelSlotHighlightingWhenTooltipIsEmpty", description = "!!.description")
        @LatticeWidgetButton
        public boolean cancelSlotHighlightingWhenTooltipIsEmpty = false;

        @LatticeOption(title = "krazconfig.visual.inventory.cancelClicksOnSlotWhenTooltipIsEmpty", description = "!!.description")
        @LatticeWidgetButton
        public boolean cancelClicksOnSlotWhenTooltipIsEmpty = false;
    }
}