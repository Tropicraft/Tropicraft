package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftTrees;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.block_placer.HugePlantBlockPlacer;
import net.tropicraft.core.common.dimension.feature.block_state_provider.NoiseFromTagBlockStateProvider;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PapayaTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.PleodendronFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PleodendronTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PianguasTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PneumatophoresTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveTrunkPlacer;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static net.tropicraft.core.common.block.TropicraftTrees.BEEHIVE_002;

public final class TropicraftConfiguredFeatures {
    public final ConfiguredFeature<?, ?> grapefruitTree;
    public final ConfiguredFeature<?, ?> orangeTree;
    public final ConfiguredFeature<?, ?> lemonTree;
    public final ConfiguredFeature<?, ?> limeTree;
    public final ConfiguredFeature<?, ?> normalPalmTree;
    public final ConfiguredFeature<?, ?> curvedPalmTree;
    public final ConfiguredFeature<?, ?> largePalmTree;
    public final ConfiguredFeature<?, ?> rainforestUpTree;
    public final ConfiguredFeature<?, ?> rainforestSmallTualung;
    public final ConfiguredFeature<?, ?> rainforestLargeTualung;
    public final ConfiguredFeature<?, ?> rainforestTallTree;
    public final ConfiguredFeature<?, ?> rainforestVines;
    public final ConfiguredFeature<?, ?> eih;
    public final ConfiguredFeature<?, ?> tropicsGrass;
    public final ConfiguredFeature<?, ?> bamboo;

    public final ConfiguredFeature<?, ?> redMangroveShort;
    public final ConfiguredFeature<?, ?> redMangroveSmall;
    public final ConfiguredFeature<?, ?> redMangrove;

    public final ConfiguredFeature<?, ?> blackMangrove;
    public final ConfiguredFeature<?, ?> tallMangrove;
    public final ConfiguredFeature<?, ?> teaMangrove;
    public final ConfiguredFeature<?, ?> lightMangroves;

    public final ConfiguredFeature<?, ?> mangroves;
    public final ConfiguredFeature<?, ?> papaya;

    public final ConfiguredFeature<?, ?> mangroveVegetation;
    public final ConfiguredFeature<?, ?> sparseMangroveVegetation;

    public final ConfiguredFeature<?, ?> mudDisk;

    public final ConfiguredFeature<?, ?> smallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> tallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> hugeGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> overgrownSmallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> overgrownTallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> overgrownHugeGoldenLeatherFern;

    public final ConfiguredFeature<?, ?> pleodendron;

    public final ConfiguredFeature<?, ?> pineapplePatch;
    public final ConfiguredFeature<?, ?> tropicsFlowers;
    public final ConfiguredFeature<?, ?> rainforestFlowers;
    public final ConfiguredFeature<?, ?> irisFlowers;

    public final ConfiguredFeature<?, ?> coffeeBush;
    public final ConfiguredFeature<?, ?> undergrowth;
    public final ConfiguredFeature<?, ?> singleUndergrowth;

    public final ConfiguredFeature<?, ?> seagrass;
    public final ConfiguredFeature<?, ?> undergroundSeagrassOnStone;
    public final ConfiguredFeature<?, ?> undergroundSeagrassOnDirt;
    public final ConfiguredFeature<?, ?> undergroundSeaPickles;

    public final ConfiguredFeature<?, ?> mangroveReeds;

    public final ConfiguredFeature<?, ?> azurite;
    public final ConfiguredFeature<?, ?> eudialyte;
    public final ConfiguredFeature<?, ?> zircon;
    public final ConfiguredFeature<?, ?> manganese;
    public final ConfiguredFeature<?, ?> shaka;

    public TropicraftConfiguredFeatures(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen) {
        Register features = new Register(worldgen);

        this.grapefruitTree = features.fruitTree("grapefruit_tree", TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftBlocks.GRAPEFRUIT_LEAVES);
        this.orangeTree = features.fruitTree("orange_tree", TropicraftBlocks.ORANGE_SAPLING, TropicraftBlocks.ORANGE_LEAVES);
        this.lemonTree = features.fruitTree("lemon_tree", TropicraftBlocks.LEMON_SAPLING, TropicraftBlocks.LEMON_LEAVES);
        this.limeTree = features.fruitTree("lime_tree", TropicraftBlocks.LIME_SAPLING, TropicraftBlocks.LIME_LEAVES);

        this.normalPalmTree = features.sparseTree("normal_palm_tree", TropicraftFeatures.NORMAL_PALM_TREE, FeatureConfiguration.NONE, 0.2F);
        this.curvedPalmTree = features.sparseTree("curved_palm_tree", TropicraftFeatures.CURVED_PALM_TREE, FeatureConfiguration.NONE, 0.2F);
        this.largePalmTree = features.sparseTree("large_palm_tree", TropicraftFeatures.LARGE_PALM_TREE, FeatureConfiguration.NONE, 0.2F);

        this.rainforestUpTree = features.sparseTree("rainforest_up_tree", TropicraftFeatures.UP_TREE, FeatureConfiguration.NONE, 0.2F);
        this.rainforestSmallTualung = features.sparseTree("rainforest_small_tualung", TropicraftFeatures.SMALL_TUALUNG, FeatureConfiguration.NONE, 0.3F);
        this.rainforestLargeTualung = features.sparseTree("rainforest_large_tualung", TropicraftFeatures.LARGE_TUALUNG, FeatureConfiguration.NONE, 0.4F);
        this.rainforestTallTree = features.sparseTree("rainforest_tall_tree", TropicraftFeatures.TALL_TREE, FeatureConfiguration.NONE, 0.5F);
        this.rainforestVines = features.register("rainforest_vines", TropicraftFeatures.VINES,
                f -> f.configured(new RainforestVinesConfig()).squared().count(50)
        );

        this.smallGoldenLeatherFern = features.register("small_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, SimpleBlockPlacer.INSTANCE)
                    .tries(12)
                    .build()
            ).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE);
        });

        this.tallGoldenLeatherFern = features.register("tall_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, DoublePlantPlacer.INSTANCE)
                    .tries(6)
                    .build()
            ).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE);
        });

        this.hugeGoldenLeatherFern = features.register("huge_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, HugePlantBlockPlacer.INSTANCE)
                    .tries(3)
                    .build()
            ).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE);
        });

        this.overgrownSmallGoldenLeatherFern = features.register("overgrown_small_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, SimpleBlockPlacer.INSTANCE)
                    .tries(28)
                    .build()
            ).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(10);
        });

        this.overgrownTallGoldenLeatherFern = features.register("overgrown_tall_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, DoublePlantPlacer.INSTANCE)
                    .tries(16)
                    .build()
            ).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(8);
        });

        this.overgrownHugeGoldenLeatherFern = features.register("overgrown_huge_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, HugePlantBlockPlacer.INSTANCE)
                    .tries(8)
                    .build()
            ).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(6);
        });

        this.pleodendron = features.tree("pleodendron",
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(Blocks.JUNGLE_LOG.defaultBlockState()),
                        new PleodendronTrunkPlacer(10, 8, 0),
                        BlockStateProvider.simple(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                        new PleodendronFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1),
                        new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
                ).build(),
                0, 0.08f, 1);

        this.papaya = features.tree("papaya",
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LOG.get().defaultBlockState()),
                        new StraightTrunkPlacer(5, 2, 3),
                        BlockStateProvider.simple(TropicraftBlocks.PAPAYA_LEAVES.get().defaultBlockState()),
                        new PapayaFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                        new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
                )
                .decorators(ImmutableList.of(TropicraftTrees.BEEHIVE_005, new PapayaTreeDecorator()))
                .build(),
                0, 0.2f, 1
        );

        FoliagePlacer mangroveFoliage = new MangroveFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0));
        BlockStateProvider redMangroveLog = BlockStateProvider.simple(TropicraftBlocks.RED_MANGROVE_LOG.get().defaultBlockState());
        BlockStateProvider lightMangroveLog = BlockStateProvider.simple(TropicraftBlocks.LIGHT_MANGROVE_LOG.get().defaultBlockState());
        BlockStateProvider blackMangroveLog = BlockStateProvider.simple(TropicraftBlocks.BLACK_MANGROVE_LOG.get().defaultBlockState());
        Block redMangroveRoots = TropicraftBlocks.RED_MANGROVE_ROOTS.get();
        Block lightMangroveRoots = TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get();
        Block blackMangroveRoots = TropicraftBlocks.BLACK_MANGROVE_ROOTS.get();

        BlockStateProvider redMangroveLeaves = BlockStateProvider.simple(TropicraftBlocks.RED_MANGROVE_LEAVES.get().defaultBlockState());
        BlockStateProvider tallMangroveLeaves = BlockStateProvider.simple(TropicraftBlocks.TALL_MANGROVE_LEAVES.get().defaultBlockState());
        BlockStateProvider teaMangroveLeaves = BlockStateProvider.simple(TropicraftBlocks.TEA_MANGROVE_LEAVES.get().defaultBlockState());
        BlockStateProvider blackMangroveLeaves = BlockStateProvider.simple(TropicraftBlocks.BLACK_MANGROVE_LEAVES.get().defaultBlockState());

        TwoLayersFeatureSize mangroveMinimumSize = new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4));

        MangroveTrunkPlacer redMangroveTrunk = new MangroveTrunkPlacer(3, 3, 0, redMangroveRoots, true, false);
        this.redMangroveShort = features.mangrove("red_mangrove_short",
                new TreeConfiguration.TreeConfigurationBuilder(redMangroveLog, redMangroveTrunk, redMangroveLeaves, mangroveFoliage, mangroveMinimumSize)
                        .decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR))
                        .build(),
                1
        );
        this.redMangroveSmall = features.mangrove("red_mangrove_small",
                new TreeConfiguration.TreeConfigurationBuilder(
                        redMangroveLog,
                        new SmallMangroveTrunkPlacer(2, 1, 0, redMangroveRoots),
                        redMangroveLeaves,
                        new SmallMangroveFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.SMALL)).build(),
                0
        );
        this.redMangrove = features.random("red_mangrove", this.redMangroveShort, this.redMangroveSmall);

        this.tallMangrove = features.mangrove("tall_mangrove",
                new TreeConfiguration.TreeConfigurationBuilder(
                        lightMangroveLog,
                        new MangroveTrunkPlacer(7, 4, 2, lightMangroveRoots, false, false),
                        tallMangroveLeaves,
                        mangroveFoliage,
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR)).build(),
                2
        );

        PneumatophoresTreeDecorator teaMangrovePneumatophores = new PneumatophoresTreeDecorator(lightMangroveRoots, 2, 6, 4);
        this.teaMangrove = features.mangrove("tea_mangrove",
                new TreeConfiguration.TreeConfigurationBuilder(
                        lightMangroveLog,
                        new MangroveTrunkPlacer(5, 3, 0, lightMangroveRoots, false, true),
                        teaMangroveLeaves,
                        mangroveFoliage,
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR, teaMangrovePneumatophores)).build(),
                1
        );

        PneumatophoresTreeDecorator blackMangrovePneumatophores = new PneumatophoresTreeDecorator(blackMangroveRoots, 8, 16, 6);
        this.blackMangrove = features.mangrove("black_mangrove",
                new TreeConfiguration.TreeConfigurationBuilder(
                        blackMangroveLog,
                        new MangroveTrunkPlacer(4, 3, 0, blackMangroveRoots, true, false),
                        blackMangroveLeaves,
                        mangroveFoliage,
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(BEEHIVE_002, PianguasTreeDecorator.REGULAR, blackMangrovePneumatophores)).build(),
                1
        );
        this.lightMangroves = features.random("light_mangroves", this.tallMangrove, this.teaMangrove, this.blackMangrove);

        this.mangroves = features.random("mangroves", this.redMangrove, this.lightMangroves);

        this.mangroveVegetation = features.register("mangrove_vegetation", this.mangroves
                    .decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                    .decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured(new NoiseCountFactorDecoratorConfiguration(7, 200.0, 1.5)))
        );

        this.sparseMangroveVegetation = features.register("sparse_mangrove_vegetation", this.mangroves
                .decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                .decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured(new NoiseCountFactorDecoratorConfiguration(3, 200.0, 1.5)))
        );

        this.mudDisk = features.register("mud_disk", Feature.DISK, feature -> feature
                .configured(new DiskConfiguration(
                        TropicraftBlocks.MUD.get().defaultBlockState(),
                        UniformInt.of(2, 4),
                        2,
                        ImmutableList.of(Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState())
                ))
                .decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).count(3)
        );

        this.eih = features.noConfig("eih", TropicraftFeatures.EIH,
                f -> f.decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.01F, 1)))
        );

        this.tropicsGrass = features.register("tropics_grass", Feature.RANDOM_PATCH,
                f -> f.configured(VegetationFeatures.PATCH_GRASS_JUNGLE.config)
                        .decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).
                        count(10));

//        this.bamboo = features.register("bamboo", Feature.BAMBOO,
//                f -> f.configured(new ProbabilityFeatureConfiguration(0.15F))
//                        .decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE)
//                        .squared()
//                        .decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured(new NoiseCountFactorDecoratorConfiguration(70, 140.0D, 0.3D)))
//        );

        this.pineapplePatch = features.register("pineapple_patch", Feature.RANDOM_PATCH, feature -> {
            SimpleStateProvider state = BlockStateProvider.simple(TropicraftBlocks.PINEAPPLE.get().defaultBlockState());
            return feature.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(state, new DoublePlantPlacer())
                    .tries(64)
                    .noProjection()
                    .build()
            ).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE);
        });

        this.tropicsFlowers = features.register("tropics_flowers", Feature.FLOWER, feature -> {
            BlockStateProvider stateProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.TROPICS_FLOWERS);
            //SimpleStateProvider stateProvider = BlockStateProvider.simple(TropicraftBlocks.FLOWERS.get(TropicraftFlower.MAGIC_MUSHROOM).get().defaultBlockState());
            RandomPatchConfiguration config = new RandomPatchConfiguration.GrassConfigurationBuilder(stateProvider, SimpleBlockPlacer.INSTANCE).tries(64).build();
            return feature.configured(config).decorated(Features.Decorators.ADD_32.decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(12));
        });
        this.rainforestFlowers = features.register("rainforest_flowers", Feature.FLOWER, feature -> {
            BlockStateProvider stateProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.RAINFOREST_FLOWERS);
            //SimpleStateProvider stateProvider = BlockStateProvider.simple(TropicraftBlocks.FLOWERS.get(TropicraftFlower.MAGIC_MUSHROOM).get().defaultBlockState());
            RandomPatchConfiguration config = new RandomPatchConfiguration.GrassConfigurationBuilder(stateProvider, SimpleBlockPlacer.INSTANCE).tries(64).noProjection().build();
            return feature.configured(config).decorated(Features.Decorators.ADD_32.decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(4));
        });
        this.irisFlowers = features.register("iris_flowers", Feature.RANDOM_PATCH, feature -> {
            BlockStateProvider stateProvider = BlockStateProvider.simple(TropicraftBlocks.IRIS.get().defaultBlockState());
            RandomPatchConfiguration config = new RandomPatchConfiguration.GrassConfigurationBuilder(stateProvider, new DoublePlantPlacer()).tries(64).noProjection().build();
            return feature.configured(config).decorated(Features.Decorators.ADD_32.decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(10));
        });

        this.coffeeBush = features.noConfig("coffee_bush", TropicraftFeatures.COFFEE_BUSH, feature -> {
            return feature.decorated(Features.Decorators.ADD_32.decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(5));
        });
        this.undergrowth = features.noConfig("undergrowth", TropicraftFeatures.UNDERGROWTH, feature -> {
            return feature.decorated(Features.Decorators.ADD_32.decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(100));
        });
        this.singleUndergrowth = features.noConfig("single_undergrowth", TropicraftFeatures.SINGLE_UNDERGROWTH, feature -> {
            return feature.decorated(Features.Decorators.HEIGHTMAP_SQUARE.count(2));
        });

        this.seagrass = features.register("seagrass", Feature.SEAGRASS, feature -> {
            return feature.configured(new ProbabilityFeatureConfiguration(0.3f)).count(48).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).count(3);
        });

        final BlockStateProvider seaGrass = BlockStateProvider.simple(Blocks.SEAGRASS.defaultBlockState());

        this.undergroundSeagrassOnStone = features.register("underground_seagrass_on_stone", Feature.SIMPLE_BLOCK, feature -> {
            SimpleBlockConfiguration config = new SimpleBlockConfiguration(
                    seaGrass,
                    ImmutableList.of(Blocks.STONE.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState())
            );
            return feature.configured(config).decorated(FeatureDecorator.CARVING_MASK.configured(new CarvingMaskDecoratorConfiguration(GenerationStep.Carving.LIQUID)));
        });
        this.undergroundSeagrassOnDirt = features.register("underground_seagrass_on_dirt", Feature.SIMPLE_BLOCK, feature -> {
            SimpleBlockConfiguration config = new SimpleBlockConfiguration(
                    seaGrass,
                    ImmutableList.of(Blocks.DIRT.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState())
            );
            return feature.configured(config).decorated(FeatureDecorator.CARVING_MASK.configured(new CarvingMaskDecoratorConfiguration(GenerationStep.Carving.LIQUID)));
        });
        this.undergroundSeaPickles = features.noConfig("underground_sea_pickles", TropicraftFeatures.UNDERGROUND_SEA_PICKLE, feature -> {
            return feature.decorated(FeatureDecorator.CARVING_MASK.configured(new CarvingMaskDecoratorConfiguration(GenerationStep.Carving.LIQUID)));
        });

        this.mangroveReeds = features.noConfig("mangrove_reeds", TropicraftFeatures.REEDS, feature -> {
            return feature.decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).count(2);
        });

        this.azurite = features.register("azurite", Feature.ORE, f -> {
            return f.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, TropicraftBlocks.AZURITE_ORE.get().defaultBlockState(), 8))
                    .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(range(0, 128))))
                    .squared().count(3);
        });
        this.eudialyte = features.register("eudialyte", Feature.ORE, f -> {
            return f.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, TropicraftBlocks.EUDIALYTE_ORE.get().defaultBlockState(), 12))
                    .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(range(0, 128))))
                    .squared().count(10);
        });
        this.zircon = features.register("zircon", Feature.ORE, f -> {
            return f.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, TropicraftBlocks.ZIRCON_ORE.get().defaultBlockState(), 14))
                    .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(range(0, 128))))
                    .squared().count(15);
        });
        this.manganese = features.register("manganese", Feature.ORE, f -> {
            return f.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, TropicraftBlocks.MANGANESE_ORE.get().defaultBlockState(), 10))
                    .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(range(0, 32))))
                    .squared().count(8);
        });
        this.shaka = features.register("shaka", Feature.ORE, f -> {
            return f.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, TropicraftBlocks.SHAKA_ORE.get().defaultBlockState(), 8))
                    .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(range(0, 32))))
                    .squared().count(6);
        });
    }

    private static HeightProvider range(int min, int max) {
        return UniformHeight.of(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max));
    }

    public void addFruitTrees(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.grapefruitTree);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.orangeTree);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.lemonTree);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.limeTree);
    }

    public void addPalmTrees(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.normalPalmTree);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.curvedPalmTree);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.largePalmTree);
    }

    public void addRainforestTrees(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.rainforestUpTree);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.rainforestSmallTualung);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.rainforestLargeTualung);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.rainforestTallTree);
    }

    public void addRainforestPlants(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_MELON);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.rainforestVines);
    }

    public void addMangroveVegetation(BiomeGenerationSettings.Builder generation, boolean sparse) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, sparse ? this.sparseMangroveVegetation : this.mangroveVegetation);
    }

    public void addOvergrownGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.overgrownSmallGoldenLeatherFern);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.overgrownTallGoldenLeatherFern);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.overgrownHugeGoldenLeatherFern);
    }

    public void addGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.smallGoldenLeatherFern);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.tallGoldenLeatherFern);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.hugeGoldenLeatherFern);
    }

    public void addPleodendron(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.pleodendron);
    }

    public void addPapaya(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.papaya);
    }

    public void addMudDisks(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.mudDisk);
    }

    public void addMangroveReeds(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.mangroveReeds);
    }

    public void addTropicsGrass(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.tropicsGrass);
    }

    public void addBamboo(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.bamboo);
    }

    public void addPineapples(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.pineapplePatch);
    }

    public void addEih(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.eih);
    }

    public void addTropicsFlowers(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.tropicsFlowers);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.irisFlowers);
    }

    public void addTropicsGems(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.azurite);
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.eudialyte);
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.zircon);
    }

    public void addTropicsMetals(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.manganese);
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.shaka);
    }

    public void addUndergroundSeagrass(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.undergroundSeagrassOnStone);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.undergroundSeagrassOnDirt);
    }

    public void addRegularSeagrass(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.seagrass);
    }

    public void addUndergroundPickles(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.undergroundSeaPickles);
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredFeature<?, ?>> worldgen;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredFeature<?, ?>>) worldgen;
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<?, ?> feature) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), feature);
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> register(String id, F feature, Function<F, ConfiguredFeature<?, ?>> configure) {
            return this.register(id, configure.apply(feature));
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> register(String id, RegistryObject<F> feature, Function<F, ConfiguredFeature<?, ?>> configure) {
            return this.register(id, feature.get(), configure);
        }

        public <F extends Feature<NoneFeatureConfiguration>> ConfiguredFeature<?, ?> noConfig(String id, RegistryObject<F> feature, UnaryOperator<ConfiguredFeature<?, ?>> configure) {
            return this.register(id, feature, f -> configure.apply(f.configured(NoneFeatureConfiguration.INSTANCE)));
        }

        public ConfiguredFeature<?, ?> fruitTree(String id, Supplier<? extends Block> sapling, Supplier<? extends Block> fruitLeaves) {
            TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(Blocks.OAK_LOG.defaultBlockState()),
                    new CitrusTrunkPlacer(6, 3, 0),
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1)
                            .add(fruitLeaves.get().defaultBlockState(), 1)
                            .build()
                    ),
                    BlockStateProvider.simple(sapling.get().defaultBlockState()),
                    new CitrusFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                    new TwoLayersFeatureSize(1, 0, 2)
            ).build();

            return this.tree(id, config, 0, 0.1F, 1);
        }

        public <C extends FeatureConfiguration, F extends Feature<C>> ConfiguredFeature<?, ?> sparseTree(String id, RegistryObject<F> feature, C config, float chance) {
            return this.register(id, feature, f -> {
                return f.configured(config).decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, chance, 1)));
            });
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> tree(String id, TreeConfiguration config, int count, float extraChance, int extraCount) {
            return this.register(id, Feature.TREE, feature -> {
                return feature.configured(config)
                        .decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(count, extraChance, extraCount)));
            });
        }

        public ConfiguredFeature<?, ?> mangrove(String id, TreeConfiguration config, int maxWaterDepth) {
            return this.register(id, TropicraftFeatures.MANGROVE_TREE.get(), feature -> {
                ConfiguredFeature<TreeConfiguration, ?> configured = feature.configured(config);

                if (maxWaterDepth > 0) {
                    return configured.decorated(FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(maxWaterDepth)));
                }

                return configured;
            });
        }

        public ConfiguredFeature<?, ?> random(String id, ConfiguredFeature<?, ?>... choices) {
            if (choices.length == 2) {
                ConfiguredFeature<?, ?> left = choices[0];
                ConfiguredFeature<?, ?> right = choices[1];
                return this.register(id, Feature.RANDOM_BOOLEAN_SELECTOR, feature -> {
                    return feature.configured(new RandomBooleanFeatureConfiguration(() -> left, () -> right));
                });
            }

            return this.register(id, Feature.SIMPLE_RANDOM_SELECTOR, feature -> {
                SimpleRandomFeatureConfiguration config = new SimpleRandomFeatureConfiguration(Arrays.stream(choices)
                        .map(c -> (Supplier<ConfiguredFeature<?, ?>>) () -> c)
                        .collect(Collectors.toList()));
                return feature.configured(config);
            });
        }
    }
}
