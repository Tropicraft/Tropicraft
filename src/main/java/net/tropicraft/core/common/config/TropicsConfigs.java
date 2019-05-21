package net.tropicraft.core.common.config;

import java.io.File;

import com.google.common.base.Preconditions;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.Info;
import net.tropicraft.configuration.GenRates;

@EventBusSubscriber
public class TropicsConfigs {

    /* == Generation Configs == */

    public static boolean genOverworld = true;

    public static boolean genOverworldPalms = true;
    public static boolean genOverworldPalmsBeachOnly = false;
    public static int chancePalmOverworld = -1;
    public static int factorPalmOverworld = 3;

    public static boolean genOverworldEIH = true;
    public static boolean genOverworldFlowers = true;
    public static boolean genOverworldPineapples = true;
    public static boolean genOverworldBamboo = true;
    public static int minBambooPerPatchOverworld = GenRates.MIN_BAMBOO;
    public static int maxBambooPerPatchOverworld = GenRates.MAX_BAMBOO;
    public static int minBambooPerPatchTropics = GenRates.MIN_BAMBOO;
    public static int maxBambooPerPatchTropics = GenRates.MAX_BAMBOO;
    public static boolean allowVolcanoEruption = true;
    public static int bambooGenChanceOverworld = GenRates.BAMBOO_CHANCE;
    public static int tallFlowerGenChanceOverworld = GenRates.TALL_FLOWERS_CHANCE;
    
    public static boolean onlyGenFlowersInTropicalBiomes = true;

    public static int tropicsOceanID = 60;
    public static int tropicsID = 61;
    public static int rainforestPlainsID = 62;
    public static int rainforestHillsID = 63;
    public static int rainforestMountainsID = 64;
    public static int tropicsRiverID = 65;
    public static int tropicsBeachID = 66;
    public static int tropicsLakeID = 67;
    public static int islandMountainsID = 68;
    public static int rainforestThicknessAmount = 2;

    public static int tropicsDimensionID = -127;

    /* == Misc Configs == */

    public static String[] coconutBombWhitelist = {};
    public static boolean enableSwimAnimation = true;
    
    /* == Entity Configs == */
    public static boolean spawnPassiveTropicsOceanMobsOverworld = false;
    public static boolean spawnHostileTropicsOceanMobsOverworld = false;
    public static boolean spawnPassiveTropicsLandMobsOverworld = false;
    public static boolean spawnHostileTropicsLandMobsOverworld = false;
    
    /* == Donations == */
    public static String tiltifyAppToken = "";
    public static int tiltifyCampaign = 0;
    public static int donationTrackerRefreshRate = 10;
    public static int donationAmountPerMonument = 500;
    public static String tiltifyCommandRun = "function internaluseonly:addmonument";

    private static final String C_GENERATION = "generation";
    private static final String C_ENTITY = "entity";
    private static final String C_MISC = "misc";
    public static final String C_DONATIONS = "donations";

    private static Configuration config;

    public static void init(File file) {
        config = new Configuration(file);
        sync();

        for (String s : config.getCategoryNames()) {
            config.getCategory(s).setLanguageKey(Info.MODID + ".config." + s + ".title");
        }
    }

    public static void sync() {
        genOverworld = config.get(C_GENERATION, "genTropicraftInOverworld", genOverworld, "If false, no Tropicraft generation will occur at all in the overworld.").getBoolean();

        genOverworldPalms = config.get(C_GENERATION, "genPalmsInOverworld", genOverworldPalms).getBoolean();
        genOverworldPalmsBeachOnly = config.get(C_GENERATION, "genOverworldPalmsInBeachOnly", genOverworldPalmsBeachOnly).getBoolean();
        chancePalmOverworld = config.get(C_GENERATION, "palmChanceOfGenInOverworld", chancePalmOverworld).getInt();
        factorPalmOverworld = config.get(C_GENERATION, "palmPopulationFactorInOverworld", factorPalmOverworld).getInt();

        genOverworldEIH = config.get(C_GENERATION, "genTropicraftEIHInOverworld", genOverworldEIH).getBoolean();
        genOverworldFlowers = config.get(C_GENERATION, "genTropicraftFlowersInOverworld", genOverworldFlowers).getBoolean();
        genOverworldPineapples = config.get(C_GENERATION, "genPineapplesInOverworld", genOverworldPineapples).getBoolean();
        genOverworldBamboo = config.get(C_GENERATION, "genBambooInOverworld", genOverworldBamboo).getBoolean();
        minBambooPerPatchOverworld = config.getInt("minBambooPerPatchOverworld", C_GENERATION, minBambooPerPatchOverworld, 0, 1000, "Minimum amount of bamboo in a patch in the overworld");
        maxBambooPerPatchOverworld = config.getInt("maxBambooPerPatchOverworld", C_GENERATION, maxBambooPerPatchOverworld, minBambooPerPatchOverworld + 1, 1001, "Maximum amount of bamboo in a patch in the overworld");
        minBambooPerPatchTropics = config.getInt("minBambooPerPatchTropics", C_GENERATION, minBambooPerPatchTropics, 0, 1000, "Minimum amount of bamboo in a patch in the tropics");
        maxBambooPerPatchTropics = config.getInt("maxBambooPerPatchTropics", C_GENERATION, maxBambooPerPatchTropics, minBambooPerPatchTropics + 1, 1001, "Maximum amount of bamboo in a patch in the tropics");
        bambooGenChanceOverworld = config.getInt("bambooGenChanceOverworld", C_GENERATION, bambooGenChanceOverworld, 0, 1000, "Chance of bamboo spawn. Lower values = more common");
        tallFlowerGenChanceOverworld = config.getInt("tallFlowerGenChanceOverworld", C_GENERATION, tallFlowerGenChanceOverworld, 0, 1000, "Chance of pineapple/iris spawn. Lower values = more common");

        onlyGenFlowersInTropicalBiomes = config.get(C_GENERATION, "onlyGenFlowersInTropicalBiomesOverworld", onlyGenFlowersInTropicalBiomes, "Should Tropicraft flowers only generate in tropical biomes in the overworld?").getBoolean();
        
        coconutBombWhitelist = config.get(C_MISC, "coconutBombWhitelist", coconutBombWhitelist).getStringList();
        enableSwimAnimation = config.get(C_MISC, "enableSwimAnimation", enableSwimAnimation).getBoolean();

        allowVolcanoEruption = config.get(C_MISC, "allowVolcanoEruption", allowVolcanoEruption).getBoolean();

        tropicsDimensionID = config.get(C_MISC, "tropicsDimensionID", tropicsDimensionID).getInt();

        rainforestThicknessAmount = config.getInt(C_GENERATION, "rainforestThicknessAmount", rainforestThicknessAmount, 0, 3, "How thick should the trees in rainforest biomes be?");

        spawnPassiveTropicsOceanMobsOverworld = config.get(C_ENTITY, "spawnPassiveTropicsOceanMobsOverworld", spawnPassiveTropicsOceanMobsOverworld, "Should Tropicraft's passive water mobs spawn in the overworld?").getBoolean();
        spawnHostileTropicsOceanMobsOverworld = config.get(C_ENTITY, "spawnHostileTropicsOceanMobsOverworld", spawnHostileTropicsOceanMobsOverworld, "Should Tropicraft's hostile water mobs spawn in the overworld?").getBoolean();

        spawnPassiveTropicsLandMobsOverworld = config.get(C_ENTITY, "spawnPassiveTropicsLandMobsOverworld", spawnPassiveTropicsLandMobsOverworld, "Should Tropicraft's passive land mobs spawn in the overworld?").getBoolean();
        spawnHostileTropicsLandMobsOverworld = config.get(C_ENTITY, "spawnHostileTropicsLandMobsOverworld", spawnHostileTropicsLandMobsOverworld, "Should Tropicraft's hostile land mobs spawn in the overworld?").getBoolean();
        
        tiltifyAppToken = config.get(C_DONATIONS, "tiltifyAppToken", tiltifyAppToken, "Add a token here to enable donation tracking, leave blank to disable").getString();
        tiltifyCampaign = config.get(C_DONATIONS, "tiltifyCampaign", tiltifyCampaign, "The tiltify campaign to track donations from").getInt();
        donationTrackerRefreshRate = config.get(C_DONATIONS, "donationTrackerRefreshRate", donationTrackerRefreshRate, "How often the tracker checks for new donations, in seconds").getInt();
        tiltifyCommandRun = config.get(C_DONATIONS, "tiltifyCommandRun", tiltifyCommandRun, "Command run when donation comes in").getString();
        donationAmountPerMonument = config.get(C_DONATIONS, "donationAmountPerMonument", donationAmountPerMonument, "Amount of $ required per monument command run").getInt();
        
        config.save();
    }

    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Info.MODID)) {
            sync();
        }
    }

    public static Configuration getConfig() {
        return config;
    }
}
