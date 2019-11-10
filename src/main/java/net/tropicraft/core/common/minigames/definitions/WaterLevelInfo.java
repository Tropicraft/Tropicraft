package net.tropicraft.core.common.minigames.definitions;

public class WaterLevelInfo {
    private final int targetLevel, interval;

    public WaterLevelInfo(int targetLevel, int prevLevel, int phaseLength) {
        this.targetLevel = targetLevel;
        int waterLevelDiff = prevLevel - this.targetLevel;

        this.interval = phaseLength / waterLevelDiff;
    }

    public int getInterval() {
        return this.interval;
    }

    public int getTargetLevel() {
        return this.targetLevel;
    }
}
