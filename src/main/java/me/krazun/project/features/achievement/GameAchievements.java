package me.krazun.project.features.achievement;

import me.krazun.project.features.achievement.types.Achievement;
import me.krazun.project.features.achievement.types.TieredAchievement;

import java.util.Map;

public record GameAchievements(
        String game,
        Map<String, Achievement> oneTimeAchievementsMap,
        Map<String, TieredAchievement> tieredAchievementsMap,
        int totalPoints,
        int totalLegacyPoints
) {}
