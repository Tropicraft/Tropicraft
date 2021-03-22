package net.tropicraft.core.common.dimension.layer;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

import java.util.List;

public final class TropicraftAddWeightedSubBiomesLayer implements IC0Transformer {
    private List<WeightedRandom.Item> biomeWeights;
    private int totalWeight;
    final int baseID;
    final int[] subBiomeIDs;
    private final Object2IntMap<WeightedRandom.Item> biomeLookup = new Object2IntOpenHashMap<>();

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

    public static TropicraftAddWeightedSubBiomesLayer ocean(TropicraftBiomeIds biomeIds) {
        return new TropicraftAddWeightedSubBiomesLayer(biomeIds.ocean, new int[]{biomeIds.ocean, biomeIds.kelpForest}, new WeightedRandom.Item(20), new WeightedRandom.Item(4));
    }

    @Override
    public int apply(INoiseRandom random, int center) {
        if (center == baseID) {
            if (biomeLookup.size() > 0) {
                return biomeLookup.getInt(WeightedRandom.getRandomItem(biomeWeights, random.random(totalWeight)));
            }
            return subBiomeIDs[random.random(subBiomeIDs.length)];
        } else {
            return center;
        }
    }
}
