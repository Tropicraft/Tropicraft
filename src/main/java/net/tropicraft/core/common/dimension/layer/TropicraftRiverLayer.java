package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum TropicraftRiverLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom random, int north, int east, int south, int west, int center) {
        final int middle = riverFilter(center);
        if (middle == riverFilter(west)
                && middle == riverFilter(north)
                && middle == riverFilter(east)
                && middle == riverFilter(south)
                ) {
            return -1;
        }
        return TropicraftLayerUtil.RIVER_ID;
    }

    private static int riverFilter(final int value) {
        if (value >= 2) {
            return 2 + (value & 1);
        }
        return value;
    }
}
