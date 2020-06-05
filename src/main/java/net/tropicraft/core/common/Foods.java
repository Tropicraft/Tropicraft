package net.tropicraft.core.common;

import net.minecraft.item.Food;

public class Foods {
    public static final Food LEMON = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food LIME = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food GRAPEFRUIT = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food ORANGE = new Food.Builder().hunger(2).saturation(0.2f).fastToEat().build();
    public static final Food PINEAPPLE_CUBES = new Food.Builder().hunger(1).saturation(0.1f).fastToEat().build();
    public static final Food COCONUT_CHUNK = new Food.Builder().hunger(1).saturation(0.1f).fastToEat().build();

    public static final Food COOKED_RAY = new Food.Builder().hunger(5).saturation(0.5f).build();
    public static final Food FRESH_MARLIN = new Food.Builder().hunger(2).saturation(0.3f).build();
    public static final Food SEARED_MARLIN = new Food.Builder().hunger(5).saturation(0.65f).build();
    public static final Food COOKED_FROG_LEG = new Food.Builder().hunger(2).saturation(0.15f).build();
    public static final Food SEA_URCHIN_ROE = new Food.Builder().hunger(3).saturation(0.3f).build();
    public static final Food TOASTED_NORI = new Food.Builder().hunger(2).saturation(0.2f).build();
    public static final Food RAW_FISH = new Food.Builder().hunger(2).saturation(0.2f).build();
    public static final Food RAW_RAY = new Food.Builder().hunger(1).saturation(0.2f).build();
    public static final Food RAW_FROG_LEG = new Food.Builder().hunger(1).saturation(0.2f).build();
    public static final Food COOKED_FISH = new Food.Builder().hunger(4).saturation(0.4f).build();
}
