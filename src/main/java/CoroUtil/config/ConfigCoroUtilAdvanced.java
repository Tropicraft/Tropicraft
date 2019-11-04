package CoroUtil.config;

import modconfig.ConfigComment;
import modconfig.IConfigCategory;

import java.io.File;

public class ConfigCoroUtilAdvanced implements IConfigCategory {

	@ConfigComment("max repair speed will be whatever scheduleBlockUpdate set the tick, which is 30 seconds")
	public static boolean repairBlockNextRandomTick = false;

	@ConfigComment("Probably usefull if you want zombie miners get stopped by FTBU claimed chunks for example, but i dont want this behavior by default")
	public static boolean blockBreakingInvokesCancellableEvent = false;

	public static boolean removeInvasionAIWhenInvasionDone = true;

	//TODO: if false, will we be double buffing infernal mobs accidentally?
	@ConfigComment("Only used of HWMonsters is installed. If true, tie overall chance of infernal mobs to our difficulty system scaling, if false, don't try to control it at all")
	public static boolean difficulty_OverrideInfernalMobs = true;

	@ConfigComment("Track chunk bound data required for some difficulty calculations, disable if issues with server stability relating to CoroUtil, will affect HW-Invasions")
	public static boolean trackChunkData = true;

	@ConfigComment("-1 to disable. Not counting instant hits, this is a workaround for an ongoing issue where extremely high hit rates are logged causing super high dps")
	public static double difficulty_MaxAttackSpeedLoggable = 10;

	@ConfigComment("Makes sure the difficulty rating for dps doesnt go above this value")
	public static double difficulty_MaxDPSRatingAllowed = 5;

	@ConfigComment("-1 to disable. Limit the actual logged damage to this, but difficulty_MaxDPSRatingAllowed will still override what is used for difficulty value")
	public static double difficulty_MaxDPSLoggable = 500;
    public static boolean chunkCacheOverworldOnly = false;
    public static boolean usePlayerRadiusChunkLoadingForFallback = true;
    public static String chunkCacheDimensionBlacklist_IDs = "";
    public static String chunkCacheDimensionBlacklist_Names = "promised";
    @ConfigComment("Used for tracking time spent in chunk and block right clicks for measuring activity for difficulty, currently unused")
    public static boolean trackPlayerData = false;
    public static boolean PFQueueDebug = false;
    @ConfigComment("Test admin thing for kcauldron issues, kills zombies a bit after sunrise every cleanupStrayMobsDayRate days")
    public static boolean cleanupStrayMobs = false;
    public static int cleanupStrayMobsDayRate = 5;
    public static int cleanupStrayMobsTimeOfDay = 2000;
    public static boolean desirePathDerp = false;
    public static boolean headshots = false;
    public static boolean useCoroPets = false;
    @ConfigComment("Fix WorldEntitySpawner crash caused by other mods that look like this https://github.com/pWn3d1337/Techguns/issues/132")
    public static boolean fixBadBiomeEntitySpawnEntries = false;
	public static boolean disableParticleRenderer = false;
	public static boolean disableMipmapFix = false;

	public static int worldTimeDelayBetweenLongDistancePathfindTries = 40;

	public static boolean logging_DPS_Fine = false;
	public static boolean logging_DPS_HighSources = false;

	public static boolean logging_DPS_All = false;

	public static boolean minersPushAwayOtherNonMinerMobsWhileMining = true;
	public static boolean minersPushAwayOnlyOtherBuffedMobs = true;

	public static boolean enableDebugRenderer = false;

	@Override
	public String getName() {
		return "Advanced";
	}

	@Override
	public String getRegistryName() {
		return "coroutil_advanced";
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

	}

}
