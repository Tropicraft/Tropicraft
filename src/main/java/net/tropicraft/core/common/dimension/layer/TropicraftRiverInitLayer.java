package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public enum TropicraftRiverInitLayer implements IC0Transformer {
    INSTANCE {
        @Override
        public int apply(INoiseRandom iNoiseRandom, int center) {
            return TropicraftLayerUtil.isOcean(center) ? center : iNoiseRandom.random(4) + 1;
        }
    };
/*
    @Override
    public int apply(INoiseRandom iNoiseRandom, int center) {
        //return iNoiseRandom.random(4) + 1;
        return TropicraftLayerUtil.isOcean(center) ? center : iNoiseRandom.random(4) + 1;
    }*/
}
