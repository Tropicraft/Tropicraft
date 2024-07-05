package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.tree.CurvedPalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.LargePalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.NormalPalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.PalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.RainforestTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.RainforestVinesFeature;
import net.tropicraft.core.common.dimension.feature.tree.TallRainforestTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.TualungFeature;
import net.tropicraft.core.common.dimension.feature.tree.UpTreeFeature;

import java.util.function.Supplier;

public class TropicraftFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Constants.MODID);

    public static final DeferredHolder<Feature<?>, PalmTreeFeature> NORMAL_PALM_TREE = register("normal_palm_tree", () -> new NormalPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, PalmTreeFeature> CURVED_PALM_TREE = register("curved_palm_tree", () -> new CurvedPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, PalmTreeFeature> LARGE_PALM_TREE = register("large_palm_tree", () -> new LargePalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, RainforestTreeFeature> UP_TREE = register("up_tree", () -> new UpTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, RainforestTreeFeature> SMALL_TUALUNG = register("small_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 16, 9));
    public static final DeferredHolder<Feature<?>, RainforestTreeFeature> LARGE_TUALUNG = register("large_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 25, 11));
    public static final DeferredHolder<Feature<?>, RainforestTreeFeature> TALL_TREE = register("tall_tree", () -> new TallRainforestTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, EIHFeature> EIH = register("eih", () -> new EIHFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, UndergrowthFeature> UNDERGROWTH = register("undergrowth", () -> new UndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, SingleUndergrowthFeature> SINGLE_UNDERGROWTH = register("single_undergrowth", () -> new SingleUndergrowthFeature(SimpleTreeFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, RainforestVinesFeature> VINES = register("rainforest_vines", () -> new RainforestVinesFeature(RainforestVinesConfig.CODEC));
    public static final DeferredHolder<Feature<?>, UndergroundSeaPickleFeature> UNDERGROUND_SEA_PICKLE = register("underground_sea_pickle", () -> new UndergroundSeaPickleFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, CoffeePlantFeature> COFFEE_BUSH = register("coffee_bush", () -> new CoffeePlantFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, ReedsFeature> REEDS = register("reeds", () -> new ReedsFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, SeagrassFeature> SEAGRASS = register("seagrass", () -> new SeagrassFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, HugePlantFeature> HUGE_PLANT = register("huge_plant", () -> new HugePlantFeature(SimpleBlockConfiguration.CODEC));

    private static <T extends Feature<?>> DeferredHolder<Feature<?>, T> register(String name, Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }
}
