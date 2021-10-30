package net.tropicraft.core.common;

import net.minecraft.item.Food;

public class Foods {
    public static final Food LEMON = new Food.Builder().nutrition(2).saturationMod(0.2f).fast().build();
    public static final Food LIME = new Food.Builder().nutrition(2).saturationMod(0.2f).fast().build();
    public static final Food GRAPEFRUIT = new Food.Builder().nutrition(2).saturationMod(0.2f).fast().build();
    public static final Food ORANGE = new Food.Builder().nutrition(2).saturationMod(0.2f).fast().build();
    public static final Food PINEAPPLE_CUBES = new Food.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final Food COCONUT_CHUNK = new Food.Builder().nutrition(1).saturationMod(0.1f).fast().build();

    public static final Food COOKED_RAY = new Food.Builder().nutrition(5).saturationMod(0.5f).build();
    public static final Food FRESH_MARLIN = new Food.Builder().nutrition(2).saturationMod(0.3f).build();
    public static final Food SEARED_MARLIN = new Food.Builder().nutrition(5).saturationMod(0.65f).build();
    public static final Food COOKED_FROG_LEG = new Food.Builder().nutrition(2).saturationMod(0.15f).build();
    public static final Food SEA_URCHIN_ROE = new Food.Builder().nutrition(3).saturationMod(0.3f).build();
    public static final Food TOASTED_NORI = new Food.Builder().nutrition(2).saturationMod(0.2f).build();
    public static final Food RAW_FISH = new Food.Builder().nutrition(2).saturationMod(0.2f).build();
    public static final Food RAW_RAY = new Food.Builder().nutrition(1).saturationMod(0.2f).build();
    public static final Food RAW_FROG_LEG = new Food.Builder().nutrition(1).saturationMod(0.2f).build();
    public static final Food COOKED_FISH = new Food.Builder().nutrition(4).saturationMod(0.4f).build();
}
