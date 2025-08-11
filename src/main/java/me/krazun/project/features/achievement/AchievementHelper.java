package me.krazun.project.features.achievement;

import me.krazun.project.utils.MiscUtils;

public class AchievementHelper {

    public static void load() {
        final var jsonObject = MiscUtils.fetchAchievementsFromHypixel();
        System.out.println(jsonObject);
    }
}