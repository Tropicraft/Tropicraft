package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftRiverMix extends GenLayerTropicraft {
	private int lakeID = BiomeGenTropicraft.tropicsLake.biomeID;

	private GenLayerTropicraft parentBiome;
	private GenLayerTropicraft parentRiver;

	public GenLayerTropicraftRiverMix(long seed, GenLayerTropicraft parentBiome, GenLayerTropicraft parentRiver) {
		super(seed);
		this.parentBiome = parentBiome;
		this.parentRiver = parentRiver;
		this.setZoom(1);
	}

	public void initWorldGenSeed(long par1) {
		this.parentBiome.initWorldGenSeed(par1);
		this.parentRiver.initWorldGenSeed(par1);
		super.initWorldGenSeed(par1);
	}

	public int[] getInts(int x, int y, int width, int length) {
		int[] biomeMap = this.parentBiome.getInts(x, y, width, length);
		int[] riverMap = this.parentRiver.getInts(x, y, width, length);
		int[] resultMap = IntCache.getIntCache(width * length);

		for(int index = 0; index < width * length; ++index) {
			if(riverMap[index] != -1 && biomeMap[index] != oceanID && biomeMap[index] != lakeID) {
				resultMap[index] = riverMap[index];
			} else {
				resultMap[index] = biomeMap[index];
			}
		}

		return resultMap;
	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
		this.parentBiome.setZoom(zoom);
		this.parentRiver.setZoom(zoom);
	}

}