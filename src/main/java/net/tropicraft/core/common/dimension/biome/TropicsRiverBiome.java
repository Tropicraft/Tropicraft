package net.tropicraft.core.common.dimension.biome;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;
import net.tropicraft.core.common.entity.TropicraftEntities;

import static net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs.PURIFIED_SAND_CONFIG;

public class TropicsRiverBiome extends TropicraftBiome {
    protected TropicsRiverBiome() {
        super(new Biome.Builder()
            .surfaceBuilder(TropicraftSurfaceBuilders._UNDERWATER, PURIFIED_SAND_CONFIG.get())
            .precipitation(RainType.RAIN)
            .category(Category.RIVER)
            .depth(-0.7F)
            .scale(0.05F)
            .temperature(1.5F)
            .downfall(1.25F)
            .parent(null)
        );
    }
    
    @Override
    public void addFeatures() {
        super.addFeatures();
        DefaultTropicsFeatures.addCarvers(this);
        DefaultTropicsFeatures.addTropicsFlowers(this);

        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.PIRANHA.get(), 20, 1, 12));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.RIVER_SARDINE.get(), 20, 1, 8));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(EntityType.SQUID, 8, 1, 4));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(EntityType.COD, 4, 1, 5));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(EntityType.SALMON, 4, 1, 5));
    }
}
