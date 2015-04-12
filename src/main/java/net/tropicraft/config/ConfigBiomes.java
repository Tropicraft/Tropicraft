package net.tropicraft.config;

import modconfig.IConfigCategory;

public class ConfigBiomes implements IConfigCategory {

	public static int tropicsOceanID = 60;
	public static int tropicsID = 61;
	public static int rainforestPlainsID = 62;
	public static int rainforestHillsID = 63;
	public static int rainforestMountainsID = 64;
	public static int tropicsRiverID = 65;
	public static int tropicsBeachID = 66;
	public static int tropicsLakeID = 67;
	public static int islandMountainsID = 68;
	
	@Override
	public String getConfigFileName() {
		return "Tropicraft_Biomes";
	}

	@Override
	public String getCategory() {
		return "Tropicraft Biomes Config";
	}

	@Override
	public void hookUpdatedValues() {
		//apply changes to code here if needed
	}

}
