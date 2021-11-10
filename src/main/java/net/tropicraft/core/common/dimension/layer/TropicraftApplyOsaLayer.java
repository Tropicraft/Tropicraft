package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public final class TropicraftApplyOsaLayer implements ICastleTransformer {
    private final TropicraftBiomeIds ids;

    public TropicraftApplyOsaLayer(TropicraftBiomeIds ids) {
        this.ids = ids;
    }

    private boolean canOsaGenerate(int id) {
        return id == this.ids.rainforestPlains || id == this.ids.rainforestHills || id == this.ids.rainforestMountains || id == this.ids.bambooRainforest;
    }

    private boolean isMangrove(int id) {
        return id == this.ids.mangroves;
    }

    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center) {
        if (canOsaGenerate(center)) {
            // Rare chance of simply existing
            if (context.random(18) == 0) {
                return this.ids.osaRainforest;
            }

            // 5/6 of the time when bordering mangrove, generate osa rainforest
            if (context.random(6) > 0) {
                return isMangrove(north) || isMangrove(west) || isMangrove(south) || isMangrove(east) ? this.ids.osaRainforest : center;
            }
        }

        return center;
    }
}