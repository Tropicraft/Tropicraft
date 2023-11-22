package net.tropicraft.core.common;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class TropicsConfigs {

    //TODO: 1.14 PLACEHOLDER CONFIG FILE
    public static int tropicsDimensionID = -127;
    public static boolean allowVolcanoEruption = false;

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> allowExplodingCoconutsByNonOPs;
        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Items");
            allowExplodingCoconutsByNonOPs = builder
                    .comment("Should non OPs be allowed to throw exploding coconuts?")
                    .translation("config.tropicraft.common.items.allow_exploding_coconuts")
                    .define("ALlow exploding coconuts to be used by non-ops/creative mode players?", false);
            builder.pop();
        }
    }

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
