package net.tropicraft.world.genlayer;

import net.tropicraft.world.biomes.BiomeGenTropicraft;
import net.tropicraft.world.biomes.BiomeGenTropics;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public abstract class GenLayerTropicraft extends GenLayer {

	protected static int oceanID = BiomeGenTropicraft.tropicsOcean.biomeID;
	protected static int landID = BiomeGenTropicraft.tropics.biomeID;
	
	private static int[] landBiomeIDs = new int[] { BiomeGenTropicraft.tropics.biomeID, BiomeGenTropicraft.rainforestPlains.biomeID };
	private static int[] rainforestBiomeIDs = new int[] { BiomeGenTropicraft.rainforestPlains.biomeID, BiomeGenTropicraft.rainforestHills.biomeID,
		BiomeGenTropicraft.rainforestMountains.biomeID
	};
	
	public GenLayerTropicraft(long par1) {
		super(par1);
	}
	
	public static GenLayer[] initializeAllBiomeGenerators(long par0, WorldType par2WorldType)
    {
        GenLayerTropicraftIsland layerIsland = new GenLayerTropicraftIsland(1L);
        GenLayerFuzzyZoom layerFuzzyZoom = new GenLayerFuzzyZoom(2000L, layerIsland);
        GenLayerTropicraftExpandIsland layerExpand = new GenLayerTropicraftExpandIsland(2L, layerFuzzyZoom);
        GenLayerTropicraftAddIsland layerAddIsland = new GenLayerTropicraftAddIsland(3L, layerExpand, 10, landID);
        GenLayerZoom layerZoom = new GenLayerZoom(2000L, layerAddIsland);
        layerExpand = new GenLayerTropicraftExpandIsland(6L, layerZoom);
        GenLayerTropicraftAddInland layerLake = new GenLayerTropicraftAddInland(9L, layerExpand, 20, landID);
        layerAddIsland = new GenLayerTropicraftAddIsland(5L, layerLake, 8, landID);
        layerAddIsland = new GenLayerTropicraftAddIsland(6L, layerLake, 13, BiomeGenTropicraft.islandMountains.biomeID);
        layerZoom = new GenLayerZoom(2001L, layerAddIsland);
        layerExpand = new GenLayerTropicraftExpandIsland(7L, layerZoom);
        layerAddIsland = new GenLayerTropicraftAddIsland(8L, layerExpand, 9, landID);
        layerExpand = new GenLayerTropicraftExpandIsland(10L, layerAddIsland);
        GenLayerTropicraftBiomes genLayerBiomes = new GenLayerTropicraftBiomes(15L, layerExpand, landBiomeIDs);
        GenLayerTropicraftAddSubBiomes genLayerHills = new GenLayerTropicraftAddSubBiomes(16L, genLayerBiomes, BiomeGenTropicraft.rainforestHills.biomeID, rainforestBiomeIDs);
        layerZoom = new GenLayerZoom(2002L, genLayerHills);
    	layerExpand = new GenLayerTropicraftExpandIsland(10L, layerZoom);
        
        for(int i = 0; i < 4; i++)
        {
        	layerExpand = new GenLayerTropicraftExpandIsland(10L, layerExpand);
        }
        
        GenLayerTropicraftRiverInit layerRiverInit = new GenLayerTropicraftRiverInit(12L);
        GenLayer layerRiverMag = GenLayerZoom.magnify(2007L, layerRiverInit, 5);
        GenLayerTropicraftRiver layerRiver = new GenLayerTropicraftRiver(13L, layerRiverMag);
        GenLayerSmooth layerRiverSmooth = new GenLayerSmooth(2008L, layerRiver);
        
        GenLayer layerMagnify = GenLayerZoom.magnify(2007L, layerExpand, 3);
        GenLayer layerBeach = new GenLayerTropicraftBeach(20L, layerMagnify);
        GenLayerSmooth layerBiomesSmooth = new GenLayerSmooth(17L, layerBeach);
        GenLayerTropicraftRiverMix layerRiverMix = new GenLayerTropicraftRiverMix(5L, layerBiomesSmooth, layerRiverSmooth);
        
        GenLayerVoronoiZoom layerVoronoi = new GenLayerVoronoiZoom(10L, layerRiverMix);
        layerRiverMix.initWorldGenSeed(par0);
        layerVoronoi.initWorldGenSeed(par0);
        return new GenLayer[] {layerRiverMix, layerVoronoi};
    }
	
}
