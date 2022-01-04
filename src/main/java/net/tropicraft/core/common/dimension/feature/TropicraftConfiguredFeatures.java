package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
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
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftTrees;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
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
import org.jetbrains.annotations.NotNull;
import net.tropicraft.core.mixin.PlacedFeatureAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.worldSurfaceSquaredWithCount;
import static net.tropicraft.core.common.block.TropicraftTrees.BEEHIVE_002;

public final class TropicraftConfiguredFeatures {
    public final PlacedFeature grapefruitTree;
    public final PlacedFeature orangeTree;
    public final PlacedFeature lemonTree;
    public final PlacedFeature limeTree;
    public final PlacedFeature normalPalmTree;
    public final PlacedFeature curvedPalmTree;
    public final PlacedFeature largePalmTree;
    public final PlacedFeature rainforestUpTree;
    public final PlacedFeature rainforestSmallTualung;
    public final PlacedFeature rainforestLargeTualung;
    public final PlacedFeature rainforestTallTree;
    public final PlacedFeature rainforestVines;
    public final PlacedFeature eih;
    public final PlacedFeature tropicsGrass;
    public final PlacedFeature bamboo;

    public final PlacedFeature redMangroveShort;
    public final PlacedFeature redMangroveSmall;
    public final PlacedFeature redMangrove;

    public final PlacedFeature blackMangrove;
    public final PlacedFeature tallMangrove;
    public final PlacedFeature teaMangrove;
    public final PlacedFeature lightMangroves;

    public final PlacedFeature mangroves;
    public final PlacedFeature papaya;

    public final PlacedFeature mangroveVegetation;
    public final PlacedFeature sparseMangroveVegetation;

    public final PlacedFeature mudDisk;

    public final PlacedFeature smallGoldenLeatherFern;
    public final PlacedFeature tallGoldenLeatherFern;
    public final PlacedFeature hugeGoldenLeatherFern;
    public final PlacedFeature overgrownSmallGoldenLeatherFern;
    public final PlacedFeature overgrownTallGoldenLeatherFern;
    public final PlacedFeature overgrownHugeGoldenLeatherFern;

    public final PlacedFeature pleodendron;

    public final PlacedFeature pineapplePatch;
    public final PlacedFeature tropicsFlowers;
    public final PlacedFeature rainforestFlowers;
    public final PlacedFeature irisFlowers;

    public final PlacedFeature coffeeBush;
    public final PlacedFeature undergrowth;
    public final PlacedFeature singleUndergrowth;

    public final PlacedFeature seagrass;
    public final PlacedFeature undergroundSeagrassOnStone;
    public final PlacedFeature undergroundSeagrassOnDirt;
    public final PlacedFeature undergroundSeaPickles;

    public final PlacedFeature mangroveReeds;

    public final PlacedFeature azurite;
    public final PlacedFeature eudialyte;
    public final PlacedFeature zircon;
    public final PlacedFeature manganese;
    public final PlacedFeature shaka;

    public TropicraftConfiguredFeatures(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen, WorldgenDataConsumer<PlacedFeature> placed) {
        Register features = new Register(worldgen, placed);

        this.grapefruitTree = features.fruitTree("grapefruit_tree", TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftBlocks.GRAPEFRUIT_LEAVES);
        this.orangeTree = features.fruitTree("orange_tree", TropicraftBlocks.ORANGE_SAPLING, TropicraftBlocks.ORANGE_LEAVES);
        this.lemonTree = features.fruitTree("lemon_tree", TropicraftBlocks.LEMON_SAPLING, TropicraftBlocks.LEMON_LEAVES);
        this.limeTree = features.fruitTree("lime_tree", TropicraftBlocks.LIME_SAPLING, TropicraftBlocks.LIME_LEAVES);

        this.normalPalmTree = features.sparseTree("normal_palm_tree", TropicraftFeatures.NORMAL_PALM_TREE, FeatureConfiguration.NONE, TropicraftBlocks.PALM_SAPLING, 0.2F);
        this.curvedPalmTree = features.sparseTree("curved_palm_tree", TropicraftFeatures.CURVED_PALM_TREE, FeatureConfiguration.NONE, TropicraftBlocks.PALM_SAPLING, 0.2F);
        this.largePalmTree = features.sparseTree("large_palm_tree", TropicraftFeatures.LARGE_PALM_TREE, FeatureConfiguration.NONE, TropicraftBlocks.PALM_SAPLING, 0.2F);

        this.rainforestUpTree = features.sparseTree("rainforest_up_tree", TropicraftFeatures.UP_TREE, FeatureConfiguration.NONE, TropicraftBlocks.MAHOGANY_SAPLING, 0.2F);
        this.rainforestSmallTualung = features.sparseTree("rainforest_small_tualung", TropicraftFeatures.SMALL_TUALUNG, FeatureConfiguration.NONE, TropicraftBlocks.MAHOGANY_SAPLING, 0.25F);
        this.rainforestLargeTualung = features.sparseTree("rainforest_large_tualung", TropicraftFeatures.LARGE_TUALUNG, FeatureConfiguration.NONE, TropicraftBlocks.MAHOGANY_SAPLING, 0.5F);
        this.rainforestTallTree = features.sparseTree("rainforest_tall_tree", TropicraftFeatures.TALL_TREE, FeatureConfiguration.NONE, TropicraftBlocks.MAHOGANY_SAPLING, 0.5F);

        this.rainforestVines = features.registerPlaced("rainforest_vines", TropicraftFeatures.VINES,
                f -> f.configured(new RainforestVinesConfig()), f -> f.placed(CountPlacement.of(50), InSquarePlacement.spread())
        );

        // TODO 1.18 adjust rarity
        this.smallGoldenLeatherFern = features.registerPlaced(
                "small_golden_leather_fern",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.GOLDEN_LEATHER_FERN),
                f -> f.placed(RarityFilter.onAverageOnceEvery(45), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        this.tallGoldenLeatherFern = features.registerPlaced(
                "tall_golden_leather_fern",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN),
                f -> f.placed(RarityFilter.onAverageOnceEvery(90), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        this.hugeGoldenLeatherFern = features.registerPlaced(
                "huge_golden_leather_fern",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN),
                f -> f.placed(RarityFilter.onAverageOnceEvery(150), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        this.overgrownSmallGoldenLeatherFern = features.registerPlaced(
                "overgrown_small_golden_leather_fern",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.GOLDEN_LEATHER_FERN),
                f -> f.placed(RarityFilter.onAverageOnceEvery(35), CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        this.overgrownTallGoldenLeatherFern = features.registerPlaced(
                "overgrown_tall_golden_leather_fern",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN),
                f -> f.placed(RarityFilter.onAverageOnceEvery(60), CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        this.overgrownHugeGoldenLeatherFern = features.registerPlaced(
                "overgrown_huge_golden_leather_fern",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN),
                f -> f.placed(RarityFilter.onAverageOnceEvery(90), CountPlacement.of(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        this.pleodendron = features.tree("pleodendron",
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(Blocks.JUNGLE_LOG.defaultBlockState()),
                        new PleodendronTrunkPlacer(10, 8, 0),
                        BlockStateProvider.simple(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                        new PleodendronFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1),
                        new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
                ).build(),
                () -> Blocks.JUNGLE_SAPLING,
                0, 0.1f, 1);

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
                TropicraftBlocks.PAPAYA_SAPLING,
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
                TropicraftBlocks.RED_MANGROVE_PROPAGULE,
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
                TropicraftBlocks.RED_MANGROVE_PROPAGULE,
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
                TropicraftBlocks.TALL_MANGROVE_PROPAGULE,
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
                TropicraftBlocks.TEA_MANGROVE_PROPAGULE,
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
                TropicraftBlocks.BLACK_MANGROVE_PROPAGULE,
                1
        );
        this.lightMangroves = features.random("light_mangroves", this.tallMangrove, this.teaMangrove, this.blackMangrove);

        this.mangroves = features.random("mangroves", this.redMangrove, this.lightMangroves);

        this.mangroveVegetation = features.registerPlaced("mangrove_vegetation", this.mangroves,
                    f -> f.placed(
                            NoiseBasedCountPlacement.of(7, 200.0, 1.5),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR)
                    )
        );

        this.sparseMangroveVegetation = features.registerPlaced("sparse_mangrove_vegetation", this.mangroves,
                f -> f.placed(
                        NoiseBasedCountPlacement.of(3, 200.0, 1.5),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR)
                )
        );

        this.mudDisk = features.registerPlaced(
                "mud_disk",
                Feature.DISK.configured(new DiskConfiguration(
                        TropicraftBlocks.MUD.get().defaultBlockState(),
                        UniformInt.of(2, 4),
                        2,
                        ImmutableList.of(Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState())
                )).placed(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome())
        );

        this.eih = features.noConfig(
                "eih",
                TropicraftFeatures.EIH,
                f -> f.placed(PlacementUtils.countExtra(0, 0.1f, 1),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
        );

        this.tropicsGrass = features.registerPlaced(
                "tropics_grass",
                Feature.RANDOM_PATCH,
                f -> f.configured(VegetationFeatures.PATCH_GRASS_JUNGLE.config),
                f -> f.placed(worldSurfaceSquaredWithCount(10))
        );

        this.bamboo = features.registerPlaced(
                "bamboo",
                Feature.BAMBOO.configured(new ProbabilityFeatureConfiguration(0.15F)).placed(
                        NoiseBasedCountPlacement.of(70, 140.0D, 0.3D),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                )
        );

        this.pineapplePatch = features.registerPlaced(
                "pineapple_patch",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.PINEAPPLE),
                f -> f.placed(RarityFilter.onAverageOnceEvery(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
        );

        BlockStateProvider tropicsFlowersProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.TROPICS_FLOWERS);
        this.tropicsFlowers = features.registerPlaced(
                "tropics_flowers",
                Feature.FLOWER,
                f -> features.randomPatch(tropicsFlowersProvider),
                f -> f.placed(worldSurfaceSquaredWithCount(12))
        );

        BlockStateProvider rainforestFlowersProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.RAINFOREST_FLOWERS);
        this.rainforestFlowers = features.registerPlaced(
                "rainforest_flowers",
                Feature.FLOWER,
                f -> features.randomPatch(rainforestFlowersProvider),
                f -> f.placed(worldSurfaceSquaredWithCount(4))
        );

        this.irisFlowers = features.registerPlaced(
                "iris_flowers",
                Feature.RANDOM_PATCH,
                f -> features.randomPatch(TropicraftBlocks.IRIS),
                f -> f.placed(worldSurfaceSquaredWithCount(10))
        );

        this.coffeeBush = features.noConfig(
                "coffee_bush",
                TropicraftFeatures.COFFEE_BUSH,
                f -> f.placed(worldSurfaceSquaredWithCount(5))
        );

        this.undergrowth = features.noConfig(
                "undergrowth",
                TropicraftFeatures.UNDERGROWTH,
                f -> f.placed(worldSurfaceSquaredWithCount(100))
        );

        this.singleUndergrowth = features.noConfig(
                "single_undergrowth",
                TropicraftFeatures.SINGLE_UNDERGROWTH,
                f -> f.placed(worldSurfaceSquaredWithCount(2))
        );

        this.seagrass = features.registerPlaced("seagrass", Feature.SEAGRASS,
                feature -> feature.configured(new ProbabilityFeatureConfiguration(0.3f)),
                feature -> feature.placed(CountPlacement.of(48), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG))
        );

        this.undergroundSeagrassOnStone = features.registerPlaced(
                "underground_seagrass_on_stone",
                Feature.SIMPLE_BLOCK,
                f -> f.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SEAGRASS))),
                f -> f.placed(getSeagrassPlacementModifiers(() -> Blocks.STONE))
        );


        this.undergroundSeagrassOnDirt = features.registerPlaced(
                "underground_seagrass_on_dirt",
                Feature.SIMPLE_BLOCK,
                f -> f.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SEAGRASS))),
                f -> f.placed(getSeagrassPlacementModifiers(() -> Blocks.DIRT))
        );

        this.undergroundSeaPickles = features.noConfig(
                "underground_sea_pickles",
                TropicraftFeatures.UNDERGROUND_SEA_PICKLE,
                f -> f.placed(
                        ImmutableList.of(CarvingMaskPlacement.forStep(GenerationStep.Carving.LIQUID),
                                RarityFilter.onAverageOnceEvery(10),
                                BiomeFilter.biome())
                )
        );

        this.mangroveReeds = features.noConfig(
                "mangrove_reeds",
                TropicraftFeatures.REEDS,
                feature -> feature.placed(CountPlacement.of(48),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG)
                )
        );

        // TODO 1.18 decide ore placements in 1.18
        this.azurite = features.placedOre(features,
                "azurite",
                8,
                TropicraftBlocks.AZURITE_ORE,
                3,
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
        );

        this.eudialyte = features.placedOre(features,
                "eudialyte",
                12,
                TropicraftBlocks.EUDIALYTE_ORE,
                10,
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
        );

        this.zircon = features.placedOre(features,
                "zircon",
                14,
                TropicraftBlocks.ZIRCON_ORE,
                15,
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
        );

        this.manganese = features.placedOre(features,
                "manganese",
                10,
                TropicraftBlocks.MANGANESE_ORE,
                8,
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-16), VerticalAnchor.aboveBottom(32))
        );

        this.shaka = features.placedOre(features,
                "shaka",
                8,
                TropicraftBlocks.SHAKA_ORE,
                6,
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-16), VerticalAnchor.aboveBottom(32))
        );
    }

    @NotNull
    private ImmutableList<PlacementModifier> getSeagrassPlacementModifiers(final Supplier<Block> belowBlock) {
        final BlockPredicateFilter seagrassPredicate = BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesBlock(belowBlock.get(), new BlockPos(0, -1, 0)),
                BlockPredicate.matchesBlock(Blocks.WATER, BlockPos.ZERO),
                BlockPredicate.matchesBlock(Blocks.WATER, new BlockPos(0, 1, 0))));

        return ImmutableList.of(CarvingMaskPlacement.forStep(GenerationStep.Carving.LIQUID),
                RarityFilter.onAverageOnceEvery(10),
                seagrassPredicate,
                BiomeFilter.biome());
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
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON);
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
        private final WorldgenDataConsumer<PlacedFeature> placed;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen, WorldgenDataConsumer<PlacedFeature> placed) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredFeature<?, ?>>) worldgen;
            this.placed = placed;
        }

        // Register configured feature
        public <F extends Feature<?>> ConfiguredFeature<?, ?> registerConfigured(String id, ConfiguredFeature<?, ?> feature) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), feature);
        }

        // Register placed feature
        public PlacedFeature registerPlaced(String id, PlacedFeature feature) {
            return this.placed.register(new ResourceLocation(Constants.MODID, id), feature);
        }

        // Add placements on top of an existing placed feature, removing any that already exist (intended for use with configured selector features which have no placements anyway)
        public PlacedFeature registerPlaced(String id, PlacedFeature feature, Function<ConfiguredFeature<?, ?>, PlacedFeature> extra) {
            return this.placed.register(new ResourceLocation(Constants.MODID, id), extra.apply(unwrap(feature)));
        }

        // Configure a feature with the configure function, and place it with the place function
        public <F extends Feature<?>, CF extends ConfiguredFeature<?, ?>> PlacedFeature registerPlaced(String id, F feature, Function<F, CF> configure, Function<CF, PlacedFeature> place) {
            CF configured = configure.apply(feature);
            this.registerConfigured(id, configured);
            return this.registerPlaced(id, place.apply(configured));
        }

        // Call above method but RegistryObject<Feature> -> Feature
        public <F extends Feature<?>, CF extends ConfiguredFeature<?, ?>> PlacedFeature registerPlaced(String id, RegistryObject<F> feature, Function<F, CF> configure, Function<CF, PlacedFeature> place) {
            return this.registerPlaced(id, feature.get(), configure, place);
        }

        public <F extends Feature<NoneFeatureConfiguration>, CF extends ConfiguredFeature<?, ?>> PlacedFeature noConfig(String id, RegistryObject<F> feature, Function<CF, PlacedFeature> place) {
            return this.registerPlaced(id, feature, f -> (CF) f.configured(NoneFeatureConfiguration.INSTANCE), place);
        }

        public ConfiguredFeature<?, ?> randomPatch(final Supplier<? extends Block> block) {
            return randomPatch(BlockStateProvider.simple(block.get()));
        }

        public ConfiguredFeature<?, ?> randomPatch(final BlockStateProvider blockStateProvider) {
            return Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(blockStateProvider))));
        }

        private PlacedFeature placedOre(Register register, final String name, final int blobSize, final Supplier<? extends Block> block, final int count, PlacementModifier modifier) {
            return register.registerPlaced(
                    name,
                    Feature.ORE,
                    f -> register.configuredOre(blobSize, block),
                    f -> f.placed(rareOrePlacement(count, modifier))
            );
        }

        // TODO add deepslate / tropicraft equivalent replacement here
        public ConfiguredFeature<?, ?> configuredOre(final int blobSize, final Supplier<? extends Block> block) {
            return Feature.ORE.configured(new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState(), blobSize));
        }

        public PlacedFeature fruitTree(String id, Supplier<? extends Block> sapling, Supplier<? extends Block> fruitLeaves) {
            TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
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

            return this.tree(id, config, sapling, 0, 0.1F, 1);
        }

        public <F extends Feature<FC>, FC extends FeatureConfiguration> PlacedFeature sparseTree(String id, RegistryObject<F> feature, FC config, Supplier<? extends Block> sapling, float chance) {
            return this.registerPlaced(id, feature,  (F f) -> f.configured(config),
                    f -> f.placed(PlacementUtils.countExtra(0, chance, 1), InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.get().defaultBlockState(), BlockPos.ZERO)))
            );
        }

        public PlacedFeature tree(String id, TreeConfiguration config, Supplier<? extends Block> sapling, int count, float extraChance, int extraCount) {
            return this.registerPlaced(id, Feature.TREE, feature -> feature.configured(config), feature ->
                    feature.placed(
                            PlacementUtils.countExtra(count, extraChance, extraCount),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP,
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.get().defaultBlockState(), BlockPos.ZERO)))
            );
        }

        public PlacedFeature mangrove(String id, TreeConfiguration config, Supplier<? extends Block> sapling, int maxWaterDepth) {
            return this.registerPlaced(id, TropicraftFeatures.MANGROVE_TREE.get(), feature -> {
                ConfiguredFeature<TreeConfiguration, ?> configured = feature.configured(config);

                // TODO: do we need the if?
//                if (maxWaterDepth > 0) {
//                    return configured.decorated(FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(maxWaterDepth)));
//                }

                return configured;
            }, f -> f.placed(SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth),
                    BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.get().defaultBlockState(), BlockPos.ZERO))
                    ));
        }

        public PlacedFeature random(String id, PlacedFeature... choices) {
            if (choices.length == 2) {
                PlacedFeature left = choices[0];
                PlacedFeature right = choices[1];
                return this.registerPlaced(id, Feature.RANDOM_BOOLEAN_SELECTOR, feature -> {
                    return feature.configured(new RandomBooleanFeatureConfiguration(() -> left, () -> right));
                }, ConfiguredFeature::placed);
            }

            return this.registerPlaced(id, Feature.SIMPLE_RANDOM_SELECTOR, feature -> {
                SimpleRandomFeatureConfiguration config = new SimpleRandomFeatureConfiguration(Arrays.stream(choices)
                        .map(c -> (Supplier<PlacedFeature>) (() -> c))
                        .collect(Collectors.toList()));

                return feature.configured(config);
            }, ConfiguredFeature::placed);
        }
    }

    // Placed feature -> configured feature, with some spicy hacks
    // Not safe under normal circumstances but as we're only using it for our own features it should be fine
    private static ConfiguredFeature<?, ?> unwrap(PlacedFeature feature) {
        return ((PlacedFeatureAccessor)feature).getFeature().get();
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }
}
