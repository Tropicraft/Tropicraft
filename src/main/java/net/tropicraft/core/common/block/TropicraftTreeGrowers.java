package net.tropicraft.core.common.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.feature.TropicraftTreeFeatures;

import java.util.Optional;

public final class TropicraftTreeGrowers {
    public static final TreeGrower GRAPEFRUIT = create("grapefruit", TropicraftTreeFeatures.GRAPEFRUIT_TREE);
    public static final TreeGrower LEMON = create("lemon", TropicraftTreeFeatures.LEMON_TREE);
    public static final TreeGrower LIME = create("lime", TropicraftTreeFeatures.LIME_TREE);
    public static final TreeGrower ORANGE = create("orange", TropicraftTreeFeatures.ORANGE_TREE);
    public static final TreeGrower PAPAYA = create("papaya", TropicraftTreeFeatures.PAPAYA);
    public static final TreeGrower PLANTAIN = create("plantain", TropicraftTreeFeatures.PLANTAIN);
    public static final TreeGrower JOCOTE = create("jocote", TropicraftTreeFeatures.JOCOTE);

    public static final TreeGrower RAINFOREST = create("rainforest", TropicraftTreeFeatures.RAINFOREST_TREE);
    public static final TreeGrower PALM = create("palm", TropicraftTreeFeatures.PALM_TREE);

    public static final TreeGrower RED_MANGROVE = create("red_mangrove", TropicraftTreeFeatures.RED_MANGROVE);
    public static final TreeGrower TALL_MANGROVE = create("tall_mangrove", TropicraftTreeFeatures.TALL_MANGROVE);
    public static final TreeGrower TEA_MANGROVE = create("tea_mangrove", TropicraftTreeFeatures.TEA_MANGROVE);
    public static final TreeGrower BLACK_MANGROVE = create("black_mangrove", TropicraftTreeFeatures.BLACK_MANGROVE);

    private static TreeGrower create(String id, ResourceKey<ConfiguredFeature<?, ?>> featureKey) {
        return new TreeGrower(Tropicraft.ID + ":" + id, Optional.empty(), Optional.of(featureKey), Optional.empty());
    }
}
