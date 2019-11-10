package net.tropicraft.core.common.minigames.definitions.survive_the_tide;

public class WaterLevelInfo {
    private final int interval;

    public WaterLevelInfo(int targetLevel, int prevLevel, int phaseLength) {
        int waterLevelDiff = prevLevel - targetLevel;

        this.interval = phaseLength / waterLevelDiff;
    }

    public int getInterval() {
        return this.interval;
    }
}
