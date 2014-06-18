package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftRiverMix extends GenLayer
{
	private int oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
	private int lakeID = BiomeGenTropicraft.tropicsLake.biomeID;
	
    private GenLayer parentBiome;
    private GenLayer parentRiver;

    public GenLayerTropicraftRiverMix(long par1, GenLayer par3GenLayer, GenLayer par4GenLayer)
    {
        super(par1);
        this.parentBiome = par3GenLayer;
        this.parentRiver = par4GenLayer;
    }

    public void initWorldGenSeed(long par1)
    {
        this.parentBiome.initWorldGenSeed(par1);
        this.parentRiver.initWorldGenSeed(par1);
        super.initWorldGenSeed(par1);
    }

    public int[] getInts(int i, int j, int k, int l)
    {
        int[] aint = this.parentBiome.getInts(i, j, k, l);
        int[] aint1 = this.parentRiver.getInts(i, j, k, l);
        int[] aint2 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < k * l; ++i1)
        {
        	if(aint1[i1] != -1 && aint[i1] != oceanID && aint[i1] != lakeID)
        	{
        		aint2[i1] = aint1[i1];
        	}
        	else
        	{
        		aint2[i1] = aint[i1];
        	}
        }

        return aint2;
    }
}