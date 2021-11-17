package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public class TropicraftApplyOsaLayer implements CastleTransformer {
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
    public int apply(Context context, int north, int east, int south, int west, int center) {
        if (canOsaGenerate(center)) {
            // Rare chance of simply existing
            if (context.nextRandom(18) == 0) {
                return this.ids.osaRainforest;
            }

            // 5/6 of the time when bordering mangrove, generate osa rainforest
            if (context.nextRandom(6) > 0) {
                return isMangrove(north) || isMangrove(west) || isMangrove(south) || isMangrove(east) ? this.ids.osaRainforest : center;
            }
        }

        return center;
    }
}
