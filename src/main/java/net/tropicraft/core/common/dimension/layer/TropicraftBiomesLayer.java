package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public final class TropicraftBiomesLayer implements IC0Transformer {
    private final TropicraftBiomeIds biomeIds;
    private final int[] landIds;

    public TropicraftBiomesLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
        this.landIds = new int[] { biomeIds.land, biomeIds.rainforestPlains };
    }

    @Override
    public int apply(INoiseRandom iNoiseRandom, int center) {
        if (biomeIds.isLand(center)) {
            return landIds[iNoiseRandom.random(landIds.length)];
        }

        return center;
    }
}
