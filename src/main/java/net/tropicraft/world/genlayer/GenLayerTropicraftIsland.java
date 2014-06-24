package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftIsland extends GenLayerTropicraft {
	
	private static final int CHANCE_OF_LAND = 10;
	
	public GenLayerTropicraftIsland(long seed) {
		super(seed);
		this.setZoom(1);
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		
		int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				this.initChunkSeed((long) (x + i), (long) (y + j));
				resultMap[i + j * width] = this.nextInt(CHANCE_OF_LAND) == 0 ? landID : oceanID;
			}
		}

		if(x > -width && x <= 0 && y > -length && y <= 0) {
			resultMap[-x + -y * width] = landID;
		}

		return resultMap;
	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
}
