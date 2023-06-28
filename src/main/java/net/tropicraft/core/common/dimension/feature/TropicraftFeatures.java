package net.tropicraft.core.common.dimension.feature;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.tree.*;

import java.util.function.Supplier;

public class TropicraftFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);

    public static final RegistryObject<PalmTreeFeature> NORMAL_PALM_TREE = register("normal_palm_tree", () -> new NormalPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<PalmTreeFeature> CURVED_PALM_TREE = register("curved_palm_tree", () -> new CurvedPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<PalmTreeFeature> LARGE_PALM_TREE = register("large_palm_tree", () -> new LargePalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestTreeFeature> UP_TREE = register("up_tree", () -> new UpTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestTreeFeature> SMALL_TUALUNG = register("small_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 16, 9));
    public static final RegistryObject<RainforestTreeFeature> LARGE_TUALUNG = register("large_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 25, 11));
    public static final RegistryObject<RainforestTreeFeature> TALL_TREE = register("tall_tree", () -> new TallRainforestTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<EIHFeature> EIH = register("eih", () -> new EIHFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<UndergrowthFeature> UNDERGROWTH = register("undergrowth", () -> new UndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<SingleUndergrowthFeature> SINGLE_UNDERGROWTH = register("single_undergrowth", () -> new SingleUndergrowthFeature(SimpleTreeFeatureConfig.CODEC));
    public static final RegistryObject<RainforestVinesFeature> VINES = register("rainforest_vines", () -> new RainforestVinesFeature(RainforestVinesConfig.CODEC));
    public static final RegistryObject<UndergroundSeaPickleFeature> UNDERGROUND_SEA_PICKLE = register("underground_sea_pickle", () -> new UndergroundSeaPickleFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<CoffeePlantFeature> COFFEE_BUSH = register("coffee_bush", () -> new CoffeePlantFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<ReedsFeature> REEDS = register("reeds", () -> new ReedsFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<SeagrassFeature> SEAGRASS = register("seagrass", () -> new SeagrassFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<HugePlantFeature> HUGE_PLANT = register("huge_plant", () -> new HugePlantFeature(SimpleBlockConfiguration.CODEC));

    private static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }
}
