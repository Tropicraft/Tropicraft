package net.tropicraft.core.common.dimension.feature.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.world.gen.feature.IFeatureConfig;

public class RainforestVinesConfig implements IFeatureConfig {
    // TODO make home tree radius configurable
    public final int height;
    public final int xzSpread;
    public final int rollsPerY;
    
    public RainforestVinesConfig() {
        this(256, 4, 1);
    }

    public RainforestVinesConfig(final int height, final int xzSpread, final int rollsPerY) {
        this.height = height;
        this.xzSpread = xzSpread;
        this.rollsPerY = rollsPerY;
    }
    
    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> dynamicOps) {
        return new Dynamic<>(dynamicOps, dynamicOps.createMap(ImmutableMap.of(
                dynamicOps.createString("height"), dynamicOps.createInt(this.height),
                dynamicOps.createString("xzSpread"), dynamicOps.createInt(this.xzSpread),
                dynamicOps.createString("rollsPerY"), dynamicOps.createInt(this.rollsPerY)
        )));
    }

    public static <T> RainforestVinesConfig deserialize(Dynamic<T> dynamic) {
        final int height = dynamic.get("height").asInt(256);
        final int xzSpread = dynamic.get("xzSpread").asInt(4);
        final int rollsPerY = dynamic.get("rollsPerY").asInt(1);
        return new RainforestVinesConfig(height, xzSpread, rollsPerY);
    }
}
