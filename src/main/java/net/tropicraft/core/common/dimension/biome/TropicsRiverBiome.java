package net.tropicraft.core.common.dimension.biome;

import static net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs.PURIFIED_SAND_CONFIG;

import net.minecraft.world.biome.Biome;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;

public class TropicsRiverBiome extends TropicraftBiome {
    protected TropicsRiverBiome() {
        super(new Biome.Builder()
            .surfaceBuilder(TropicraftSurfaceBuilders._RIVER, PURIFIED_SAND_CONFIG.get())
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
    
    @Override
    public void addFeatures() {
        super.addFeatures();
        DefaultTropicsFeatures.addCarvers(this);
        DefaultTropicsFeatures.addTropicsFlowers(this);
    }
}
