package net.tropicraft.core.common.worldgen.genlayer;

import net.minecraft.util.WeightedRandom;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.tropicraft.core.common.biome.BiomeTropicraft;

public abstract class GenLayerTropicraft extends GenLayer {

    //zoom - takes the output from the parent layer and grows it, with some scaling algorithm
    // fuzzy zoom - The fuzzy zoom randomly samples from 4 pixels to decide which biome should be in the scaled map
	protected int zoom;
	protected GenLayerTropicraft parent;

	protected static int oceanID = getID(BiomeTropicraft.tropicsOcean);
	protected static int landID = getID(BiomeTropicraft.tropics);

	private static int[] landBiomeIDs = new int[] { landID, getID(BiomeTropicraft.rainforestPlains) };
	private static int[] rainforestBiomeIDs = new int[] {
//		getID(BiomeGenTropicraft.rainforestPlains),
		getID(BiomeTropicraft.rainforestHills),
		getID(BiomeTropicraft.rainforestMountains)
	};

	private static int[] oceanBiomeIDs = new int[] {
		getID(BiomeTropicraft.tropicsOcean),
//		getID(BiomeGenTropicraft.kelpForest)
	};
	private static WeightedRandom.Item[] oceanBiomeWeights = {
		new WeightedRandom.Item(20),
//		new WeightedRandom.Item(1)
	};

	public GenLayerTropicraft(long par1) {
		super(par1);
	}

	protected static int getID(Biome biome) {
		return Biome.getIdForBiome(biome);
	}

	public static GenLayer[] initializeAllBiomeGenerators(long par0, WorldType par2WorldType) {
		GenLayerTropicraft layerIsland = new GenLayerTropicraftIsland(1L, /*10*/3);
		GenLayerTropicraft layerFuzzyZoom = new GenLayerTropicraftFuzzyZoom(2000L, layerIsland);
		GenLayerTropicraft layerExpand = new GenLayerTropicraftExpandIsland(2L, layerFuzzyZoom);
		//layerExpand = new GenLayerTropicraftExpandIsland(1L, layerFuzzyZoom); // new
		GenLayerTropicraft layerAddIsland = new GenLayerTropicraftAddIsland(3L, layerExpand, /*10*/3, landID);
		GenLayerTropicraft layerZoom = new GenLayerTropicraftZoom(2000L, layerAddIsland);
		layerExpand = new GenLayerTropicraftExpandIsland(55L/*6*/, layerZoom);
		GenLayerTropicraft layerLake = new GenLayerTropicraftAddInland(9L, layerExpand, 20, landID);
		layerAddIsland = new GenLayerTropicraftAddIsland(5L, layerLake, 8, landID);
		layerAddIsland = new GenLayerTropicraftAddIsland(6L, layerLake, 13, getID(BiomeTropicraft.islandMountains));
		layerZoom = new GenLayerTropicraftZoom(2001L, layerAddIsland);
		layerZoom = new GenLayerTropicraftZoom(2004L, layerZoom);
		layerExpand = new GenLayerTropicraftExpandIsland(7L, layerZoom);
		layerAddIsland = new GenLayerTropicraftAddIsland(8L, layerExpand, 2/*9*/, landID);
		layerExpand = new GenLayerTropicraftExpandIsland(10L, layerAddIsland);
		GenLayerTropicraft genLayerBiomes = new GenLayerTropicraftBiomes(15L, layerExpand, landBiomeIDs);
		GenLayerTropicraft genLayerOceans = new GenLayerTropicraftAddWeightedSubBiomes(16L, genLayerBiomes, oceanID, oceanBiomeIDs, oceanBiomeWeights);
		GenLayerTropicraft genLayerHills = new GenLayerTropicraftAddSubBiomes(17L/*16*/, genLayerOceans, getID(BiomeTropicraft.rainforestPlains), rainforestBiomeIDs);
		layerZoom = new GenLayerTropicraftZoom(2002L, genLayerHills);
		layerZoom = new GenLayerTropicraftZoom(2003L, genLayerHills);
		layerExpand = new GenLayerTropicraftExpandIsland(10L, layerZoom);

		for(int i = 0; i < 4; i++) {
			layerExpand = new GenLayerTropicraftExpandIsland(10L, layerExpand);
		}

		GenLayerTropicraft layerRiverInit = new GenLayerTropicraftRiverInit(12L);
		GenLayerTropicraft layerRiverMag = GenLayerTropicraftZoom.magnify(2007L, layerRiverInit, 5);
		GenLayerTropicraft layerRiver = new GenLayerTropicraftRiver(13L, layerRiverMag);
		GenLayerTropicraft layerRiverSmooth = new GenLayerTropicraftSmooth(2008L, layerRiver);

		GenLayerTropicraft layerMagnify = GenLayerTropicraftZoom.magnify(2007L, layerExpand, 3);
		GenLayerTropicraft layerBeach = new GenLayerTropicraftBeach(20L, layerMagnify);
		GenLayerTropicraft layerBiomesSmooth = new GenLayerTropicraftSmooth(17L, layerBeach);
		GenLayerTropicraft layerRiverMix = new GenLayerTropicraftRiverMix(5L, layerBiomesSmooth, layerRiverSmooth);

		GenLayerTropicraft layerVoronoi = new GenLayerTropiVoronoiZoom(10L, layerRiverMix, GenLayerTropiVoronoiZoom.Mode.MANHATTAN);
		layerRiverMix.initWorldGenSeed(par0);
		layerVoronoi.initWorldGenSeed(par0);
		return new GenLayer[] {layerRiverMix, layerVoronoi};
	}

	public abstract void setZoom(int zoom);

}
