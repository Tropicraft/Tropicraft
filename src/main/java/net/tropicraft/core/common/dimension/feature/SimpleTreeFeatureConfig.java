package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public record SimpleTreeFeatureConfig(Supplier<BlockState> log, Supplier<BlockState> leaves) implements FeatureConfiguration {
    public static final Codec<SimpleTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("log").forGetter(c -> c.log.get()),
            BlockState.CODEC.fieldOf("leaves").forGetter(c -> c.leaves.get())
    ).apply(instance, SimpleTreeFeatureConfig::new));

    public SimpleTreeFeatureConfig(BlockState log, BlockState leaves) {
        this(() -> log, () -> leaves);
    }
}
