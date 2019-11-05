package CoroUtil.config;

import modconfig.ConfigComment;
import modconfig.IConfigCategory;

import java.io.File;

public class ConfigCoroUtil implements IConfigCategory {

	@ConfigComment("Use a different json mob spawn template for testing different invasion setups, note this is referring to the 'format' tag in the json file, not the json file name itself, eg: mob_spawns_testing_miners from mob_spawns_testing_miners.json")
	public static String mobSpawnsProfile = "mob_spawns";

	@ConfigComment("Force a specific profile to spawn, will ignore conditions and force it too be used, usefull for testing to see how a custom invasion will play out in normal circumstances, set it to one of the named invasions within your templates list, eg: invasion_stage_1 from mob_spawns.json")
	public static String mobSpawnsWaveToForceUse = "";

	@ConfigComment("Used by weather2")
	public static boolean forceShadersOff = false;

	@ConfigComment("Provides better context for shaders/particles to work nice with translucent blocks like glass and water")
	public static boolean useEntityRenderHookForShaders = true;

	//maybe temp
	@ConfigComment("WIP, more strict transparent cloud usage, better on fps")
	public static boolean optimizedCloudRendering = false;

	@ConfigComment("Used by weather2")
	public static boolean debugShaders = false;

	@ConfigComment("Used by weather2")
	public static boolean foliageShaders = false;

	@ConfigComment("Used by weather2")
	public static boolean particleShaders = false;

	@ConfigComment("For seldom used but important things to print out in production")
	public static boolean useLoggingLog = true;

	@ConfigComment("For debugging things")
	public static boolean useLoggingDebug = false;

	@ConfigComment("For logging warnings/errors")
	public static boolean useLoggingError = true;

	@ConfigComment("Use at own risk, will not support")
	public static boolean enableAdvancedDeveloperConfigFiles = false;

	@Override
	public String getName() {
		return "General";
	}

	@Override
	public String getRegistryName() {
		return "coroutil_general";
	}

	@Override
	public String getConfigFileName() {
		return "CoroUtil" + File.separator + getName();
	}

	@Override
	public String getCategory() {
		return getName();
	}

	@Override
	public void hookUpdatedValues() {
		try {
			//TODO: 1.14 remove entirely? from old threaded pathfinder
			/*String[] ids = ConfigCoroUtilAdvanced.chunkCacheDimensionBlacklist_IDs.split(",");
			String[] names = ConfigCoroUtilAdvanced.chunkCacheDimensionBlacklist_Names.split(",");
			
			DimensionChunkCacheNew.listBlacklistIDs.clear();
			for (int i = 0; i < ids.length; i++) {
				DimensionChunkCacheNew.listBlacklistIDs.add(Integer.valueOf(ids[i]));
			}
			DimensionChunkCacheNew.listBlacklistNamess = Arrays.asList(names);*/
		} catch (Exception ex) {
			//silence!
		}

		//TODO: 1.14 uncomment
		/*if (ConfigCoroUtil.enableAdvancedDeveloperConfigFiles || CoroUtilCompatibility.isHWInvasionsInstalled() || CoroUtilCompatibility.isHWMonstersInstalled()) {
			ConfigMod.addConfigFile(CoroUtil.forge.CoroUtil.configDD);
		}
		if (ConfigCoroUtil.enableAdvancedDeveloperConfigFiles) {
			ConfigMod.addConfigFile(CoroUtil.forge.CoroUtil.configHWMonsters);
			ConfigMod.addConfigFile(CoroUtil.forge.CoroUtil.configDev);
		}*/
	}

}
