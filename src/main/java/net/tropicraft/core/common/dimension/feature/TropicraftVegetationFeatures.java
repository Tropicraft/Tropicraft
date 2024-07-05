package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.block_state_provider.NoiseFromTagBlockStateProvider;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;

import java.util.List;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.*;

public final class TropicraftVegetationFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_VINES = createKey("rainforest_vines");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_GOLDEN_LEATHER_FERN = createKey("small_golden_leather_fern");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_GOLDEN_LEATHER_FERN = createKey("tall_golden_leather_fern");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_GOLDEN_LEATHER_FERN = createKey("huge_golden_leather_fern");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_FRUIT = createKey("trees_fruit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PALM = createKey("trees_palm");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_MANGROVE = createKey("trees_mangroves");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_RAINFOREST = createKey("trees_rainforest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PAPAYA = createKey("trees_papaya");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PLEODENDRON = createKey("trees_pleodendron");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS_TROPICS = createKey("patch_grass_tropics");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINEAPPLE = createKey("pineapple");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PINEAPPLE = createKey("patch_pineapple");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_IRIS = createKey("iris_flowers");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO = createKey("bamboo");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWERS_TROPICS = createKey("flowers_tropics");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWERS_RAINFOREST = createKey("flowers_rainforest");

    public static final ResourceKey<ConfiguredFeature<?, ?>> COFFEE_BUSH = createKey("coffee_bush");

    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERGROWTH = createKey("undergrowth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_UNDERGROWTH = createKey("single_undergrowth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_FLOWERING_BUSH = createKey("red_flowering_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WHITE_FLOWERING_BUSH = createKey("white_flowering_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_FLOWERING_BUSH = createKey("blue_flowering_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PURPLE_FLOWERING_BUSH = createKey("purple_flowering_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_FLOWERING_BUSH = createKey("yellow_flowering_bush");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BUSH_FLOWERING = createKey("flowering_bushes");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS = createKey("seagrass");

    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERGROUND_SEAGRASS = createKey("underground_seagrass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERGROUND_SEA_PICKLES = createKey("underground_sea_pickles");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_REEDS = createKey("mangrove_reeds");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TROPI_SEAGRASS = createKey("tropi_seagrass");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, RAINFOREST_VINES, TropicraftFeatures.VINES, new RainforestVinesConfig());

        register(context, SMALL_GOLDEN_LEATHER_FERN, Feature.RANDOM_PATCH, randomPatch(TropicraftBlocks.GOLDEN_LEATHER_FERN));
        register(context, TALL_GOLDEN_LEATHER_FERN, Feature.RANDOM_PATCH, randomPatch(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN));
        register(context, HUGE_GOLDEN_LEATHER_FERN, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
                TropicraftFeatures.HUGE_PLANT.get(),
                new SimpleBlockConfiguration(BlockStateProvider.simple(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get()))
        ));

        registerRandomPlaced(context, TREES_FRUIT, TropicraftTreePlacements.GRAPEFRUIT_TREE_CHECKED, TropicraftTreePlacements.ORANGE_TREE_CHECKED, TropicraftTreePlacements.LEMON_TREE_CHECKED, TropicraftTreePlacements.LIME_TREE_CHECKED);
        registerRandomPlaced(context, TREES_PALM, TropicraftTreePlacements.PALM_TREE_CHECKED);
        registerRandomPlaced(context, TREES_MANGROVE, TropicraftTreePlacements.RED_MANGROVE_CHECKED, TropicraftTreePlacements.LIGHT_MANGROVES_CHECKED);
        registerRandom(context, TREES_RAINFOREST, List.of(
                Pair.of(TropicraftTreePlacements.RAINFOREST_UP_TREE_CHECKED, 0.2F),
                Pair.of(TropicraftTreePlacements.RAINFOREST_SMALL_TUALUNG_CHECKED, 0.25F),
                Pair.of(TropicraftTreePlacements.RAINFOREST_LARGE_TUALUNG_CHECKED, 0.5F)
        ), TropicraftTreePlacements.RAINFOREST_TALL_TREE_CHECKED);
        registerRandomPlaced(context, TREES_PAPAYA, TropicraftTreePlacements.PAPAYA_CHECKED);
        registerRandomPlaced(context, TREES_PLEODENDRON, TropicraftTreePlacements.PLEODENDRON_CHECKED);

        register(context, PATCH_GRASS_TROPICS, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, PlacementUtils.filtered(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.SHORT_GRASS.defaultBlockState(), 3)
                        .add(Blocks.FERN.defaultBlockState(), 1)
                )),
                BlockPredicate.ONLY_IN_AIR_PREDICATE
        )));
        register(context, PINEAPPLE, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(stateProvider(TropicraftBlocks.PINEAPPLE)));
        register(context, PATCH_PINEAPPLE, Feature.RANDOM_PATCH, randomPatch(TropicraftBlocks.PINEAPPLE));
        register(context, PATCH_IRIS, Feature.RANDOM_PATCH, randomPatch(TropicraftBlocks.IRIS));

        register(context, BAMBOO, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.15F));

        register(context, FLOWERS_TROPICS, Feature.FLOWER, randomPatch(new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.TROPICS_FLOWERS)));
        register(context, FLOWERS_RAINFOREST, Feature.FLOWER, randomPatch(new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.RAINFOREST_FLOWERS)));

        register(context, COFFEE_BUSH, TropicraftFeatures.COFFEE_BUSH);

        register(context, UNDERGROWTH, TropicraftFeatures.UNDERGROWTH);
        register(context, SINGLE_UNDERGROWTH, TropicraftFeatures.SINGLE_UNDERGROWTH, new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.KAPOK_LEAVES.get().defaultBlockState()));
        register(context, RED_FLOWERING_BUSH, TropicraftFeatures.SINGLE_UNDERGROWTH, new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.RED_FLOWERING_LEAVES.get().defaultBlockState()));
        register(context, WHITE_FLOWERING_BUSH, TropicraftFeatures.SINGLE_UNDERGROWTH, new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.WHITE_FLOWERING_LEAVES.get().defaultBlockState()));
        register(context, BLUE_FLOWERING_BUSH, TropicraftFeatures.SINGLE_UNDERGROWTH, new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.BLUE_FLOWERING_LEAVES.get().defaultBlockState()));
        register(context, PURPLE_FLOWERING_BUSH, TropicraftFeatures.SINGLE_UNDERGROWTH, new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.PURPLE_FLOWERING_LEAVES.get().defaultBlockState()));
        register(context, YELLOW_FLOWERING_BUSH, TropicraftFeatures.SINGLE_UNDERGROWTH, new SimpleTreeFeatureConfig(() -> TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState(), () -> TropicraftBlocks.YELLOW_FLOWERING_LEAVES.get().defaultBlockState()));

        registerRandom(context, BUSH_FLOWERING, RED_FLOWERING_BUSH, WHITE_FLOWERING_BUSH, BLUE_FLOWERING_BUSH, PURPLE_FLOWERING_BUSH, YELLOW_FLOWERING_BUSH);

        register(context, SEAGRASS, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.3f));

        register(context, UNDERGROUND_SEAGRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SEAGRASS)));
        register(context, UNDERGROUND_SEA_PICKLES, TropicraftFeatures.UNDERGROUND_SEA_PICKLE);

        register(context, MANGROVE_REEDS, TropicraftFeatures.REEDS);
        register(context, TROPI_SEAGRASS, TropicraftFeatures.SEAGRASS);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }
}
