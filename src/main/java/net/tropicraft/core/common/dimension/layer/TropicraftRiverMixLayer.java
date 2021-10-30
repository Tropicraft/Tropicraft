package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;

public final class TropicraftRiverMixLayer implements AreaTransformer2, DimensionOffset0Transformer {
    private final TropicraftBiomeIds biomeIds;

    public TropicraftRiverMixLayer(TropicraftBiomeIds biomeIds) {
        this.biomeIds = biomeIds;
    }

    @Override
    public int applyPixel(Context random, Area parent1, Area parent2, int x, int y) {
        final int biome = parent1.get(getParentX(x), getParentY(y));
        final int river = parent2.get(getParentX(x), getParentY(y));

        if (!biomeIds.isOcean(biome)) {
            if (biomeIds.isRiver(river)) {
                return river;
            }
        }

        return biome;
    }
}
