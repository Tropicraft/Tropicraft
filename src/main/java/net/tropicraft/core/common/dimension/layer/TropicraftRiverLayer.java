package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public final class TropicraftRiverLayer implements ICastleTransformer {
    private final TropicraftBiomeIds biomeIds;

    public TropicraftRiverLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(INoiseRandom random, int north, int east, int south, int west, int center) {
        if (center != north || center != east || center != south || center != west) {
            return biomeIds.river;
        }

        return -1;
    }
}
