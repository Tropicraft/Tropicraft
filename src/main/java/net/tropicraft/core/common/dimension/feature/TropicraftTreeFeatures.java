package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.FruitingBranchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.tree.BranchTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.PleodendronFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PleodendronTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PneumatophoresTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.ReplaceInSoilDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.*;

public final class TropicraftTreeFeatures {
    private static final BeehiveDecorator BEEHIVE_002 = new BeehiveDecorator(0.02F);
    private static final BeehiveDecorator BEEHIVE_005 = new BeehiveDecorator(0.05F);

    public static final ResourceKey<ConfiguredFeature<?, ?>> GRAPEFRUIT_TREE = createKey("grapefruit_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_TREE = createKey("orange_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEMON_TREE = createKey("lemon_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIME_TREE = createKey("lime_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> NORMAL_PALM_TREE = createKey("normal_palm_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CURVED_PALM_TREE = createKey("curved_palm_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_PALM_TREE = createKey("large_palm_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE = createKey("palm_trees");

    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_UP_TREE = createKey("rainforest_up_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_SMALL_TUALUNG = createKey("rainforest_small_tualung");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_LARGE_TUALUNG = createKey("rainforest_large_tualung");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_TALL_TREE = createKey("rainforest_tall_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_TREE = createKey("rainforest_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PLEODENDRON = createKey("pleodendron");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PAPAYA = createKey("papaya");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLANTAIN = createKey("plantain");
    public static final ResourceKey<ConfiguredFeature<?, ?>> JOCOTE = createKey("jocote");

    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_MANGROVE_SHORT = createKey("red_mangrove_short");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_MANGROVE_SMALL = createKey("red_mangrove_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_MANGROVE = createKey("red_mangrove");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_MANGROVE = createKey("tall_mangrove");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TEA_MANGROVE = createKey("tea_mangrove");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLACK_MANGROVE = createKey("black_mangrove");

    private static TreeConfiguration fruitTree(Supplier<? extends Block> fruitLeaves) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG.defaultBlockState()),
                new CitrusTrunkPlacer(6, 3, 0),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1)
                        .add(fruitLeaves.get().defaultBlockState(), 1)
                        .build()
                ),
                new CitrusFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).build();
    }

    private static ReplaceInSoilDecorator addPianguasInMud(int count, int spread) {
        return new ReplaceInSoilDecorator(count,
                spread,
                RuleBasedBlockStateProvider.simple(TropicraftBlocks.MUD_WITH_PIANGUAS.get()),
                BlockPredicate.matchesBlocks(TropicraftBlocks.MUD.get())
        );
    }

    private static final Supplier<FoliagePlacer> MANGROVE_FOLIAGE = () -> new MangroveFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0));
    private static final Supplier<MangroveTrunkPlacer> RED_MANGROVE_TRUNK = () -> new MangroveTrunkPlacer(3, 3, 0, stateProvider(TropicraftBlocks.RED_MANGROVE_ROOTS), true, false);

    private static final TwoLayersFeatureSize MANGROVE_MINIMUM_SIZE = new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, GRAPEFRUIT_TREE, Feature.TREE, fruitTree(TropicraftBlocks.GRAPEFRUIT_LEAVES));
        register(context, ORANGE_TREE, Feature.TREE, fruitTree(TropicraftBlocks.ORANGE_LEAVES));
        register(context, LEMON_TREE, Feature.TREE, fruitTree(TropicraftBlocks.LEMON_LEAVES));
        register(context, LIME_TREE, Feature.TREE, fruitTree(TropicraftBlocks.LIME_LEAVES));

        register(context, NORMAL_PALM_TREE, TropicraftFeatures.NORMAL_PALM_TREE);
        register(context, CURVED_PALM_TREE, TropicraftFeatures.CURVED_PALM_TREE);
        register(context, LARGE_PALM_TREE, TropicraftFeatures.LARGE_PALM_TREE);
        registerRandom(context, PALM_TREE, NORMAL_PALM_TREE, CURVED_PALM_TREE, LARGE_PALM_TREE);

        register(context, RAINFOREST_UP_TREE, TropicraftFeatures.UP_TREE);
        register(context, RAINFOREST_SMALL_TUALUNG, TropicraftFeatures.SMALL_TUALUNG);
        register(context, RAINFOREST_LARGE_TUALUNG, TropicraftFeatures.LARGE_TUALUNG);
        register(context, RAINFOREST_TALL_TREE, TropicraftFeatures.TALL_TREE);
        registerRandom(context, RAINFOREST_TREE, RAINFOREST_UP_TREE, RAINFOREST_SMALL_TUALUNG, LARGE_PALM_TREE, RAINFOREST_TALL_TREE);

        register(context, PLEODENDRON, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.JUNGLE_LOG.defaultBlockState()),
                new PleodendronTrunkPlacer(10, 8, 0),
                BlockStateProvider.simple(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                new PleodendronFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        ).build());

        register(context, PAPAYA, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LOG.get().defaultBlockState()),
                new StraightTrunkPlacer(5, 2, 3),
                BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LEAVES.get().defaultBlockState()),
                new PapayaFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        )
                .decorators(List.of(BEEHIVE_005, new PapayaTreeDecorator()))
                .build());

        register(context, PLANTAIN, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(TropicraftBlocks.PLANTAIN_STEM.get().defaultBlockState()),
                new StraightTrunkPlacer(3, 1, 1),
                BlockStateProvider.simple(TropicraftBlocks.PLANTAIN_LEAVES.get().defaultBlockState()),
                new SmallMangroveFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        )
                .decorators(List.of(new AttachedToLeavesDecorator(
                        0.5f,
                        1,
                        0,
                        new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                .add(TropicraftBlocks.YELLOW_PLANTAIN_BUNCH.getDefaultState(), 1)
                                .add(TropicraftBlocks.GREEN_PLANTAIN_BUNCH.getDefaultState(), 1)
                                .build()),
                        2,
                        List.of(Direction.DOWN)
                )))
                .build());

        register(context, JOCOTE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(TropicraftBlocks.JOCOTE_LOG.get().defaultBlockState()),
                new StraightTrunkPlacer(3, 1, 1),
                BlockStateProvider.simple(TropicraftBlocks.JOCOTE_LEAVES.get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        )
                .decorators(List.of(new BranchTreeDecorator(
                        0.1f,
                        new RandomizedIntStateProvider(
                                BlockStateProvider.simple(TropicraftBlocks.JOCOTE_BRANCH.getDefaultState()),
                                FruitingBranchBlock.AGE,
                                UniformInt.of(0, FruitingBranchBlock.MAX_AGE)
                        ),
                        2
                )))
                .build());

        register(context, RED_MANGROVE_SHORT, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                stateProvider(TropicraftBlocks.RED_MANGROVE_LOG),
                RED_MANGROVE_TRUNK.get(),
                stateProvider(TropicraftBlocks.RED_MANGROVE_LEAVES),
                MANGROVE_FOLIAGE.get(),
                MANGROVE_MINIMUM_SIZE
        )
                .decorators(List.of(BEEHIVE_002, addPianguasInMud(8, 4)))
                .build());

        register(context, RED_MANGROVE_SMALL, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                stateProvider(TropicraftBlocks.RED_MANGROVE_LOG),
                new SmallMangroveTrunkPlacer(2, 1, 0, TropicraftBlocks.RED_MANGROVE_ROOTS.get()),
                stateProvider(TropicraftBlocks.RED_MANGROVE_LEAVES),
                new SmallMangroveFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),
                MANGROVE_MINIMUM_SIZE
        )
                .decorators(List.of(BEEHIVE_002, addPianguasInMud(2, 2)))
                .build());
        registerRandom(context, RED_MANGROVE, RED_MANGROVE_SHORT, RED_MANGROVE_SMALL);

        register(context, TALL_MANGROVE, Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        stateProvider(TropicraftBlocks.LIGHT_MANGROVE_LOG),
                        new MangroveTrunkPlacer(7, 4, 2, stateProvider(TropicraftBlocks.LIGHT_MANGROVE_ROOTS), false, false),
                        stateProvider(TropicraftBlocks.TALL_MANGROVE_LEAVES),
                        MANGROVE_FOLIAGE.get(),
                        MANGROVE_MINIMUM_SIZE
                )
                        .decorators(List.of(BEEHIVE_002, addPianguasInMud(8, 4)))
                        .build());

        register(context, TEA_MANGROVE, Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        stateProvider(TropicraftBlocks.LIGHT_MANGROVE_LOG),
                        new MangroveTrunkPlacer(5, 3, 0, stateProvider(TropicraftBlocks.LIGHT_MANGROVE_ROOTS), false, true),
                        stateProvider(TropicraftBlocks.TEA_MANGROVE_LEAVES),
                        MANGROVE_FOLIAGE.get(),
                        MANGROVE_MINIMUM_SIZE
                )
                        .decorators(List.of(BEEHIVE_002, addPianguasInMud(8, 4), new PneumatophoresTreeDecorator(stateProvider(TropicraftBlocks.LIGHT_MANGROVE_ROOTS), 2, 6, 4)))
                        .build());

        register(context, BLACK_MANGROVE, Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        stateProvider(TropicraftBlocks.BLACK_MANGROVE_LOG),
                        new MangroveTrunkPlacer(4, 3, 0, stateProvider(TropicraftBlocks.BLACK_MANGROVE_ROOTS), true, false),
                        stateProvider(TropicraftBlocks.BLACK_MANGROVE_LEAVES),
                        MANGROVE_FOLIAGE.get(),
                        MANGROVE_MINIMUM_SIZE
                )
                        .decorators(List.of(BEEHIVE_002, addPianguasInMud(8, 4), new PneumatophoresTreeDecorator(stateProvider(TropicraftBlocks.BLACK_MANGROVE_ROOTS), 8, 16, 6)))
                        .build());
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }
}
