package net.tropicraft.core.common.dimension.layer;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

import java.util.List;

public enum TropicraftAddWeightedSubBiomesLayer implements IC0Transformer {
    // TODO add kelp forest here to sub biomes list
    OCEANS(TropicraftLayerUtil.OCEAN_ID, new int[]{TropicraftLayerUtil.OCEAN_ID}, new WeightedRandom.Item(20))
    ;
    private List<WeightedRandom.Item> biomeWeights;
    private int totalWeight;
    final int baseID;
    final int[] subBiomeIDs;
    private Object2IntMap<WeightedRandom.Item> biomeLookup = new Object2IntOpenHashMap<>();

    TropicraftAddWeightedSubBiomesLayer(final int baseID, final int[] subBiomeIDs, WeightedRandom.Item... weights) {
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
        if (center == baseID) {
            if (biomeLookup.size() > 0) {
                return biomeLookup.get(WeightedRandom.getRandomItem(biomeWeights, random.random(totalWeight)));
            }
            return subBiomeIDs[random.random(subBiomeIDs.length)];
        } else {
            return center;
        }
    }
}
