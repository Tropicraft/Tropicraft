package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public final class TropicraftApplyOsaLayer implements CastleTransformer {
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
    public int apply(Context pContext, int pNorth, int pEast, int pSouth, int pWest, int pCenter) {
        if (canOsaGenerate(pCenter)) {
            // Rare chance of simply existing
            if (pContext.nextRandom(18) == 0) {
                return this.ids.osaRainforest;
            }

            // 5/6 of the time when bordering mangrove, generate osa rainforest
            if (pContext.nextRandom(6) > 0) {
                return isMangrove(pNorth) || isMangrove(pWest) || isMangrove(pSouth) || isMangrove(pEast) ? this.ids.osaRainforest : pCenter;
            }
        }

        return pCenter;
    }
}