package net.tropicraft.core.common.worldgen.genlayer;

import net.minecraft.world.gen.layer.IntCache;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;

public class GenLayerTropicraftBeach extends GenLayerTropicraft {

	private int beachID = getID(BiomeGenTropicraft.tropicsBeach);

	public GenLayerTropicraftBeach(long seed, GenLayerTropicraft parent) {
		super(seed);
		this.parent = parent;
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		int x2 = x - 1;
		int y2 = y - 1;
		int width2 = width + 2;
		int length2 = length + 2;
		int[] parentMap = this.parent.getInts(x2, y2, width2, length2);
		int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				int backX = parentMap[i + 0 + (j + 1) * width2];
				int forwardX = parentMap[i + 2 + (j + 1) * width2];
				int backY = parentMap[i + 1 + (j + 0) * width2];
				int forwardY = parentMap[i + 1 + (j + 2) * width2];
				int cur = parentMap[i + 1 + (j + 1) * width2];

				if(cur == oceanID && (forwardX != oceanID || backY != oceanID || forwardY != oceanID || backX != oceanID)) {
					resultMap[i + j * width] = beachID;
				} else {
					resultMap[i + j * width] = cur;
				}
			}
		}
		return resultMap;
	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
		this.parent.setZoom(zoom);
	}

}
