package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;

public class TropicsBiome extends TropicraftBiome {
    protected TropicsBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.BEACH)
                .depth(-1.5F)
                .scale(0.3F)
                .temperature(2.0F)
                .downfall(1.25F)
                .parent(null));

        DefaultBiomeFeatures.addCarvers(this);
    }
}
