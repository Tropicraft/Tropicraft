package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public final class TropicraftRiverLayer implements CastleTransformer {
    private final TropicraftBiomeIds biomeIds;

    public TropicraftRiverLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(Context random, int north, int east, int south, int west, int center) {
        if (center != north || center != east || center != south || center != west) {
            return biomeIds.river;
        }

        return -1;
    }
}
