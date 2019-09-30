package net.tropicraft.core.common.dimension.biome;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockWithContextConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.CaveEdgeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

public class DefaultTropicsFeatures {
    
    public static void addCarvers(Biome biome) {
        biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(TropicraftCarvers.CAVE.get(), new ProbabilityConfig(0.25F)));
        biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(TropicraftCarvers.CANYON.get(), new ProbabilityConfig(0.02F)));
    }

    public static void addUnderwaterCarvers(Biome biome) {
        biome.addCarver(GenerationStage.Carving.LIQUID, Biome.createCarver(TropicraftCarvers.UNDERWATER_CANYON.get(), new ProbabilityConfig(0.02F)));
        biome.addCarver(GenerationStage.Carving.LIQUID, Biome.createCarver(TropicraftCarvers.UNDERWATER_CAVE.get(), new ProbabilityConfig(0.15F)));
    }
    
    public static void addUndergroundSeagrass(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.SIMPLE_BLOCK, new BlockWithContextConfig(Blocks.SEAGRASS.getDefaultState(), new BlockState[]{Blocks.STONE.getDefaultState()}, new BlockState[]{Blocks.WATER.getDefaultState()}, new BlockState[]{Blocks.WATER.getDefaultState()}), Placement.CARVING_MASK, new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.1F)));
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.SIMPLE_BLOCK, new BlockWithContextConfig(Blocks.SEAGRASS.getDefaultState(), new BlockState[]{Blocks.DIRT.getDefaultState()}, new BlockState[]{Blocks.WATER.getDefaultState()}, new BlockState[]{Blocks.WATER.getDefaultState()}), Placement.CARVING_MASK, new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.5F)));
    }
    
    public static void addUndergroundPickles(Biome biome) {
        // TODO maybe combine these into a single feature type that chooses pickle count randomly?
        addPickleFeature(biome, 1, Blocks.STONE.getDefaultState(), 0.025F);
        addPickleFeature(biome, 2, Blocks.STONE.getDefaultState(), 0.01F);
        addPickleFeature(biome, 3, Blocks.STONE.getDefaultState(), 0.005F);
        addPickleFeature(biome, 4, Blocks.STONE.getDefaultState(), 0.001F);
        addPickleFeature(biome, 1, Blocks.DIRT.getDefaultState(), 0.05F);
        addPickleFeature(biome, 2, Blocks.DIRT.getDefaultState(), 0.04F);
        addPickleFeature(biome, 3, Blocks.DIRT.getDefaultState(), 0.02F);
        addPickleFeature(biome, 4, Blocks.DIRT.getDefaultState(), 0.01F);
    }

    private static void addPickleFeature(Biome biome, int pickles, BlockState placeOn, float chance) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.SIMPLE_BLOCK, new BlockWithContextConfig(Blocks.SEA_PICKLE.getDefaultState().with(SeaPickleBlock.PICKLES, pickles), new BlockState[]{placeOn}, new BlockState[]{Blocks.WATER.getDefaultState()}, new BlockState[]{Blocks.WATER.getDefaultState()}), Placement.CARVING_MASK, new CaveEdgeConfig(GenerationStage.Carving.LIQUID, chance)));
    }

    public static void addRainforestPlants(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.MELON, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.VINES.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHT_64, new FrequencyConfig(50)));
    }
}
