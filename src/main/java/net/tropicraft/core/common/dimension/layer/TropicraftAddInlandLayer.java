package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;

public final class TropicraftAddInlandLayer implements IBishopTransformer {
    private final int chance;
    private final TropicraftBiomeIds biomeIds;

    TropicraftAddInlandLayer(int chance, TropicraftBiomeIds biomeIds) {
        this.chance = chance;
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(INoiseRandom random, int ne, int se, int sw, int nw, int center) {
        if (biomeIds.isLand(nw) && biomeIds.isLand(sw) && biomeIds.isLand(ne) && biomeIds.isLand(se) && biomeIds.isLand(center) && random.nextRandom(chance) == 0) {
            return biomeIds.land;
        }

        return center;
    }
}
