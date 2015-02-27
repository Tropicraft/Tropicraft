package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftAddInland extends GenLayerTropicraft {
	
	private int inlandID;
	private int chance;
	
	public GenLayerTropicraftAddInland(long seed, GenLayerTropicraft parent, int chance, int inlandID) {
		super(seed);
		this.parent = parent;
		this.chance = chance;
		this.inlandID = inlandID;
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		int x2 = x - 1;
        int y2 = y - 1;
        int width2 = width + 2;
        int height2 = length + 2;
        int[] parentMap = this.parent.getInts(x2, y2, width2, height2);
        int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				int backX = parentMap[i + 0 + (j + 1) * width2];
				int forwardX = parentMap[i + 2 + (j + 1) * width2];
				int backY = parentMap[i + 1 + (j + 0) * width2];
				int forwardY = parentMap[i + 1 + (j + 2) * width2];
				int cur = parentMap[i + 1 + (j + 1) * width2];
				this.initChunkSeed((long) (i + x), (long) (j + y));

				if(backX == landID && forwardX == landID && backY == landID && forwardY == landID && cur == landID && this.nextInt(chance) == 0) {
					resultMap[i + j * width] = inlandID;
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
		parent.setZoom(zoom);
	}

}