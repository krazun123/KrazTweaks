package me.krazun.project.features.achievement.types;

public record Achievement(
        String internalName,
        int points,
        String displayName,
        String description,
        double gamePercentUnlocked,
        double globalPercentUnlocked
) {}
