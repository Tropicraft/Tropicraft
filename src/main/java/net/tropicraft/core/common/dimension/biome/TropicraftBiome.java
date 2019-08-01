package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;

public abstract class TropicraftBiome extends Biome {
    public static final int TROPICS_WATER_COLOR = 0x43d5ee;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;

    protected TropicraftBiome(final Builder builder) {
        super(builder
            .waterColor(TROPICS_WATER_COLOR)
            .waterFogColor(TROPICS_WATER_FOG_COLOR));
    }
}
