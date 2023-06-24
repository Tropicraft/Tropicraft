package net.tropicraft.core.common.volcano;

public enum VolcanoState {
    DORMANT(604800),
    SMOKING(600),
    RISING(3000),
    ERUPTING(3000),
    RETREATING(600);

    private final int duration;

    VolcanoState(int duration) {
        this.duration = duration;
    }

    public static int getTimeBefore(VolcanoState state) {
        return switch (state) {
            case DORMANT -> RETREATING.duration;
            case SMOKING -> DORMANT.duration;
            case RISING -> SMOKING.duration;
            case ERUPTING -> RISING.duration;
            case RETREATING -> ERUPTING.duration;
        };
    }
}