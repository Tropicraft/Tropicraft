package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public final class TropicraftLagoonifyLayer implements ICastleTransformer {
    private final TropicraftBiomeIds ids;

    public TropicraftLagoonifyLayer(TropicraftBiomeIds ids) {
        this.ids = ids;
    }

    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center) {
        if (ids.isOcean(center)) {
            int rarity = 6;

            if (ids.isLand(north)) {
                rarity--;
            }

            if (ids.isLand(west)) {
                rarity--;
            }

            if (ids.isLand(south)) {
                rarity--;
            }

            if (ids.isLand(east)) {
                rarity--;
            }

            if (rarity < 6 && context.random(rarity) == 0) {
                return ids.lagoon;
            }
        }

        return center;
    }
}
