package net.tropicraft.core.common.worldgen.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftIsland extends GenLayerTropicraft {
	
	private int chanceOfLand = 4;
	
	public GenLayerTropicraftIsland(long seed, int chanceOfLand) {
		super(seed);
		this.setZoom(1);
		this.chanceOfLand = chanceOfLand;
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		
		int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				this.initChunkSeed((long) (x + i), (long) (y + j));
				resultMap[i + j * width] = this.nextInt(chanceOfLand) == 0 ? landID : oceanID;
			}
		}

		// We think this contributed to generating islands far out instead of other land
//		if(x > -width && x <= 0 && y > -length && y <= 0) {
//			resultMap[-x + -y * width] = landID;
//		}

		return resultMap;
	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
}
