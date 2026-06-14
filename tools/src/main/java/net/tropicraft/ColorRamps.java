package net.tropicraft;

import net.minecraft.util.CommonColors;

public class ColorRamps {
    public static final ColorRamp GRAYSCALE = ColorRamp.builder()
            .color(0.0f, CommonColors.BLACK)
            .color(1.0f, CommonColors.WHITE)
            .build();
}
