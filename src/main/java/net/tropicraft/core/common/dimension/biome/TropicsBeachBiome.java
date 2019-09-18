package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

public class TropicsBeachBiome extends TropicraftBiome {

    protected TropicsBeachBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.BEACH)
                .depth(-0.1F)
                .scale(0.1F)
                .temperature(1.5F)
                .downfall(1.25F)
                .parent(null));

        this.addStructure(TropicraftFeatures.VILLAGE, new NoFeatureConfig());

        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.NORMAL_PALM_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.2F, 1)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.CURVED_PALM_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.2F, 1)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.LARGE_PALM_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.2F, 1)));

        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(TropicraftFeatures.VILLAGE, new NoFeatureConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
    }
}
