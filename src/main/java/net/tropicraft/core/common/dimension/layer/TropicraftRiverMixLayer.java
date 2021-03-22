package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

public final class TropicraftRiverMixLayer implements IAreaTransformer2, IDimOffset0Transformer {
    private final TropicraftBiomeIds biomeIds;

    public TropicraftRiverMixLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int apply(INoiseRandom random, IArea parent1, IArea parent2, int x, int y) {
        final int biome = parent1.getValue(getOffsetX(x), getOffsetZ(y));
        final int river = parent2.getValue(getOffsetX(x), getOffsetZ(y));

        if (!biomeIds.isOcean(biome)) {
            if (biomeIds.isRiver(river)) {
                return river;
            }
        }

        return biome;
    }
}
