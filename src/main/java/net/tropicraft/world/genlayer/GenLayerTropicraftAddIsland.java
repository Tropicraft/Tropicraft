package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;
import net.tropicraft.world.biomes.BiomeGenTropicraft;

public class GenLayerTropicraftAddIsland extends GenLayerTropicraft {
	
	private int oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
	private int landID = BiomeGenTropicraft.tropics.biomeID;
	private int chance;
	
	public GenLayerTropicraftAddIsland(long seed, GenLayerTropicraft parent, int chance, int landID) {
		super(seed);
		this.parent = parent;
		this.chance = chance;
		this.landID = landID;
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		int x2 = x - 1;
        int y2 = y - 1;
        int width2 = width + 2;
        int length2 = length + 2;
        int[] parentMap = this.parent.getInts(x2, y2, width2, length2);
        int[] resultMap = IntCache.getIntCache(width * length);

		for(int i2 = 0; i2 < length; ++i2) {
			for(int j2 = 0; j2 < width; ++j2) {
				int backX = parentMap[j2 + 0 + (i2 + 1) * width2];
				int forwardX = parentMap[j2 + 2 + (i2 + 1) * width2];
				int backY = parentMap[j2 + 1 + (i2 + 0) * width2];
				int forwardY = parentMap[j2 + 1 + (i2 + 2) * width2];
				int cur = parentMap[j2 + 1 + (i2 + 1) * width2];
				this.initChunkSeed((long) (j2 + x), (long) (i2 + y));

				if(backX != landID && forwardX != landID && backY != landID && forwardY != landID && cur != landID && this.nextInt(chance) == 0) {
					resultMap[j2 + i2 * width] = landID;
				} else {
					resultMap[j2 + i2 * width] = cur;
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