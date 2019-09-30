package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;

public class TropicsKelpForestBiome extends TropicraftBiome {

    protected TropicsKelpForestBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG.get())
                .precipitation(RainType.RAIN)
                .category(Category.OCEAN)
                .depth(-1.5F)
                .scale(0.3F)
                .temperature(2.0F)
                .downfall(1.25F)
                .parent(null));
    }
    
    @Override
    public void addFeatures() {
        super.addFeatures();
        
        DefaultTropicsFeatures.addUnderwaterCarvers(this);
        
        // KELP!
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.KELP, IFeatureConfig.NO_FEATURE_CONFIG, Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED, new TopSolidWithNoiseConfig(120, 80, 1, Heightmap.Type.OCEAN_FLOOR_WG)));
        
        DefaultTropicsFeatures.addUnderwaterCarvers(this);
        DefaultTropicsFeatures.addUndergroundSeagrass(this);
        DefaultTropicsFeatures.addUndergroundPickles(this);
    }
}
