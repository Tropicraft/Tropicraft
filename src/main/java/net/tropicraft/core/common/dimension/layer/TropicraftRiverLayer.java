package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum TropicraftRiverLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom iNoiseRandom, int i, int i1, int i2, int i3, int i4) {
        return 0;
    }
}
