package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

public abstract class TropicraftBiome extends Biome {
    public static final int TROPICS_WATER_COLOR = 0x4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;

    protected TropicraftBiome(final Builder builder) {
        super(builder
            .waterColor(TROPICS_WATER_COLOR)
            .waterFogColor(TROPICS_WATER_FOG_COLOR));
        
        DefaultBiomeFeatures.addStructures(this);
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(TropicraftFeatures.VILLAGE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
    }
}
