package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.AquaticFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.tropicraft.Constants;

import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.worldSurfaceSquaredWithCount;
import static net.tropicraft.core.common.dimension.feature.TropicraftPlacementUtil.*;

public final class TropicraftVegetationPlacements {
    public static final ResourceKey<PlacedFeature> RAINFOREST_VINES = createKey("rainforest_vines");

    public static final ResourceKey<PlacedFeature> SMALL_GOLDEN_LEATHER_FERN = createKey("small_golden_leather_fern");
    public static final ResourceKey<PlacedFeature> TALL_GOLDEN_LEATHER_FERN = createKey("tall_golden_leather_fern");
    public static final ResourceKey<PlacedFeature> HUGE_GOLDEN_LEATHER_FERN = createKey("huge_golden_leather_fern");

    public static final ResourceKey<PlacedFeature> OVERGROWN_SMALL_GOLDEN_LEATHER_FERN = createKey("overgrown_small_golden_leather_fern");
    public static final ResourceKey<PlacedFeature> OVERGROWN_TALL_GOLDEN_LEATHER_FERN = createKey("overgrown_tall_golden_leather_fern");
    public static final ResourceKey<PlacedFeature> OVERGROWN_HUGE_GOLDEN_LEATHER_FERN = createKey("overgrown_huge_golden_leather_fern");

    public static final ResourceKey<PlacedFeature> TREES_MANGROVE = createKey("trees_mangrove");
    public static final ResourceKey<PlacedFeature> TREES_MANGROVE_SPARSE = createKey("trees_mangrove_sparse");

    public static final ResourceKey<PlacedFeature> TREES_FRUIT = createKey("trees_fruit");
    public static final ResourceKey<PlacedFeature> TREES_PALM = createKey("trees_palm");
    public static final ResourceKey<PlacedFeature> TREES_PALM_OVERWORLD = createKey("trees_palm_overworld");
    public static final ResourceKey<PlacedFeature> TREES_RAINFOREST = createKey("trees_rainforest");

    public static final ResourceKey<PlacedFeature> TREES_PLEODENDRON = createKey("trees_pleodendron");
    public static final ResourceKey<PlacedFeature> TREES_PAPAYA = createKey("trees_papaya");

    public static final ResourceKey<PlacedFeature> BUSH_FLOWERING_COMMON = createKey("bush_flowering_common");
    public static final ResourceKey<PlacedFeature> BUSH_FLOWERING_RARE = createKey("bush_flowering_rare");

    public static final ResourceKey<PlacedFeature> TROPICS_GRASS = createKey("tropics_grass");

    public static final ResourceKey<PlacedFeature> BAMBOO = createKey("bamboo");

    public static final ResourceKey<PlacedFeature> TROPI_SEAGRASS = createKey("tropi_seagrass");

    public static final ResourceKey<PlacedFeature> PINEAPPLE = createKey("pineapple");
    public static final ResourceKey<PlacedFeature> PATCH_PINEAPPLE = createKey("patch_pineapple");

    public static final ResourceKey<PlacedFeature> FLOWERS_TROPICS = createKey("flowers_tropics");
    public static final ResourceKey<PlacedFeature> FLOWERS_RAINFOREST = createKey("flowers_rainforest");

    public static final ResourceKey<PlacedFeature> PATCH_IRIS = createKey("patch_iris");

    public static final ResourceKey<PlacedFeature> COFFEE_BUSH = createKey("coffee_bush");

    public static final ResourceKey<PlacedFeature> UNDERGROWTH = createKey("undergrowth");

    public static final ResourceKey<PlacedFeature> SINGLE_UNDERGROWTH = createKey("single_undergrowth");

    public static final ResourceKey<PlacedFeature> SEAGRASS = createKey("seagrass");

    public static final ResourceKey<PlacedFeature> UNDERGROUND_SEAGRASS_ON_STONE = createKey("underground_seagrass_on_stone");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_SEAGRASS_ON_DIRT = createKey("underground_seagrass_on_dirt");

    public static final ResourceKey<PlacedFeature> UNDERGROUND_SEA_PICKLES = createKey("underground_sea_pickles");

    public static final ResourceKey<PlacedFeature> MANGROVE_REEDS = createKey("mangrove_reeds");

    public static final ResourceKey<PlacedFeature> KELP = createKey("kelp");

    public static List<PlacementModifier> worldSurfaceSquaredWithChance(int onceEvery) {
        return List.of(RarityFilter.onAverageOnceEvery(onceEvery), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }

    private static List<PlacementModifier> seagrassPlacement(final Supplier<? extends Block> belowBlock) {
        final BlockPredicateFilter seagrassPredicate = BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), belowBlock.get()),
                BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.WATER),
                BlockPredicate.matchesBlocks(new BlockPos(0, 1, 0), Blocks.WATER)));

        return List.of(CarvingMaskPlacement.forStep(GenerationStep.Carving.LIQUID),
                RarityFilter.onAverageOnceEvery(10),
                seagrassPredicate,
                BiomeFilter.biome());
    }

    public static void bootstrap(final BootstapContext<PlacedFeature> context) {
        register(context, RAINFOREST_VINES, TropicraftVegetationFeatures.RAINFOREST_VINES, List.of(
                CountPlacement.of(50),
                InSquarePlacement.spread()
        ));

        register(context, SMALL_GOLDEN_LEATHER_FERN, TropicraftVegetationFeatures.SMALL_GOLDEN_LEATHER_FERN, List.of(
                RarityFilter.onAverageOnceEvery(4),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, TALL_GOLDEN_LEATHER_FERN, TropicraftVegetationFeatures.TALL_GOLDEN_LEATHER_FERN, List.of(
                RarityFilter.onAverageOnceEvery(8),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, HUGE_GOLDEN_LEATHER_FERN, TropicraftVegetationFeatures.HUGE_GOLDEN_LEATHER_FERN, List.of(
                RarityFilter.onAverageOnceEvery(12),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, OVERGROWN_SMALL_GOLDEN_LEATHER_FERN, TropicraftVegetationFeatures.SMALL_GOLDEN_LEATHER_FERN, List.of(
                RarityFilter.onAverageOnceEvery(2),
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, OVERGROWN_TALL_GOLDEN_LEATHER_FERN, TropicraftVegetationFeatures.TALL_GOLDEN_LEATHER_FERN, List.of(
                RarityFilter.onAverageOnceEvery(2),
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, OVERGROWN_HUGE_GOLDEN_LEATHER_FERN, TropicraftVegetationFeatures.HUGE_GOLDEN_LEATHER_FERN, List.of(RarityFilter.onAverageOnceEvery(90),
                CountPlacement.of(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, TREES_MANGROVE, TropicraftVegetationFeatures.TREES_MANGROVE, mangrovePlacement(7, 200.0, 1.5));
        register(context, TREES_MANGROVE_SPARSE, TropicraftVegetationFeatures.TREES_MANGROVE, mangrovePlacement(3, 200.0, 1.));

        register(context, TREES_FRUIT, TropicraftVegetationFeatures.TREES_FRUIT, sparseTreePlacement(0.1F));
        register(context, TREES_PALM, TropicraftVegetationFeatures.TREES_PALM, sparseTreePlacement(1.0f / 2.0f));
        register(context, TREES_PALM_OVERWORLD, TropicraftVegetationFeatures.TREES_PALM, sparseTreePlacement(1.0f / 10.0f));
        register(context, TREES_RAINFOREST, TropicraftVegetationFeatures.TREES_RAINFOREST, treePlacement(1, 1.0f / 2.0f, 1));

        register(context, TREES_PLEODENDRON, TropicraftVegetationFeatures.TREES_PLEODENDRON, treePlacement(0, 0.1f, 1));
        register(context, TREES_PAPAYA, TropicraftVegetationFeatures.TREES_PAPAYA, treePlacement(0, 0.2f, 1));

        register(context, BUSH_FLOWERING_COMMON, TropicraftVegetationFeatures.BUSH_FLOWERING, treePlacement(0, 1.0f / 4.0f, 1));
        register(context, BUSH_FLOWERING_RARE, TropicraftVegetationFeatures.BUSH_FLOWERING, treePlacement(0, 1.0f / 8.0f, 1));

        register(context, TROPICS_GRASS, TropicraftVegetationFeatures.PATCH_GRASS_TROPICS, worldSurfaceSquaredWithCount(10));

        register(context, BAMBOO, TropicraftVegetationFeatures.BAMBOO, List.of(
                NoiseBasedCountPlacement.of(50, 140.0D, 0.5D),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        ));

        register(context, TROPI_SEAGRASS, TropicraftVegetationFeatures.TROPI_SEAGRASS, List.of(
                NoiseBasedCountPlacement.of(1, 150.0, 0),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        ));

        register(context, PINEAPPLE, TropicraftVegetationFeatures.PINEAPPLE, List.of(
                RarityFilter.onAverageOnceEvery(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, PATCH_PINEAPPLE, TropicraftVegetationFeatures.PATCH_PINEAPPLE, List.of(
                RarityFilter.onAverageOnceEvery(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, FLOWERS_TROPICS, TropicraftVegetationFeatures.FLOWERS_TROPICS, List.of(RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, FLOWERS_RAINFOREST, TropicraftVegetationFeatures.FLOWERS_RAINFOREST, List.of(RarityFilter.onAverageOnceEvery(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, PATCH_IRIS, TropicraftVegetationFeatures.PATCH_IRIS, List.of(
                RarityFilter.onAverageOnceEvery(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));

        register(context, COFFEE_BUSH, TropicraftVegetationFeatures.COFFEE_BUSH, worldSurfaceSquaredWithChance(25));

        register(context, UNDERGROWTH, TropicraftVegetationFeatures.UNDERGROWTH, worldSurfaceSquaredWithChance(5));

        register(context, SINGLE_UNDERGROWTH, TropicraftVegetationFeatures.SINGLE_UNDERGROWTH, worldSurfaceSquaredWithCount(2));

        register(context, SEAGRASS, TropicraftVegetationFeatures.SEAGRASS, List.of(CountPlacement.of(48),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG)
        ));

        register(context, UNDERGROUND_SEAGRASS_ON_STONE, TropicraftVegetationFeatures.UNDERGROUND_SEAGRASS, seagrassPlacement(() -> Blocks.STONE));
        register(context, UNDERGROUND_SEAGRASS_ON_DIRT, TropicraftVegetationFeatures.UNDERGROUND_SEAGRASS, seagrassPlacement(() -> Blocks.DIRT));

        register(context, UNDERGROUND_SEA_PICKLES, TropicraftVegetationFeatures.UNDERGROUND_SEA_PICKLES, List.of(
                CarvingMaskPlacement.forStep(GenerationStep.Carving.LIQUID),
                RarityFilter.onAverageOnceEvery(10),
                BiomeFilter.biome()
        ));

        register(context, MANGROVE_REEDS, TropicraftVegetationFeatures.MANGROVE_REEDS, List.of(
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                BiomeFilter.biome()
        ));

        register(context, KELP, AquaticFeatures.KELP, List.of(
                NoiseBasedCountPlacement.of(75, 80.0D, 0.55D),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
        ));
    }

    private static ResourceKey<PlacedFeature> createKey(final String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Constants.MODID, name));
    }

    public static void addFruitTrees(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TREES_FRUIT);
    }

    public static void addFloweringBushes(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, BUSH_FLOWERING_COMMON);
    }

    public static void addRareFloweringBushes(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, BUSH_FLOWERING_RARE);
    }

    public static void addPalmTrees(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TREES_PALM);
    }

    public static void addRainforestTrees(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TREES_RAINFOREST);
    }

    public static void addRainforestPlants(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, VegetationPlacements.PATCH_MELON);
        addVegetalDecoration(generation, RAINFOREST_VINES);
        addVegetalDecoration(generation, COFFEE_BUSH);
        addVegetalDecoration(generation, SINGLE_UNDERGROWTH);
        addVegetalDecoration(generation, FLOWERS_RAINFOREST);
    }

    public static void addUndergrowth(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, UNDERGROWTH);
    }

    public static void addMangroveVegetation(BiomeGenerationSettings.Builder generation, boolean sparse) {
        addVegetalDecoration(generation, sparse ? TREES_MANGROVE_SPARSE : TREES_MANGROVE);
    }

    public static void addOvergrownGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, OVERGROWN_SMALL_GOLDEN_LEATHER_FERN);
        addVegetalDecoration(generation, OVERGROWN_TALL_GOLDEN_LEATHER_FERN);
        addVegetalDecoration(generation, OVERGROWN_HUGE_GOLDEN_LEATHER_FERN);
    }

    public static void addGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, SMALL_GOLDEN_LEATHER_FERN);
        addVegetalDecoration(generation, TALL_GOLDEN_LEATHER_FERN);
        addVegetalDecoration(generation, HUGE_GOLDEN_LEATHER_FERN);
    }

    public static void addPleodendron(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TREES_PLEODENDRON);
    }

    public static void addPapaya(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TREES_PAPAYA);
    }

    public static void addMangroveReeds(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, MANGROVE_REEDS);
    }

    public static void addTropicsGrass(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TROPICS_GRASS);
    }

    public static void addBamboo(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, BAMBOO);
    }

    public static void addPineapples(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, PATCH_PINEAPPLE);
    }

    public static void addTropicsFlowers(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, FLOWERS_TROPICS);
        addVegetalDecoration(generation, PATCH_IRIS);
    }

    public static void addUndergroundSeagrass(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, UNDERGROUND_SEAGRASS_ON_STONE);
        addVegetalDecoration(generation, UNDERGROUND_SEAGRASS_ON_DIRT);
    }

    public static void addSeagrass(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, TROPI_SEAGRASS);
        addVegetalDecoration(generation, SEAGRASS);
    }

    public static void addUndergroundPickles(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, UNDERGROUND_SEA_PICKLES);
    }

    public static void addKelp(BiomeGenerationSettings.Builder generation) {
        addVegetalDecoration(generation, KELP);
    }

    private static void addVegetalDecoration(BiomeGenerationSettings.Builder generation, ResourceKey<PlacedFeature> feature) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, feature);
    }

    private static List<PlacementModifier> mangrovePlacement(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
        return List.of(
                NoiseBasedCountPlacement.of(noiseToCountRatio, noiseFactor, noiseOffset),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome()
        );
    }
}
