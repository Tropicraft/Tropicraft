package net.tropicraft.core.common;

import net.minecraft.util.math.MathHelper;

public final class Easings {
    public static final float PI = (float) Math.PI;

    public static float inOutSine(float x) {
        return -(MathHelper.cos(PI * x) - 1.0F) / 2.0F;
    }
}
