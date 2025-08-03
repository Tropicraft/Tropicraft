package net.tropicraft.core.common.volcano;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;

public enum VolcanoState {
    DORMANT(604800),
    SMOKING(600),
    RISING(3000),
    ERUPTING(3000),
    RETREATING(600);

    public static final Codec<VolcanoState> CODEC = ExtraCodecs.legacyEnum(VolcanoState::valueOf);

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
