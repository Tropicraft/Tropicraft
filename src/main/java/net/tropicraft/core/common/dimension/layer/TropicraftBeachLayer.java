package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;

public final class TropicraftBeachLayer implements BishopTransformer {
    private final TropicraftBiomeIds biomeIds;

    public TropicraftBeachLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(Context random, int ne, int se, int sw, int nw, int center) {
        TropicraftBiomeIds ids = this.biomeIds;

        if (ids.isOcean(center) && (!ids.isOcean(ne) || !ids.isOcean(se) || !ids.isOcean(sw) || !ids.isOcean(nw))) {
            return ids.beach;
        }

        if (ids.isRiver(center) && (!ids.isRiver(ne) || !ids.isRiver(se) || !ids.isRiver(sw) || !ids.isRiver(nw))) {
            return ids.beach;
        }

        return center;
    }
}
