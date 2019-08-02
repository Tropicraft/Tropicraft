package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;

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
    }
}
