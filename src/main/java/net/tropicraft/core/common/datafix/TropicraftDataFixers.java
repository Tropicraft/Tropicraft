package net.tropicraft.core.common.datafix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.AbstractBlockPropertyFix;

import java.util.Set;

public class TropicraftDataFixers {
    private static final Set<String> PERSISTENT_LEAVES = Set.of("tropicraft:mahogany_leaves", "tropicraft:palm_leaves", "tropicraft:kapok_leaves");

    public static void injectFixers(Builder builder) {
        Schema v4424 = builder.getSchema(4424, 0);
        builder.injectFixer(new AbstractBlockPropertyFix(v4424, "TropicraftFixPersistentLeaves") {
            @Override
            protected boolean shouldFix(String name) {
                return PERSISTENT_LEAVES.contains(name);
            }

            @Override
            protected <T> Dynamic<T> fixProperties(String name, Dynamic<T> properties) {
                boolean newDecay = properties.get("new_decay").asBoolean(false);
                properties = properties.remove("new_decay");
                if (!newDecay) {
                    return properties.set("persistent", properties.createBoolean(true));
                }
                return properties;
            }
        });
    }

    public interface Builder {
        void injectFixer(DataFix fix);

        Schema getSchema(int version, int subVersion);
    }
}
