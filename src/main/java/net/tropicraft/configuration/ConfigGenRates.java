package net.tropicraft.configuration;

import configuration.IConfigCategory;

public class ConfigGenRates implements IConfigCategory {

	public static final int EIH_CHANCE = 50;
	public static final int SHIPWRECK_CHANCE = 300;
	public static final int TALL_FLOWERS_CHANCE = 3;
	public static final int BAMBOO_CHANCE = 2;
	public static final int WATERFALL_AMOUNT = 25;
	public static final int TALL_GRASS_CHANCE = 4;
	public static final int CURVED_PALM_CHANCE = 3;
	public static final int LARGE_PALM_CHANCE = 3;
	public static final int NORMAL_PALM_CHANCE = 3;
	public static final int FRUIT_TREE_CHANCE = 2;
	public static final int TREASURE_CHANCE = 225;
	
	public static boolean genPalmsInOverworld = true;
	public static boolean genOverworldPalmsInBeachOnly = false;
	public static int palmChanceOfGenInOverworld = -1;
	public static int palmPopulationFactorInOverworld = 3;
	public static boolean genTropicraftEIHInOverworld = true;
	public static boolean genTropicraftFlowersInOverworld = true;
	public static boolean genTropicraftInOverworld = true;
	public static boolean genPineapplesInOverworld = true;
	public static boolean genBambooInOverworld = true;
	
	@Override
	public String getConfigFileName() {
		return "Tropicraft_GenRates";
	}

	@Override
	public String getCategory() {
		return "Tropicraft Gen Rates Config";
	}

	@Override
	public void hookUpdatedValues() {
		//apply changes to code here if needed
	}

}
