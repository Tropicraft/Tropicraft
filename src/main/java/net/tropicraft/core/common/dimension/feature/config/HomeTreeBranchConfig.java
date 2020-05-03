package net.tropicraft.core.common.dimension.feature.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class HomeTreeBranchConfig implements IFeatureConfig {
    // TODO make home tree radius configurable
    public final float minAngle;
    public final float maxAngle;

    public HomeTreeBranchConfig(final float minAngle, final float maxAngle) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }
    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> dynamicOps) {
        return new Dynamic(dynamicOps, dynamicOps.createMap(ImmutableMap.of(
                dynamicOps.createString("minAngle"), dynamicOps.createFloat(this.minAngle),
                dynamicOps.createString("maxAngle"), dynamicOps.createFloat(this.maxAngle)
        )));
    }

    public static <T> HomeTreeBranchConfig deserialize(Dynamic<T> dynamic) {
        final float minA = dynamic.get("minAngle").asFloat(0.0F);
        final float maxA = dynamic.get("maxAngle").asFloat(0.0F);
        return new HomeTreeBranchConfig(minA, maxA);
    }
}
