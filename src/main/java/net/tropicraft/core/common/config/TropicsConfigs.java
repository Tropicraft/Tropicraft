package net.tropicraft.core.common.config;

import java.io.File;

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

    private static final String C_GENERATION = "generation";
    private static final String C_MISC = "misc";

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
        minBambooPerPatchOverworld = config.get(C_GENERATION, "minBambooPerPatchOverworld", minBambooPerPatchOverworld).getInt();
        maxBambooPerPatchOverworld = config.get(C_GENERATION, "maxBambooPerPatchOverworld", maxBambooPerPatchOverworld).getInt();
        minBambooPerPatchTropics = config.get(C_GENERATION, "minBambooPerPatchTropics", minBambooPerPatchTropics).getInt();
        maxBambooPerPatchTropics = config.get(C_GENERATION, "maxBambooPerPatchTropics", maxBambooPerPatchTropics).getInt();
        bambooGenChanceOverworld = config.getInt(C_GENERATION, "bambooGenChanceOverworld", bambooGenChanceOverworld, 0, 1000, "Chance of bamboo spawn. Lower values = more common");
        tallFlowerGenChanceOverworld = config.getInt(C_GENERATION, "tallFlowerGenChanceOverworld", tallFlowerGenChanceOverworld, 0, 1000, "Chance of pineapple/iris spawn. Lower values = more common");

        coconutBombWhitelist = config.get(C_MISC, "coconutBombWhitelist", coconutBombWhitelist).getStringList();

        allowVolcanoEruption = config.get(C_MISC, "allowVolcanoEruption", allowVolcanoEruption).getBoolean();

        tropicsDimensionID = config.get(C_MISC, "tropicsDimensionID", tropicsDimensionID).getInt();

        rainforestThicknessAmount = config.getInt(C_GENERATION, "rainforestThicknessAmount", rainforestThicknessAmount, 0, 3, "How thick should the trees in rainforest biomes be?");

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
