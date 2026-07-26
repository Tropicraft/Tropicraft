package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.function.Function;

/// @param smoothness Difference in cell-normalized distance units over which to blend cell values.
/// `0` always picks the nearest cell value, while `1` blends over differences of `1` unit of distance.
public record Voronoi(
        VoronoiGrid grid,
        float smoothness,
        int scanSize,
        DistanceMode distanceMode,
        DensityFunction value,
        PositionalRandomFactory jitterRandom
) implements BakeableDensityFunction {
    public static final MapCodec<Voronoi> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            VoronoiGrid.CODEC.forGetter(Voronoi::grid),
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("smoothness").forGetter(Voronoi::smoothness),
            ExtraCodecs.POSITIVE_INT.fieldOf("scan_size").forGetter(Voronoi::scanSize),
            DistanceMode.CODEC.fieldOf("distance").forGetter(Voronoi::distanceMode),
            DensityFunction.CODEC.fieldOf("value").forGetter(Voronoi::value)
    ).apply(i, Voronoi::new));

    public Voronoi(VoronoiGrid grid, float smoothness, int scanSize, DistanceMode distanceMode, DensityFunction value) {
        this(grid, smoothness, scanSize, distanceMode, value, RandomSource.create(0).forkPositional());
    }

    @Override
    public DensityFunction bake(Function<Identifier, PositionalRandomFactory> randomFactory) {
        return new Voronoi(grid, smoothness, scanSize, distanceMode, value, randomFactory.apply(grid.jitterSeed()));
    }

    @Override
    public double compute(FunctionContext context) {
        int blockX = context.blockX();
        int blockZ = context.blockZ();

        int cellSize = grid.cellSize();

        // TODO: Lift constants?
        // TODO: Specialise non-smooth implementation
        int scanMin = -scanSize / 2;
        int scanMax = scanMin + scanSize - 1;
        int scanCenterOffset = 0;
        if ((scanSize & 1) == 0) {
            scanCenterOffset = cellSize / 2;
        }

        float jitterScale = grid.jitterScale();

        int centerCellX = Mth.floorDiv(blockX + scanCenterOffset, cellSize);
        int centerCellZ = Mth.floorDiv(blockZ + scanCenterOffset, cellSize);
        float relativeX = (float) (blockX - centerCellX * cellSize) / cellSize;
        float relativeZ = (float) (blockZ - centerCellZ * cellSize) / cellSize;

        float outputValue = 0.0f;
        float outputDistance = -1.0f;

        for (int deltaZ = scanMin; deltaZ <= scanMax; deltaZ++) {
            for (int deltaX = scanMin; deltaX <= scanMax; deltaX++) {
                int cellX = centerCellX + deltaX;
                int cellZ = centerCellZ + deltaZ;

                RandomSource cellRandom = jitterRandom.at(cellX * cellSize, 0, cellZ * cellSize);
                float cellSeedOffsetX = cellRandom.nextFloat() * jitterScale;
                float cellSeedOffsetZ = cellRandom.nextFloat() * jitterScale;

                float cellDistance = computeCellDistance(deltaX + cellSeedOffsetX, deltaZ + cellSeedOffsetZ, relativeX, relativeZ);

                float factor;
                if (outputDistance == -1.0f) {
                    factor = 1.0f;
                } else {
                    // Applying a quadratic polynomial smooth-minimum by distance (thanks <https://iquilezles.org/articles/smin>!)
                    factor = smoothstep(0.5f + 0.5f * (outputDistance - cellDistance) / smoothness);
                }
                if (factor <= Mth.EPSILON) {
                    continue;
                }

                SinglePointContext cellContext = new SinglePointContext(
                        cellX * cellSize + Mth.floor(cellSeedOffsetX * cellSize),
                        0,
                        cellZ * cellSize + Mth.floor(cellSeedOffsetZ * cellSize)
                );
                float cellValue = (float) value.compute(cellContext);

                // Correction preserves gradient continuity
                float correction = smoothness * factor * (1.0f - factor);
                outputDistance = Mth.lerp(factor, outputDistance, cellDistance) - correction;
                outputValue = Mth.lerp(factor, outputValue, cellValue);
            }
        }

        return outputValue;
    }

    private float computeCellDistance(float cellSeedX, float cellSeedZ, float relativeX, float relativeZ) {
        float distanceSq = Mth.square(cellSeedX - relativeX) + Mth.square(cellSeedZ - relativeZ);
        return switch (distanceMode) {
            case EUCLIDEAN -> Mth.sqrt(distanceSq);
            case EUCLIDEAN_SQUARED -> distanceSq;
        };
    }

    private static float smoothstep(float x) {
        if (x <= 0.0f) {
            return 0.0f;
        } else if (x >= 1.0f) {
            return 1.0f;
        }
        return x * x * (3.0f - 2.0f * x);
    }

    @Override
    public void fillArray(double[] output, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(output, this);
    }

    @Override
    public DensityFunction mapChildren(Visitor visitor) {
        return new Voronoi(grid, smoothness, scanSize, distanceMode, visitor.apply(value), jitterRandom);
    }

    @Override
    public double minValue() {
        return value.minValue();
    }

    @Override
    public double maxValue() {
        return value.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<Voronoi> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }

    public enum DistanceMode implements StringRepresentable {
        EUCLIDEAN("euclidean"),
        EUCLIDEAN_SQUARED("euclidean_squared"),
        ;

        public static final Codec<DistanceMode> CODEC = StringRepresentable.fromEnum(DistanceMode::values);

        private final String name;

        DistanceMode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
