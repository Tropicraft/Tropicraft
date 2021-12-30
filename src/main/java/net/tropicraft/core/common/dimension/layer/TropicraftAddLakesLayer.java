package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public class TropicraftAddLakesLayer implements IC0Transformer {
    private final TropicraftBiomeIds ids;

    public TropicraftAddLakesLayer(TropicraftBiomeIds ids) {
        this.ids = ids;
    }

    private boolean canLakeGenerate(int id) {
        return id == this.ids.land || id == this.ids.rainforestPlains || id == this.ids.rainforestHills || id == this.ids.osaRainforest;
    }

    @Override
    public int apply(INoiseRandom context, int sample) {
        if (canLakeGenerate(sample)) {
            if (context.random(28) == 0) {
                return this.ids.lake;
            }
        }


        return sample;
    }
}
