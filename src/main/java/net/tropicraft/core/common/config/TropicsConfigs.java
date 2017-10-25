package net.tropicraft.core.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.Info;

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
        
        coconutBombWhitelist = config.get(C_MISC, "coconutBombWhitelist", coconutBombWhitelist).getStringList();
        
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
