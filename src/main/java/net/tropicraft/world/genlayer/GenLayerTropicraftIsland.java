package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftIsland extends GenLayerTropicraft {
	
	public GenLayerTropicraftIsland(long i) {
		super(i);
	}

	@Override
	public int[] getInts(int i, int j, int k, int l) {
		
		int[] aint = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < l; ++i1)
        {
            for (int j1 = 0; j1 < k; ++j1)
            {
                this.initChunkSeed((long)(i + j1), (long)(j + i1));
                aint[j1 + i1 * k] = this.nextInt(10) == 0 ? landID : oceanID;
            }
        }

        if (i > -k && i <= 0 && j > -l && j <= 0)
        {
            aint[-i + -j * k] = landID;
        }

        return aint;
	}

}
