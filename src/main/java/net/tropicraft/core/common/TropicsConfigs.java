package net.tropicraft.core.common;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class TropicsConfigs {

    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    public static class Common {
        public final ModConfigSpec.ConfigValue<Boolean> allowExplodingCoconutsByNonOPs;
        public final ModConfigSpec.ConfigValue<Boolean> spawnHostileMobsInTropics;
        public final ModConfigSpec.ConfigValue<Boolean> allowActiveVolcano;

        public Common(ModConfigSpec.Builder builder) {
            builder.push("Items");
            allowExplodingCoconutsByNonOPs = builder
                    .comment("Should non OPs be allowed to throw exploding coconuts?")
                    .translation("config.tropicraft.common.items.allow_exploding_coconuts")
                    .define("Allow exploding coconuts to be used by non-ops/creative mode players?", false);
            builder.pop();
            builder.push("Mobs");
            spawnHostileMobsInTropics = builder
                    .comment("Should hostile mobs spawn in the tropics?")
                    .translation("config.tropicraft.common.mobs.allow_hostiles")
                    .define("Should hostile mobs spawn in the Tropics dimension?", false);
            builder.pop();
            builder.push("Misc");
            allowActiveVolcano = builder
                    .comment("Should volcano active in the tropics?")
                    .translation("config.tropicraft.common.mobs.allow_active_volcano")
                    .define("Should volcano active the Tropics dimension?", true);
            builder.pop();
        }
    }

    static {
        Pair<Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
