package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;

import static net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil.isLand;

public enum TropicraftAddInlandLayer implements IBishopTransformer {
    INSTANCE(20, TropicraftLayerUtil.LAND_ID);

    private int chance;
    private int landID;

    TropicraftAddInlandLayer(int chance, int landID) {
        this.chance = chance;
        this.landID = landID;
    }

    @Override
    public int apply(INoiseRandom random, int ne, int se, int sw, int nw, int center) {
        if (isLand(nw) && isLand(sw) && isLand(ne) && isLand(se) && isLand(center) && random.random(chance) == 0) {
            return landID;
        }

        return center;
    }
}
