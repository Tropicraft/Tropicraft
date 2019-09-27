package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum TropicraftRiverLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom random, int north, int east, int south, int west, int center) {
        if (center != north || center != east || center != south || center != west) {
            return TropicraftLayerUtil.RIVER_ID.getAsInt();
        }

        return -1;
    }
}
