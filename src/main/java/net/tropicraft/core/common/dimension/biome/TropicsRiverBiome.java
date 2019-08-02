package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;

public class TropicsRiverBiome extends TropicraftBiome {
    protected TropicsRiverBiome() {
        super(new Biome.Builder()
            .surfaceBuilder(SurfaceBuilder.DEFAULT, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG)
            .precipitation(RainType.RAIN)
            .category(Category.RIVER)
            .depth(-0.7F)
            .scale(0.05F)
            .temperature(1.5F)
            .downfall(1.25F)
            .parent(null));

        //        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityPiranha.class, 20, 1, 12));
        //        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityRiverSardine.class, 20, 1, 15));
    }
}
