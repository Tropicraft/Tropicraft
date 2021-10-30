package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;

public final class TropicraftAddInlandLayer implements BishopTransformer {
    private final int chance;
    private final TropicraftBiomeIds biomeIds;

    TropicraftAddInlandLayer(int chance, TropicraftBiomeIds biomeIds) {
        this.chance = chance;
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(Context random, int ne, int se, int sw, int nw, int center) {
        if (biomeIds.isLand(nw) && biomeIds.isLand(sw) && biomeIds.isLand(ne) && biomeIds.isLand(se) && biomeIds.isLand(center) && random.nextRandom(chance) == 0) {
            return biomeIds.land;
        }

        return center;
    }
}
