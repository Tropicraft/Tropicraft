package net.tropicraft.core.common;

import net.minecraft.util.Mth;

public final class Easings {
    public static final float PI = (float) Math.PI;

    public static float inOutSine(float x) {
        return -(Mth.cos(PI * x) - 1.0F) / 2.0F;
    }
}
