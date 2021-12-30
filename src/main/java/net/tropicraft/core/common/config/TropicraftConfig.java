package net.tropicraft.core.common.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.tropicraft.Tropicraft;

import java.util.List;
import java.util.UUID;

public class TropicraftConfig {
    public static ForgeConfigSpec.IntValue palmTreeDensityInOverworld;
    public static ForgeConfigSpec.BooleanValue generatePalmTreesInOverworld;
    public static ForgeConfigSpec.BooleanValue generatePalmTreesInOverworldBeachesOnly;
    public static ForgeConfigSpec.BooleanValue generateEIHInOverworld;
    public static ForgeConfigSpec.BooleanValue generateTropicraftFlowersInOverworld;
    public static ForgeConfigSpec.BooleanValue generatePineapplesInOverworld;

    public static ForgeConfigSpec.BooleanValue allowVolcanoEruption;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> coconutBombWhitelist;

    public static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment(" Welcome to the Tropicraft per-world configuration!");
        builder.push(" Overworld Generation Values");

        palmTreeDensityInOverworld = builder.comment(" Higher number = more palm trees generate closer together in the overworld")
            .defineInRange("palm_tree_density_overworld", 1, 1, 5);

        generatePalmTreesInOverworld = getBooleanConfig(builder,
                "Should Tropicraft palm trees generate in the overworld? NOTE: You need these to get to the tropics dimension",
                "generate_palm_trees_in_overworld",
                true);

        generatePalmTreesInOverworldBeachesOnly = getBooleanConfig(builder,
                "In the overworld, should Tropicraft palms only generate in beach biomes?",
                "generate_palm_trees_in_overworld_beaches_only",
                false);

        generateEIHInOverworld = getBooleanConfig(builder,
                "In the overworld, should Easter Island Head statues generate?",
                "generate_eih_in_overworld",
                false);

        generateTropicraftFlowersInOverworld = getBooleanConfig(builder,
                "In the overworld, should Tropicraft flowers generate?",
                "generate_tropicraft_flowers_in_overworld",
                false);

        generatePineapplesInOverworld = getBooleanConfig(builder,
                "In the overworld, should pineapples generate? NOTE: You need these to get to the tropics dimension",
                "generate_pineapples_in_overworld",
                true);

        builder.pop();
        builder.push(" Behavior settings");
        allowVolcanoEruption = getBooleanConfig(builder,
                "Should Tropicraft volanoes erupt, spewing lava everywhere over the land?",
                "allow_volcano_eruption",
              true);

        builder.pop();
        builder.push(" User-specific settings");

        coconutBombWhitelist = builder.comment(" List of UUIDs (not usernames) of users that can use coconut bombs. These are DANGEROUS and EXPLOSIVE so give this power out wisely. NOTE: Make sure you use the version of UUID that has dashes")
            .defineList("coconut_bomb_whitelist", Lists.newArrayList(""), entry -> {
                if (!(entry instanceof String)) {
                    return false;
                }
                try {
                    UUID.fromString((String) entry);
                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            });
    }

    private static ForgeConfigSpec.BooleanValue getBooleanConfig(final ForgeConfigSpec.Builder builder, final String comment, final String id, final boolean value) {
        return builder.comment(" " + comment).define(id, value);
    }
}
