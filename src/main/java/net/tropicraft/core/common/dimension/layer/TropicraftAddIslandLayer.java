package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;

import static net.tropicraft.core.common.dimension.layer.TropicraftLayerUtil.isLand;

public enum TropicraftAddIslandLayer implements IBishopTransformer {
    BASIC_2(2, TropicraftLayerUtil.LAND_ID),
    BASIC_3(3, TropicraftLayerUtil.LAND_ID),
    RAINFOREST_13(13, TropicraftLayerUtil.ISLAND_MOUNTAINS_ID)
    ;

    private int chance;
    private int landID;

    TropicraftAddIslandLayer(int chance, int landID) {
        this.chance = chance;
        this.landID = landID;
    }

    @Override
    public int apply(INoiseRandom random, int ne, int se, int sw, int nw, int center) {
        if (!isLand(nw) && !isLand(sw) && !isLand(ne) && !isLand(se) && !isLand(center) && random.random(chance) == 0) {
            return landID;
        }

        return center;
    }
}
