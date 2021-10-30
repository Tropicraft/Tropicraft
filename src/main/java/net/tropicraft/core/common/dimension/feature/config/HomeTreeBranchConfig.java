package net.tropicraft.core.common.dimension.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class HomeTreeBranchConfig implements FeatureConfiguration {
    public static final Codec<HomeTreeBranchConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.FLOAT.fieldOf("min_angle").forGetter(c -> c.minAngle),
                Codec.FLOAT.fieldOf("max_angle").forGetter(c -> c.maxAngle)
        ).apply(instance, HomeTreeBranchConfig::new);
    });

    // TODO make home tree radius configurable
    public final float minAngle;
    public final float maxAngle;

    public HomeTreeBranchConfig(final float minAngle, final float maxAngle) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }
}
