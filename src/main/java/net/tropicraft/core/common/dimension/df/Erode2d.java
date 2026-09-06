package net.tropicraft.core.common.dimension.df;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.function.Function;

// Based on technique proposed by <https://blog.runevision.com/2026/03/fast-and-gorgeous-erosion-filter.html>
public record Erode2d(
        DensityFunction input,
        Identifier jitterSeed,
        int cellSize,
        float sharpness,
        float amplitude,
        float persistence,
        float lacunarity,
        int iterations,
        PositionalRandomFactory jitterRandom
) implements BakeableDensityFunction {
    public static final MapCodec<Erode2d> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            DensityFunction.CODEC.fieldOf("input").forGetter(Erode2d::input),
            Identifier.CODEC.fieldOf("jitter_seed").forGetter(Erode2d::jitterSeed),
            ExtraCodecs.POSITIVE_INT.fieldOf("cell_size").forGetter(Erode2d::cellSize),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("sharpness").forGetter(Erode2d::sharpness),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("amplitude").forGetter(Erode2d::amplitude),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("persistence").forGetter(Erode2d::persistence),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("lacunarity").forGetter(Erode2d::lacunarity),
            Codec.intRange(0, 10).fieldOf("iterations").forGetter(Erode2d::iterations)
    ).apply(i, Erode2d::new));

    public Erode2d(
            DensityFunction input,
            Identifier jitterSeed,
            int cellSize,
            float sharpness,
            float amplitude,
            float persistence,
            float lacunarity,
            int iterations
    ) {
        this(input, jitterSeed, cellSize, sharpness, amplitude, persistence, lacunarity, iterations, RandomSource.create(0L).forkPositional());
    }

    @Override
    public DensityFunction bake(Function<Identifier, PositionalRandomFactory> randomFactory) {
        return new Erode2d(input, jitterSeed, cellSize, sharpness, amplitude, persistence, lacunarity, iterations, randomFactory.apply(jitterSeed));
    }

    @Override
    public double compute(FunctionContext context) {
        int blockX = context.blockX();
        int blockY = context.blockY();
        int blockZ = context.blockZ();

        double inputValue = input.compute(context);
        double gradientX = input.compute(new SinglePointContext(blockX + 1, blockY, blockZ)) - inputValue;
        double gradientZ = input.compute(new SinglePointContext(blockX, blockY, blockZ + 1)) - inputValue;

        Gradient cellRidgeGradient = new Gradient();
        float cellSize = this.cellSize;
        float amplitude = this.amplitude;

        for (int iteration = 0; iteration < iterations; iteration++) {
            double gradientLength = Mth.length(gradientX, gradientZ);
            if (gradientLength == 0.0) {
                break;
            }
            double gradientNorm = 1.0 / gradientLength;
            double ridgeDirectionX = -gradientZ * gradientNorm;
            double ridgeDirectionZ = gradientX * gradientNorm;

            double centerCellXD = (double) blockX / cellSize;
            double centerCellZD = (double) blockZ / cellSize;
            int centerCellX = Mth.floor(centerCellXD);
            int centerCellZ = Mth.floor(centerCellZD);
            float cellRelativeX = (float) (centerCellXD - centerCellX);
            float cellRelativeZ = (float) (centerCellZD - centerCellZ);

            float outputRidge = 0.0f;
            float outputDistance = -1.0f;
            float outputRidgeGradientX = 0.0f;
            float outputRidgeGradientZ = 0.0f;

            float ridgeWidth = cellSize / sharpness;

            for (int cellDeltaZ = -1; cellDeltaZ <= 1; cellDeltaZ++) {
                for (int cellDeltaX = -1; cellDeltaX <= 1; cellDeltaX++) {
                    int cellX = centerCellX + cellDeltaX;
                    int cellZ = centerCellZ + cellDeltaZ;

                    RandomSource cellRandom = jitterRandom.at(cellX, 0, cellZ);
                    float cellSeedX = cellRandom.nextFloat() * 0.8f;
                    float cellSeedZ = cellRandom.nextFloat() * 0.8f;

                    float cellSeedDeltaX = cellDeltaX + cellSeedX - cellRelativeX;
                    float cellSeedDeltaZ = cellDeltaZ + cellSeedZ - cellRelativeZ;
                    float cellDistance = Voronoi.DistanceMode.EUCLIDEAN_SQUARED.compute(cellSeedDeltaX, cellSeedDeltaZ);

                    float factor;
                    if (outputDistance == -1.0f) {
                        factor = 1.0f;
                    } else {
                        factor = Voronoi.smoothstep(0.5f + 0.5f * (outputDistance - cellDistance));
                    }
                    if (factor <= Mth.EPSILON) {
                        continue;
                    }

                    float cellRidge = computeRidge(
                            cellSeedDeltaX * cellSize,
                            cellSeedDeltaZ * cellSize,
                            ridgeWidth,
                            ridgeDirectionX,
                            ridgeDirectionZ,
                            cellRidgeGradient
                    );

                    float correction = factor * (1.0f - factor);
                    outputDistance = Mth.lerp(factor, outputDistance, cellDistance) - correction;
                    outputRidge = Mth.lerp(factor, outputRidge, cellRidge);
                    outputRidgeGradientX = Mth.lerp(factor, outputRidgeGradientX, cellRidgeGradient.x);
                    outputRidgeGradientZ = Mth.lerp(factor, outputRidgeGradientZ, cellRidgeGradient.z);
                }
            }

            float ridgeFactor = (float) gradientLength * amplitude;
            inputValue += outputRidge * ridgeFactor;
            gradientX += outputRidgeGradientX * ridgeFactor;
            gradientZ += outputRidgeGradientZ * ridgeFactor;

            cellSize /= lacunarity;
            amplitude *= persistence;
        }

        return inputValue;
    }

    private static float computeRidge(float x, float z, double width, double directionX, double directionZ, Gradient gradientOutput) {
        double dot = x * directionX + z * directionZ;

        double frequency = Math.TAU / width;
        double ridgeX = Math.clamp(dot * frequency, -Math.PI, Math.PI);

        double value = (Math.cos(ridgeX) - 1.0) * 0.5;
        double gradient = -Math.sin(ridgeX) * frequency * 0.5;
        gradientOutput.x = (float) (gradient * directionX);
        gradientOutput.z = (float) (gradient * directionZ);

        return (float) value;
    }

    @Override
    public void fillArray(double[] output, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(output, this);
    }

    @Override
    public DensityFunction mapChildren(Visitor visitor) {
        return new Erode2d(visitor.apply(input), jitterSeed, cellSize, sharpness, amplitude, persistence, lacunarity, iterations, jitterRandom);
    }

    @Override
    public double minValue() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        return input.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<Erode2d> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }

    private static class Gradient {
        public float x;
        public float z;
    }
}
