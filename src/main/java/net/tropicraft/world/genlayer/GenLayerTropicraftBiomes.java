package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftBiomes extends GenLayerTropicraft {
	
	private int[] biomeIDs;
	
	public GenLayerTropicraftBiomes(long seed, GenLayerTropicraft parent, int[] biomeIDs) {
		super(seed);
		this.parent = parent;
		this.biomeIDs = biomeIDs;
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		int[] parentMap = this.parent.getInts(x, y, width, length);
		int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				this.initChunkSeed((long) (i + x), (long) (j + y));
				int cur = parentMap[i + j * width];

				if(cur == landID) {
					resultMap[i + j * width] = biomeIDs[this.nextInt(biomeIDs.length)];
				} else {
					resultMap[i + j * width] = cur; // TODO: Add ocean biomes
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
