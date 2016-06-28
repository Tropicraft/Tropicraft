package net.tropicraft.core.common.worldgen.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftRiverInit extends GenLayerTropicraft {

	private static final int AREAS = 4;

	public GenLayerTropicraftRiverInit(long seed) {
		super(seed);
		this.setZoom(1);
	}

	public int[] getInts(int x, int y, int width, int length) {
		int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				this.initChunkSeed((long) (i + x), (long) (j + y));
				resultMap[i + j * width] = this.nextInt(AREAS) + 1;
			}
		}

		return resultMap;
	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

}

