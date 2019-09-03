package net.tropicraft.core.common;

import net.minecraft.item.Food;

public class Foods {
    public static final Food LEMON = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food LIME = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food GRAPEFRUIT = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food ORANGE = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food PINEAPPLE_CUBES = new Food.Builder().hunger(1).saturation(0.1f).fastToEat().build();
    public static final Food COCONUT_CHUNK = new Food.Builder().hunger(1).saturation(0.1f).fastToEat().build();
}
