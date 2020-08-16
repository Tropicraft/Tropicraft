package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;

public class TropicsBeachBiome extends TropicraftBiome {

    protected TropicsBeachBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(TropicraftSurfaceBuilders._UNDERWATER, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG.get())
                .precipitation(RainType.RAIN)
                .category(Category.BEACH)
                .depth(-0.1F)
                .scale(0.1F)
                .temperature(1.5F)
                .downfall(1.25F)
                .parent(null));
    }

    @Override
    public void addFeatures() {
        super.addFeatures();
        addStructure(TropicraftFeatures.VILLAGE.get().withConfiguration(new NoFeatureConfig()));

        DefaultTropicsFeatures.addUnderwaterCarvers(this);
        
        DefaultTropicsFeatures.addPalmTrees(this);
        DefaultTropicsFeatures.addTropicsFlowers(this);
    }
}
