package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;

import static net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil.isOcean;
import static net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil.isRiver;

public enum TropicraftBeachLayer implements IBishopTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, int ne, int se, int sw, int nw, int center) {
        if (isOcean(center) && (!isOcean(ne) || !isOcean(se) || !isOcean(sw) || !isOcean(nw))) {
            return TropicraftLayerUtil.BEACH_ID;
        }

        if (isRiver(center) && (!isRiver(ne) || !isRiver(se) || !isRiver(sw) || !isRiver(nw))) {
            return TropicraftLayerUtil.BEACH_ID;
        }

        return center;
    }
}
