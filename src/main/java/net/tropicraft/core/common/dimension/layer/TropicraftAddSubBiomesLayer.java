package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public enum TropicraftAddSubBiomesLayer implements IC0Transformer {
    // TODO add kelp forest here to sub biomes list
    RAINFOREST(TropicraftLayerUtil.RAINFOREST_PLAINS_ID, TropicraftLayerUtil.RAINFOREST_IDS)
    ;

    final LazyInt baseID;
    final LazyInt[] subBiomeIDs;

    TropicraftAddSubBiomesLayer(final LazyInt baseID, final LazyInt[] subBiomeIDs) {
        this.baseID = baseID;
        this.subBiomeIDs = subBiomeIDs;
    }

    @Override
    public int apply(INoiseRandom random, int center) {
        if (center == baseID.getAsInt()) {
            return subBiomeIDs[random.random(subBiomeIDs.length)].getAsInt();
        } else {
            return center;
        }
    }
}
