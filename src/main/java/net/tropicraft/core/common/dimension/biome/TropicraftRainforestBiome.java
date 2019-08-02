package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class TropicraftRainforestBiome extends TropicraftBiome {

    protected TropicraftRainforestBiome(final float depth, final float scale) {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.NONE)
                .depth(depth)
                .scale(scale)
                .temperature(1.5F)
                .downfall(2F)
                .parent(null));
    }
}
