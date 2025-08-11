package me.krazun.project.features.achievement.types;

import java.util.Map;

public record TieredAchievement(
        String internalName,
        String description,
        Map<Integer, TieredAchievementStage> tierStageMap
) {
    public record TieredAchievementStage(int tier, int points, int amount) {}
}
