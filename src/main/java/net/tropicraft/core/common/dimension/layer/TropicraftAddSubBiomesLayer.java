package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public final class TropicraftAddSubBiomesLayer implements IC0Transformer {
	final int baseID;
	final int[] subBiomeIDs;

	TropicraftAddSubBiomesLayer(final int baseID, final int[] subBiomeIDs) {
		this.baseID = baseID;
		this.subBiomeIDs = subBiomeIDs;
	}

	public static TropicraftAddSubBiomesLayer rainforest(TropicraftBiomeIds biomeIds) {
		return new TropicraftAddSubBiomesLayer(biomeIds.rainforestPlains, new int[] { biomeIds.rainforestPlains, biomeIds.rainforestHills, biomeIds.rainforestMountains, biomeIds.bambooRainforest });
	}

	@Override
	public int apply(INoiseRandom random, int center) {
		if (center == baseID) {
			return subBiomeIDs[random.random(subBiomeIDs.length)];
		} else {
			return center;
		}
	}
}
