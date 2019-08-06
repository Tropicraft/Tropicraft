package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class TropicsBiome extends TropicraftBiome {
    protected TropicsBiome() {
        super(new Biome.Builder()
                // TODO NO SAND - purified sand
                .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_SAND_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.PLAINS)
                .depth(0.15F)
                .scale(0.15F)
                .temperature(2.0F)
                .downfall(1.5F)
                .parent(null));

        DefaultBiomeFeatures.addCarvers(this);
    }
}
