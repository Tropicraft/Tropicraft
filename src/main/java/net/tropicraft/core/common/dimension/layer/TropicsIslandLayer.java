package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public enum TropicsIslandLayer implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, int x, int y) {
        // if (0, 0) is located here, place an island
        if (x == 0 && y == 0) {
            return TropicraftLayerUtil.LAND_ID.getAsInt();
        }

        return iNoiseRandom.random(3) == 0 ? TropicraftLayerUtil.LAND_ID.getAsInt() : TropicraftLayerUtil.OCEAN_ID.getAsInt();
    }
}
