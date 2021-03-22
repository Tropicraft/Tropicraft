package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;

public final class TropicraftBeachLayer implements IBishopTransformer {
    private final TropicraftBiomeIds biomeIds;

    public TropicraftBeachLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(INoiseRandom iNoiseRandom, int ne, int se, int sw, int nw, int center) {
        if (biomeIds.isOcean(center) && (!biomeIds.isOcean(ne) || !biomeIds.isOcean(se) || !biomeIds.isOcean(sw) || !biomeIds.isOcean(nw))) {
            return biomeIds.beach;
        }

        if (biomeIds.isRiver(center) && (!biomeIds.isRiver(ne) || !biomeIds.isRiver(se) || !biomeIds.isRiver(sw) || !biomeIds.isRiver(nw))) {
            return biomeIds.beach;
        }

        return center;
    }
}
