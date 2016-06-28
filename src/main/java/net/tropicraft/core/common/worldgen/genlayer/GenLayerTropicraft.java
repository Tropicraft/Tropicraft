package net.tropicraft.core.common.worldgen.genlayer;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;

public abstract class GenLayerTropicraft extends GenLayer {

	protected int zoom;
	protected GenLayerTropicraft parent;

	protected static int oceanID = getID(BiomeGenTropicraft.tropicsOcean);
	protected static int landID = getID(BiomeGenTropicraft.tropics);

	private static int[] landBiomeIDs = new int[] { landID, getID(BiomeGenTropicraft.rainforestPlains) };
	private static int[] rainforestBiomeIDs = new int[] {
		getID(BiomeGenTropicraft.rainforestPlains),
		getID(BiomeGenTropicraft.rainforestHills),
		getID(BiomeGenTropicraft.rainforestMountains)
	};

	public GenLayerTropicraft(long par1) {
		super(par1);
	}

	protected static int getID(Biome biome) {
		return Biome.getIdForBiome(biome);
	}

	public static GenLayer[] initializeAllBiomeGenerators(long par0, WorldType par2WorldType) {
		GenLayerTropicraft layerIsland = new GenLayerTropicraftIsland(1L);
		GenLayerTropicraft layerFuzzyZoom = new GenLayerTropicraftFuzzyZoom(2000L, layerIsland);
		GenLayerTropicraft layerExpand = new GenLayerTropicraftExpandIsland(2L, layerFuzzyZoom);
		GenLayerTropicraft layerAddIsland = new GenLayerTropicraftAddIsland(3L, layerExpand, 10, landID);
		GenLayerTropicraft layerZoom = new GenLayerTropicraftZoom(2000L, layerAddIsland);
		layerExpand = new GenLayerTropicraftExpandIsland(6L, layerZoom);
		GenLayerTropicraft layerLake = new GenLayerTropicraftAddInland(9L, layerExpand, 20, landID);
		layerAddIsland = new GenLayerTropicraftAddIsland(5L, layerLake, 8, landID);
		layerAddIsland = new GenLayerTropicraftAddIsland(6L, layerLake, 13, getID(BiomeGenTropicraft.islandMountains));
		layerZoom = new GenLayerTropicraftZoom(2001L, layerAddIsland);
		layerExpand = new GenLayerTropicraftExpandIsland(7L, layerZoom);
		layerAddIsland = new GenLayerTropicraftAddIsland(8L, layerExpand, 9, landID);
		layerExpand = new GenLayerTropicraftExpandIsland(10L, layerAddIsland);
		GenLayerTropicraft genLayerBiomes = new GenLayerTropicraftBiomes(15L, layerExpand, landBiomeIDs);
		GenLayerTropicraft genLayerHills = new GenLayerTropicraftAddSubBiomes(16L, genLayerBiomes, getID(BiomeGenTropicraft.rainforestHills), rainforestBiomeIDs);
		layerZoom = new GenLayerTropicraftZoom(2002L, genLayerHills);
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
