package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public final class TropicraftAddSubBiomesLayer implements IC0Transformer {
	private final int baseID;
	private final int[] subBiomeIDs;
	private final int chance;

	TropicraftAddSubBiomesLayer(int baseID, int[] subBiomeIDs, int chance) {
		this.baseID = baseID;
		this.subBiomeIDs = subBiomeIDs;
		this.chance = chance;
	}

	public static TropicraftAddSubBiomesLayer mangroves(TropicraftBiomeIds biomeIds) {
		return new TropicraftAddSubBiomesLayer(biomeIds.mangroves, new int[] { biomeIds.overgrownMangroves }, 4);
	}

	public static TropicraftAddSubBiomesLayer rainforest(TropicraftBiomeIds biomeIds) {
		return new TropicraftAddSubBiomesLayer(biomeIds.rainforestPlains, new int[] { biomeIds.rainforestPlains, biomeIds.rainforestHills, biomeIds.rainforestMountains, biomeIds.bambooRainforest }, 1);
	}

	@Override
	public int apply(INoiseRandom random, int center) {
		if (center == baseID && random.random(this.chance) == 0) {
			return subBiomeIDs[random.random(subBiomeIDs.length)];
		} else {
			return center;
		}
	}
}
