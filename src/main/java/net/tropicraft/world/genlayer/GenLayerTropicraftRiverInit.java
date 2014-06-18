package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftRiverInit extends GenLayerTropicraft {

	private int oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
	private int landID = BiomeGenTropicraft.tropics.biomeID;
	
	public GenLayerTropicraftRiverInit(long par1) {
		super(par1);
	}
	
	public int[] getInts(int i, int j, int k, int l)
    {
        int[] aint1 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < l; ++i1)
        {
            for (int j1 = 0; j1 < k; ++j1)
            {
                this.initChunkSeed((long)(j1 + i), (long)(i1 + j));
                aint1[j1 + i1 * k] = this.nextInt(2) + 1;
            }
        }

        return aint1;
    }
	
}

