package net.tropicraft.core.common.worldgen.genlayer;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.util.WeightedRandom;

public class GenLayerTropicraftAddWeightedSubBiomes extends GenLayerTropicraftAddSubBiomes {

	private final List<WeightedRandom.Item> biomeWeights;
	private final int totalWeight;

	private TObjectIntMap<WeightedRandom.Item> biomeLookup = new TObjectIntHashMap<>();

	public GenLayerTropicraftAddWeightedSubBiomes(long seed, GenLayerTropicraft parent, int baseID, int[] biomes, WeightedRandom.Item... weights) {
		super(seed, parent, baseID, new int[0]);
		Preconditions.checkArgument(biomes.length == weights.length, "Must provide as many weights as there are biomes!");
		this.biomeWeights = Lists.newArrayList(weights);
		this.totalWeight = WeightedRandom.getTotalWeight(biomeWeights);

		for (int i = 0; i < weights.length; i++) {
			biomeLookup.put(weights[i], biomes[i]);
		}
	}

	@Override
	protected int getSubBiome(int id) {
		return biomeLookup.get(WeightedRandom.getRandomItem(biomeWeights, nextInt(totalWeight)));
	}
	
	@Override
	public void initChunkSeed(long p_75903_1_, long p_75903_3_) {
		super.initChunkSeed(p_75903_1_ >> 1, p_75903_3_ >> 1);
	}
}
