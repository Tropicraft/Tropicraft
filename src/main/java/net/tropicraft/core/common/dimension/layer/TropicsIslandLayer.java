package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public final class TropicsIslandLayer implements IAreaTransformer0 {
    private final TropicraftBiomeIds biomeIds;

    public TropicsIslandLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int applyPixel(INoiseRandom random, int x, int y) {
        // if (0, 0) is located here, place an island
        if (x == 0 && y == 0) {
            return this.biomeIds.land;
        }

        return random.nextRandom(3) == 0 ? this.biomeIds.land : this.biomeIds.ocean;
    }
}
