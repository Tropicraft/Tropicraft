package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;

public final class TropicraftAddSubBiomesLayer implements C0Transformer {
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
	public int apply(Context random, int center) {
		if (center == baseID) {
			return subBiomeIDs[random.nextRandom(subBiomeIDs.length)];
		} else {
			return center;
		}
	}
}
