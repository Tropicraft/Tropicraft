package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

import static net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil.isLand;

public enum TropicraftBiomesLayer implements IC0Transformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, int center) {
        if (isLand(center)) {
            return TropicraftLayerUtil.TROPICS_LAND_IDS[iNoiseRandom.random(TropicraftLayerUtil.TROPICS_LAND_IDS.length)].getAsInt();
        }

        // TODO add kelp forest and other ocean biomes random check here
        return center;
    }
}
