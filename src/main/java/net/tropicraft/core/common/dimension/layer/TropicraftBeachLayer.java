package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

import static net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil.isOcean;

public enum TropicraftBeachLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, int north, int east, int south, int west, int center) {
        //TODO very unsure of this
//        if (!isOcean(center) && center != TropicraftLayerUtil.RIVER_ID) {
//            if (isOcean(north) || isOcean(east) || isOcean(south) || isOcean(west)) {
//
//            }
//        }
//
        if (isOcean(center) && (!isOcean(north) || !isOcean(east) || !isOcean(south) || !isOcean(west))) {
            return TropicraftLayerUtil.BEACH_ID;
        }

        return center;
    }
}
