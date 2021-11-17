package net.tropicraft.core.common.dimension.layer;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.WeighedRandom;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;

import java.util.List;

public final class TropicraftAddWeightedSubBiomesLayer implements C0Transformer {
    private List<WeighedRandom.WeighedRandomItem> biomeWeights;
    private int totalWeight;
    final int baseID;
    final int[] subBiomeIDs;
    private final Object2IntMap<WeighedRandom.WeighedRandomItem> biomeLookup = new Object2IntOpenHashMap<>();

    TropicraftAddWeightedSubBiomesLayer(final int baseID, final int[] subBiomeIDs, WeighedRandom.WeighedRandomItem... weights) {
        if (weights.length > 0) {
            biomeWeights = Lists.newArrayList(weights);
            totalWeight = WeighedRandom.getTotalWeight(biomeWeights);
            for (int i = 0; i < weights.length; i++) {
                biomeLookup.put(weights[i], subBiomeIDs[i]);
            }
        }
        this.baseID = baseID;
        this.subBiomeIDs = subBiomeIDs;
    }

    public static TropicraftAddWeightedSubBiomesLayer ocean(TropicraftBiomeIds biomeIds) {
        return new TropicraftAddWeightedSubBiomesLayer(biomeIds.ocean, new int[]{biomeIds.ocean, biomeIds.kelpForest}, new WeighedRandom.WeighedRandomItem(20), new WeighedRandom.WeighedRandomItem(4));
    }

    @Override
    public int apply(Context random, int center) {

        if (center == baseID) {
            //TODO [PORT]: Issue with getting weighted item, will need to be fixed for the future as it constantly returned a 0 value all the time mean
//            if (biomeLookup.size() > 0) {
//                return biomeLookup.getInt(WeighedRandom.getWeightedItem(biomeWeights, random.nextRandom(totalWeight)));
//            }
            return subBiomeIDs[random.nextRandom(subBiomeIDs.length)];
        } else {
            return center;
        }
    }
}
