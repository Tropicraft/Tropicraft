package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public final class TropicraftRiverInitLayer implements IC0Transformer {
    public final TropicraftBiomeIds biomeIds;

    public TropicraftRiverInitLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(INoiseRandom random, int center) {
        return biomeIds.isOcean(center) ? center : random.nextRandom(4) + 1;
    }
/*
    @Override
    public int apply(INoiseRandom iNoiseRandom, int center) {
        //return iNoiseRandom.random(4) + 1;
        return TropicraftLayerUtil.isOcean(center) ? center : iNoiseRandom.random(4) + 1;
    }*/
}
