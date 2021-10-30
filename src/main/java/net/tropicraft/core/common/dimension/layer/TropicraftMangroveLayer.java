package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;

public final class TropicraftMangroveLayer implements BishopTransformer {
    private final TropicraftBiomeIds biomeIds;
    private final int chance;

    public TropicraftMangroveLayer(TropicraftBiomeIds biomeIds, int chance) {
        this.biomeIds = biomeIds;
        this.chance = chance;
    }

    @Override
    public int apply(Context random, int ne, int se, int sw, int nw, int center) {
        TropicraftBiomeIds ids = this.biomeIds;
        if (!ids.isOcean(center) && (ids.isOcean(ne) || ids.isOcean(se) || ids.isOcean(sw) || ids.isOcean(nw))) {
            if (random.nextRandom(this.chance) == 0) {
                return ids.mangroves;
            }
        }

        return center;
    }
}
