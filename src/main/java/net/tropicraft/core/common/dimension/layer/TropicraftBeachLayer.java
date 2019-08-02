package net.tropicraft.core.common.dimension.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum TropicraftBeachLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, int north, int east, int south, int west, int center) {
        final Biome biome = Registry.BIOME.getByValue(center);

        //TODO very unsure of this
        if (!isOcean(center) && center != TropicraftLayerUtil.RIVER_ID) {
            if (isOcean(north) || isOcean(east) || isOcean(south) || isOcean(west)) {
                return TropicraftLayerUtil.BEACH_ID;
            }
        }

        return 0;
    }

    private boolean isOcean(final int biome) {
        return biome == TropicraftLayerUtil.OCEAN_ID;
    }
}
