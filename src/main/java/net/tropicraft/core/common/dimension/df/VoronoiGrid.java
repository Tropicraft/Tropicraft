package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public record VoronoiGrid(
        int cellSize,
        Identifier jitterSeed,
        float jitterScale
) {
    public static final MapCodec<VoronoiGrid> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("cell_size").forGetter(VoronoiGrid::cellSize),
            Identifier.CODEC.fieldOf("jitter_seed").forGetter(VoronoiGrid::jitterSeed),
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("jitter_scale").forGetter(VoronoiGrid::jitterScale)
    ).apply(i, VoronoiGrid::new));
}
