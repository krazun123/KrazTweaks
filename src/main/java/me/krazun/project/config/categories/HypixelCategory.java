package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeCategory;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public final class HypixelCategory {

    @LatticeCategory(name = "krazconfig.hypixel.achievementHelper")
    public AchievementHelper achievementHelper = new AchievementHelper();

    public static class AchievementHelper {
        @LatticeOption(title = "krazconfig.hypixel.achievementHelper.customGameAchivementsMenu", description = "!!.description")
        @LatticeWidgetButton
        public boolean customGameAchivementsMenu = false;
    }
}
