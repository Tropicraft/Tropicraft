package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;

public final class TropicraftBiomesLayer implements C0Transformer {
    private final TropicraftBiomeIds biomeIds;
    private final int[] landIds;

    public TropicraftBiomesLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
        this.landIds = new int[] { biomeIds.land, biomeIds.rainforestPlains };
    }

    @Override
    public int apply(Context iNoiseRandom, int center) {
        if (biomeIds.isLand(center)) {
            return landIds[iNoiseRandom.nextRandom(landIds.length)];
        }

        return center;
    }
}
