package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.AquaticFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.worldSurfaceSquaredWithCount;

public final class TropicraftVegetationPlacements {
    public static final TropicraftFeatures.Register REGISTER = TropicraftFeatures.Register.create();

    public static final RegistryObject<PlacedFeature> VINES_RAINFOREST = REGISTER.placed("rainforest_vines", TropicraftVegetationFeatures.RAINFOREST_VINES, () -> List.of(
            CountPlacement.of(50),
            InSquarePlacement.spread()
    ));

    public static final RegistryObject<PlacedFeature> SMALL_GOLDEN_LEATHER_FERN = REGISTER.placed("small_golden_leather_fern", TropicraftVegetationFeatures.SMALL_GOLDEN_LEATHER_FERN, () -> List.of(
            RarityFilter.onAverageOnceEvery(4),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> TALL_GOLDEN_LEATHER_FERN = REGISTER.placed("tall_golden_leather_fern", TropicraftVegetationFeatures.TALL_GOLDEN_LEATHER_FERN, () -> List.of(
            RarityFilter.onAverageOnceEvery(8),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> HUGE_GOLDEN_LEATHER_FERN = REGISTER.placed("huge_golden_leather_fern", TropicraftVegetationFeatures.HUGE_GOLDEN_LEATHER_FERN, () -> List.of(
            RarityFilter.onAverageOnceEvery(12),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> OVERGROWN_SMALL_GOLDEN_LEATHER_FERN = REGISTER.placed("overgrown_small_golden_leather_fern", TropicraftVegetationFeatures.SMALL_GOLDEN_LEATHER_FERN, () -> List.of(
            RarityFilter.onAverageOnceEvery(2),
            CountPlacement.of(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> OVERGROWN_TALL_GOLDEN_LEATHER_FERN = REGISTER.placed("overgrown_tall_golden_leather_fern", TropicraftVegetationFeatures.TALL_GOLDEN_LEATHER_FERN, () -> List.of(
            RarityFilter.onAverageOnceEvery(2),
            CountPlacement.of(1),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> OVERGROWN_HUGE_GOLDEN_LEATHER_FERN = REGISTER.placed("overgrown_huge_golden_leather_fern", TropicraftVegetationFeatures.HUGE_GOLDEN_LEATHER_FERN, () -> List.of(RarityFilter.onAverageOnceEvery(90),
            CountPlacement.of(6),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> TREES_MANGROVE = REGISTER.placed("trees_mangrove", TropicraftVegetationFeatures.TREES_MANGROVE, () -> mangrovePlacement(7, 200.0, 1.5));
    public static final RegistryObject<PlacedFeature> TREES_MANGROVE_SPARSE = REGISTER.placed("trees_mangrove_sparse", TropicraftVegetationFeatures.TREES_MANGROVE, () -> mangrovePlacement(3, 200.0, 1.));

    public static final RegistryObject<PlacedFeature> TREES_FRUIT = REGISTER.placed("trees_fruit", TropicraftVegetationFeatures.TREES_FRUIT, () -> REGISTER.sparseTreePlacement(0.1F));
    public static final RegistryObject<PlacedFeature> TREES_PALM = REGISTER.placed("trees_palm", TropicraftVegetationFeatures.TREES_PALM, () -> REGISTER.sparseTreePlacement(1.0f / 2.0f));
    public static final RegistryObject<PlacedFeature> TREES_PALM_OVERWORLD = REGISTER.placed("trees_palm_overworld", TropicraftVegetationFeatures.TREES_PALM, () -> REGISTER.sparseTreePlacement(1.0f / 10.0f));
    public static final RegistryObject<PlacedFeature> TREES_RAINFOREST = REGISTER.placed("trees_rainforest", TropicraftVegetationFeatures.TREES_RAINFOREST, () -> REGISTER.treePlacement(1, 1.0f / 2.0f, 1));

    public static final RegistryObject<PlacedFeature> TREES_PLEODENDRON = REGISTER.placed("trees_pleodendron", TropicraftVegetationFeatures.TREES_PLEODENDRON, () -> REGISTER.treePlacement(0, 0.1f, 1));
    public static final RegistryObject<PlacedFeature> TREES_PAPAYA = REGISTER.placed("trees_papaya", TropicraftVegetationFeatures.TREES_PAPAYA, () -> REGISTER.treePlacement(0, 0.2f, 1));

    public static final RegistryObject<PlacedFeature> BUSH_FLOWERING_COMMON = REGISTER.placed("bush_flowering_common", TropicraftVegetationFeatures.BUSH_FLOWERING, () -> REGISTER.treePlacement(0, 1.0f / 4.0f, 1));
    public static final RegistryObject<PlacedFeature> BUSH_FLOWERING_RARE = REGISTER.placed("bush_flowering_rare", TropicraftVegetationFeatures.BUSH_FLOWERING, () -> REGISTER.treePlacement(0, 1.0f / 8.0f, 1));

    public static final RegistryObject<PlacedFeature> PATCH_GRASS_TROPICS = REGISTER.placed("tropics_grass", TropicraftVegetationFeatures.PATCH_GRASS_TROPICS, () -> worldSurfaceSquaredWithCount(10));

    public static final RegistryObject<PlacedFeature> BAMBOO = REGISTER.placed("bamboo", TropicraftVegetationFeatures.BAMBOO, () -> List.of(
            NoiseBasedCountPlacement.of(50, 140.0D, 0.5D),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> TROPI_SEAGRASS = REGISTER.placed("tropi_seagrass", TropicraftVegetationFeatures.TROPI_SEAGRASS, () -> List.of(
            NoiseBasedCountPlacement.of(1, 150.0, 0),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> PINEAPPLE = REGISTER.placed("pineapple", TropicraftVegetationFeatures.PINEAPPLE, () -> List.of(
            RarityFilter.onAverageOnceEvery(6),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> PATCH_PINEAPPLE = REGISTER.placed("patch_pineapple", TropicraftVegetationFeatures.PATCH_PINEAPPLE, () -> List.of(
            RarityFilter.onAverageOnceEvery(6),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> FLOWERS_TROPICS = REGISTER.placed("flowers_tropics", TropicraftVegetationFeatures.FLOWERS_TROPICS, () -> List.of(RarityFilter.onAverageOnceEvery(2),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> FLOWERS_RAINFOREST = REGISTER.placed("flowers_rainforest", TropicraftVegetationFeatures.FLOWERS_RAINFOREST, () -> List.of(RarityFilter.onAverageOnceEvery(6),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> PATCH_IRIS = REGISTER.placed("patch_iris", TropicraftVegetationFeatures.PATCH_IRIS, () -> List.of(
            RarityFilter.onAverageOnceEvery(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> COFFEE_BUSH = REGISTER.placed("coffee_bush", TropicraftVegetationFeatures.COFFEE_BUSH, () -> worldSurfaceSquaredWithChance(25));

    public static final RegistryObject<PlacedFeature> UNDERGROWTH = REGISTER.placed("undergrowth", TropicraftVegetationFeatures.UNDERGROWTH, () -> worldSurfaceSquaredWithChance(5));

    public static final RegistryObject<PlacedFeature> SINGLE_UNDERGROWTH = REGISTER.placed("single_undergrowth", TropicraftVegetationFeatures.SINGLE_UNDERGROWTH, () -> worldSurfaceSquaredWithCount(2));

    public static final RegistryObject<PlacedFeature> SEAGRASS = REGISTER.placed("seagrass", TropicraftVegetationFeatures.SEAGRASS, () -> List.of(CountPlacement.of(48),
            InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG)
    ));

    public static final RegistryObject<PlacedFeature> UNDERGROUND_SEAGRASS_ON_STONE = REGISTER.placed("underground_seagrass_on_stone", TropicraftVegetationFeatures.UNDERGROUND_SEAGRASS, () -> seagrassPlacement(() -> Blocks.STONE));
    public static final RegistryObject<PlacedFeature> UNDERGROUND_SEAGRASS_ON_DIRT = REGISTER.placed("underground_seagrass_on_dirt", TropicraftVegetationFeatures.UNDERGROUND_SEAGRASS, () -> seagrassPlacement(() -> Blocks.DIRT));

    public static final RegistryObject<PlacedFeature> UNDERGROUND_SEA_PICKLES = REGISTER.placed("underground_sea_pickles", TropicraftVegetationFeatures.UNDERGROUND_SEA_PICKLES, () -> List.of(
            CarvingMaskPlacement.forStep(GenerationStep.Carving.LIQUID),
            RarityFilter.onAverageOnceEvery(10),
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> MANGROVE_REEDS = REGISTER.placed("mangrove_reeds", TropicraftVegetationFeatures.MANGROVE_REEDS, () -> List.of(
            CountPlacement.of(2),
            InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> KELP = REGISTER.placed("kelp", AquaticFeatures.KELP, () -> List.of(
            NoiseBasedCountPlacement.of(75, 80.0D, 0.55D),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BiomeFilter.biome()
    ));

    public static List<PlacementModifier> worldSurfaceSquaredWithChance(int onceEvery) {
        return List.of(RarityFilter.onAverageOnceEvery(onceEvery), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }

    private static List<PlacementModifier> seagrassPlacement(final Supplier<? extends Block> belowBlock) {
        final BlockPredicateFilter seagrassPredicate = BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesBlock(belowBlock.get(), new BlockPos(0, -1, 0)),
                BlockPredicate.matchesBlock(Blocks.WATER, BlockPos.ZERO),
                BlockPredicate.matchesBlock(Blocks.WATER, new BlockPos(0, 1, 0))));

        return List.of(CarvingMaskPlacement.forStep(GenerationStep.Carving.LIQUID),
                RarityFilter.onAverageOnceEvery(10),
                seagrassPredicate,
                BiomeFilter.biome());
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
        addVegetalDecoration(generation, VINES_RAINFOREST);
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
        addVegetalDecoration(generation, PATCH_GRASS_TROPICS);
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

    private static void addVegetalDecoration(BiomeGenerationSettings.Builder generation, RegistryObject<PlacedFeature> feature) {
        addVegetalDecoration(generation, TropicraftFeatures.holderOf(feature));
    }

    private static void addVegetalDecoration(BiomeGenerationSettings.Builder generation, Holder<PlacedFeature> feature) {
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
