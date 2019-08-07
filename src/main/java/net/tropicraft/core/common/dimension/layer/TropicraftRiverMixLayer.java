package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

public enum TropicraftRiverMixLayer implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, IArea parent1, IArea parent2, int x, int y) {
        final int biome = parent1.getValue(this.func_215721_a(x), this.func_215722_b(y));
        final int river = parent2.getValue(this.func_215721_a(x), this.func_215722_b(y));

        // ordering problems?
        if (TropicraftLayerUtil.isRiver(river)) {
            return river;
        }

        if (TropicraftLayerUtil.isOcean(biome)) {
            return biome;
        }

        return biome;
    }
}
