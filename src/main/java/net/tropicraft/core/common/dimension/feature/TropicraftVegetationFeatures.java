package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.block_state_provider.NoiseFromTagBlockStateProvider;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;

import java.util.List;

public final class TropicraftVegetationFeatures {
    public static final TropicraftFeatures.Register REGISTER = TropicraftFeatures.Register.create();

    public static final RegistryObject<ConfiguredFeature<?, ?>> RAINFOREST_VINES = REGISTER.feature("rainforest_vines", TropicraftFeatures.VINES, RainforestVinesConfig::new);

    public static final RegistryObject<ConfiguredFeature<?, ?>> SMALL_GOLDEN_LEATHER_FERN = REGISTER.feature("small_golden_leather_fern", Feature.RANDOM_PATCH, () -> REGISTER.randomPatch(TropicraftBlocks.GOLDEN_LEATHER_FERN));
    public static final RegistryObject<ConfiguredFeature<?, ?>> TALL_GOLDEN_LEATHER_FERN = REGISTER.feature("tall_golden_leather_fern", Feature.RANDOM_PATCH, () -> REGISTER.randomPatch(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN));
    public static final RegistryObject<ConfiguredFeature<?, ?>> HUGE_GOLDEN_LEATHER_FERN = REGISTER.feature("huge_golden_leather_fern", Feature.RANDOM_PATCH, () -> {
        final SimpleBlockConfiguration config = new SimpleBlockConfiguration(BlockStateProvider.simple(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get()));
        return FeatureUtils.simplePatchConfiguration(TropicraftFeatures.HUGE_PLANT.get(), config);
    });

    public static final RegistryObject<ConfiguredFeature<?, ?>> TREES_FRUIT = REGISTER.randomPlacedFeature("trees_fruit", TropicraftTreePlacements.GRAPEFRUIT_TREE_CHECKED, TropicraftTreePlacements.ORANGE_TREE_CHECKED, TropicraftTreePlacements.LEMON_TREE_CHECKED, TropicraftTreePlacements.LIME_TREE_CHECKED);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TREES_PALM = REGISTER.randomPlacedFeature("trees_palm", TropicraftTreePlacements.PALM_TREE_CHECKED);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TREES_MANGROVE = REGISTER.randomPlacedFeature("trees_mangroves", TropicraftTreePlacements.RED_MANGROVE_CHECKED, TropicraftTreePlacements.LIGHT_MANGROVES_CHECKED);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TREES_RAINFOREST = REGISTER.randomFeature("trees_rainforest", List.of(
            Pair.of(TropicraftTreePlacements.RAINFOREST_UP_TREE_CHECKED, 0.2F),
            Pair.of(TropicraftTreePlacements.RAINFOREST_SMALL_TUALUNG_CHECKED, 0.25F),
            Pair.of(TropicraftTreePlacements.RAINFOREST_LARGE_TUALUNG_CHECKED, 0.5F)
    ), TropicraftTreePlacements.RAINFOREST_TALL_TREE_CHECKED);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TREES_PAPAYA = REGISTER.randomPlacedFeature("trees_papaya", TropicraftTreePlacements.PAPAYA_CHECKED);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TREES_PLEODENDRON = REGISTER.randomPlacedFeature("trees_pleodendron", TropicraftTreePlacements.PLEODENDRON_CHECKED);

    public static final RegistryObject<ConfiguredFeature<?, ?>> PATCH_GRASS_TROPICS = REGISTER.copyFeature("patch_grass_tropics", VegetationFeatures.PATCH_GRASS_JUNGLE);
    public static final RegistryObject<ConfiguredFeature<?, ?>> PATCH_PINEAPPLE = REGISTER.feature("patch_pineapple", Feature.RANDOM_PATCH, () -> REGISTER.randomPatch(TropicraftBlocks.PINEAPPLE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> PATCH_IRIS = REGISTER.feature("iris_flowers", Feature.RANDOM_PATCH, () -> REGISTER.randomPatch(TropicraftBlocks.IRIS));

    public static final RegistryObject<ConfiguredFeature<?, ?>> BAMBOO = REGISTER.feature("bamboo", Feature.BAMBOO, () -> new ProbabilityFeatureConfiguration(0.15F));

    public static final RegistryObject<ConfiguredFeature<?, ?>> FLOWERS_TROPICS = REGISTER.feature("flowers_tropics", Feature.FLOWER, () -> REGISTER.randomPatch(new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.TROPICS_FLOWERS)));
    public static final RegistryObject<ConfiguredFeature<?, ?>> FLOWERS_RAINFOREST = REGISTER.feature("flowers_rainforest", Feature.FLOWER, () -> REGISTER.randomPatch(new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.RAINFOREST_FLOWERS)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> COFFEE_BUSH = REGISTER.feature("coffee_bush", TropicraftFeatures.COFFEE_BUSH);

    public static final RegistryObject<ConfiguredFeature<?, ?>> UNDERGROWTH = REGISTER.feature("undergrowth", TropicraftFeatures.UNDERGROWTH);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SINGLE_UNDERGROWTH = REGISTER.feature("single_undergrowth", TropicraftFeatures.SINGLE_UNDERGROWTH, () -> new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.KAPOK_LEAVES.get().defaultBlockState()));
    public static final RegistryObject<ConfiguredFeature<?, ?>> RED_FLOWERING_BUSH = REGISTER.feature("red_flowering_bush", TropicraftFeatures.SINGLE_UNDERGROWTH, () -> new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.RED_FLOWERING_LEAVES.get().defaultBlockState()));
    public static final RegistryObject<ConfiguredFeature<?, ?>> WHITE_FLOWERING_BUSH = REGISTER.feature("white_flowering_bush", TropicraftFeatures.SINGLE_UNDERGROWTH, () -> new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.WHITE_FLOWERING_LEAVES.get().defaultBlockState()));
    public static final RegistryObject<ConfiguredFeature<?, ?>> BLUE_FLOWERING_BUSH = REGISTER.feature("blue_flowering_bush", TropicraftFeatures.SINGLE_UNDERGROWTH, () -> new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.BLUE_FLOWERING_LEAVES.get().defaultBlockState()));
    public static final RegistryObject<ConfiguredFeature<?, ?>> PURPLE_FLOWERING_BUSH = REGISTER.feature("purple_flowering_bush", TropicraftFeatures.SINGLE_UNDERGROWTH, () -> new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.PURPLE_FLOWERING_LEAVES.get().defaultBlockState()));
    public static final RegistryObject<ConfiguredFeature<?, ?>> YELLOW_FLOWERING_BUSH = REGISTER.feature("yellow_flowering_bush", TropicraftFeatures.SINGLE_UNDERGROWTH, () -> new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.YELLOW_FLOWERING_LEAVES.get().defaultBlockState()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> BUSH_FLOWERING = REGISTER.randomFeature("flowering_bushes", RED_FLOWERING_BUSH, WHITE_FLOWERING_BUSH, BLUE_FLOWERING_BUSH, PURPLE_FLOWERING_BUSH, YELLOW_FLOWERING_BUSH);

    public static final RegistryObject<ConfiguredFeature<?, ?>> SEAGRASS = REGISTER.feature("seagrass", Feature.SEAGRASS, () -> new ProbabilityFeatureConfiguration(0.3f));

    public static final RegistryObject<ConfiguredFeature<?, ?>> UNDERGROUND_SEAGRASS = REGISTER.feature("underground_seagrass", Feature.SIMPLE_BLOCK, () -> new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SEAGRASS)));
    public static final RegistryObject<ConfiguredFeature<?, ?>> UNDERGROUND_SEA_PICKLES = REGISTER.feature("underground_sea_pickles", TropicraftFeatures.UNDERGROUND_SEA_PICKLE);

    public static final RegistryObject<ConfiguredFeature<?, ?>> MANGROVE_REEDS = REGISTER.feature("mangrove_reeds", TropicraftFeatures.REEDS);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TROPI_SEAGRASS = REGISTER.feature("tropi_seagrass", TropicraftFeatures.SEAGRASS);
}
