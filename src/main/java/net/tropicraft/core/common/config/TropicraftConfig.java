package net.tropicraft.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class TropicraftConfig {
    public static ForgeConfigSpec.IntValue palmTreeDensityInOverworld;
    public static ForgeConfigSpec.BooleanValue generatePalmTreesInOverworld;
    public static ForgeConfigSpec.BooleanValue generatePalmTreesInOverworldBeachesOnly;
    public static ForgeConfigSpec.BooleanValue generateEIHInOverworld;
    public static ForgeConfigSpec.BooleanValue generateTropicraftFlowersInOverworld;
    public static ForgeConfigSpec.BooleanValue generatePineapplesInOverworld;

    public static ForgeConfigSpec.BooleanValue allowVolcanoEruption;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> coconutBombWhitelist;
}
