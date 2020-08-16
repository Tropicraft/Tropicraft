package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;

public class TropicsLakeBiome extends TropicraftBiome {
    protected TropicsLakeBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(TropicraftSurfaceBuilders._UNDERWATER, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG.get())
                .precipitation(RainType.RAIN)
                .category(Category.NONE)
                .depth(-0.6F)
                .scale(0.1F)
                .temperature(1.5F)
                .downfall(1.5F)
                .parent(null));
    }
    
    @Override
    public void addFeatures() {
        super.addFeatures();
        DefaultTropicsFeatures.addCarvers(this);
    }
}
