package net.tropicraft.core.common.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.UUID;

public class TropicraftConfig {
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> portalEnchanterWhitelist;

    public static void setupConfig(ForgeConfigSpec.Builder builder){
        builder.push(" User-specific settings");
//        builder.pop();

        portalEnchanterWhitelist = builder.comment(" List of UUIDs (not usernames) of users that can use the Portal Enchanter. This tool has the ability to create portal's any where the player right clicks. NOTE: Make sure you use the version of UUID that has dashes")
                .defineList("portal_enchanter_whitelist", Lists.newArrayList(""), entry -> {
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
}
