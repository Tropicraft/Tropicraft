package net.tropicraft.core.common.dimension.layer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public enum TropicraftAddWeightedSubBiomesLayer implements IC0Transformer {
    OCEANS(TropicraftLayerUtil.OCEAN_ID, new LazyInt[]{TropicraftLayerUtil.OCEAN_ID, TropicraftLayerUtil.KELP_FOREST_ID}, new WeightedRandom.Item(20), new WeightedRandom.Item(4))
    ;
    private List<WeightedRandom.Item> biomeWeights;
    private int totalWeight;
    final LazyInt baseID;
    final LazyInt[] subBiomeIDs;
    private Map<WeightedRandom.Item, LazyInt> biomeLookup = new HashMap<>();

    TropicraftAddWeightedSubBiomesLayer(final LazyInt baseID, final LazyInt[] subBiomeIDs, WeightedRandom.Item... weights) {
        if (weights.length > 0) {
            biomeWeights = Lists.newArrayList(weights);
            totalWeight = WeightedRandom.getTotalWeight(biomeWeights);
            for (int i = 0; i < weights.length; i++) {
                biomeLookup.put(weights[i], subBiomeIDs[i]);
            }
        }
        this.baseID = baseID;
        this.subBiomeIDs = subBiomeIDs;
    }

    @Override
    public int apply(INoiseRandom random, int center) {
        if (center == baseID.getAsInt()) {
            if (biomeLookup.size() > 0) {
                int res = biomeLookup.get(WeightedRandom.getRandomItem(biomeWeights, random.random(totalWeight))).getAsInt();
                return res;
            }
            return subBiomeIDs[random.random(subBiomeIDs.length)].getAsInt();
        } else {
            return center;
        }
    }
}
