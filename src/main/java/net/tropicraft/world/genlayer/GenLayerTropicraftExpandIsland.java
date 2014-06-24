package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftExpandIsland extends GenLayerTropicraft {

	public GenLayerTropicraftExpandIsland(long seed, GenLayerTropicraft parent) {
		super(seed);
		this.parent = parent;
		this.setZoom(1);
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
				int backXY = parentMap[i + 0 + (j + 0) * width2];
				int forwardX = parentMap[i + 2 + (j + 0) * width2];
				int forwardY = parentMap[i + 0 + (j + 2) * width2];
				int forwardXY = parentMap[i + 2 + (j + 2) * width2];
				int cur = parentMap[i + 1 + (j + 1) * width2];
				this.initChunkSeed(i + x, j + y);

				if(cur == 0 && (backXY != 0 || forwardX != 0 || forwardY != 0 || forwardXY != 0)) {
					int chance = 1;
					int result = landID;

					if(backXY != 0 && this.nextInt(chance++) == 0) {
						result = backXY;
					}

					if(forwardX != 0 && this.nextInt(chance++) == 0) {
						result = forwardX;
					}

					if(forwardY != 0 && this.nextInt(chance++) == 0) {
						result = forwardY;
					}

					if(forwardXY != 0 && this.nextInt(chance++) == 0) {
						result = forwardXY;
					}

					if(this.nextInt(3) == 0) {
						resultMap[i + j * width] = result;
					} else {
						resultMap[i + j * width] = oceanID;
					}
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
