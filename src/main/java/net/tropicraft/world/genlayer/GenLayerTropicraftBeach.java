package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.tropicraft.world.biomes.BiomeGenTropicraft;

public class GenLayerTropicraftBeach extends GenLayerTropicraft {

	private int beachID = BiomeGenTropicraft.tropicsBeach.biomeID;
	private int riverID = BiomeGenTropicraft.tropicsRiver.biomeID;
	private int oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
	
	public GenLayerTropicraftBeach(long par1, GenLayer parent) {
		super(par1);
		this.parent = parent;
	}

	@Override
	public int[] getInts(int i, int j, int k, int l) {
		int i1 = i - 1;
        int j1 = j - 1;
        int k1 = k + 2;
        int l1 = l + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(k * l);

        for (int i2 = 0; i2 < l; ++i2)
        {
            for (int j2 = 0; j2 < k; ++j2)
            {
                int k2 = aint[j2 + 0 + (i2 + 1) * k1];
                int l2 = aint[j2 + 2 + (i2 + 1) * k1];
                int i3 = aint[j2 + 1 + (i2 + 0) * k1];
                int j3 = aint[j2 + 1 + (i2 + 2) * k1];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];

                if (k3 == oceanID && (l2 != oceanID || i3 != oceanID || j3 != oceanID || k2 != oceanID))
                {
                    aint1[j2 + i2 * k] = beachID;
                }
                else
                {
                    aint1[j2 + i2 * k] = k3;
                }
            }
        }

        return aint1;		
	}

}
