/*
package net.tropicraft.core.common.dimension.biome.simulate;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.*;
import net.tropicraft.core.common.dimension.TropicraftTerrainProvider;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;

public class NoiseSimulationHelper {
    private static final double PRELIMINARY_SURFACE_THRESHOLD = 0.390625;

    private final NoiseGeneratorSettings noiseGeneratorSettings;
    private final RandomState randomState;
    private final CubicSpline<DensityFunctions.Spline.Point, DensityFunctions.Spline.Coordinate> offset;
    private final CubicSpline<DensityFunctions.Spline.Point, DensityFunctions.Spline.Coordinate> factor;

    public NoiseSimulationHelper(long seed) {
        noiseGeneratorSettings = TropicraftNoiseGenSettings.createNoise(false);
        RegistryAccess.Writable registries = RegistryAccess.builtinCopy();
        randomState = RandomState.create(noiseGeneratorSettings, registries.registryOrThrow(Registry.NOISE_REGISTRY), seed);

        offset = TropicraftTerrainProvider.offset(
                splineCoordinate(NoiseRouterData.CONTINENTS),
                splineCoordinate(NoiseRouterData.EROSION),
                splineCoordinate(NoiseRouterData.RIDGES_FOLDED)
        );
        factor = TropicraftTerrainProvider.factor(
                splineCoordinate(NoiseRouterData.CONTINENTS),
                splineCoordinate(NoiseRouterData.EROSION),
                splineCoordinate(NoiseRouterData.RIDGES),
                splineCoordinate(NoiseRouterData.RIDGES_FOLDED)
        );
    }

    private static DensityFunctions.Spline.Coordinate splineCoordinate(ResourceKey<DensityFunction> key) {
        return new DensityFunctions.Spline.Coordinate(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(key));
    }

    public Climate.TargetPoint sample(int x, int y, int z) {
        return randomState.sampler().sample(x, y, z);
    }

    public float offset(int x, int y, int z) {
        return offset.apply(new DensityFunctions.Spline.Point(new DensityFunction.SinglePointContext(x, y, z)));
    }

    public float factor(int x, int y, int z) {
        return factor.apply(new DensityFunctions.Spline.Point(new DensityFunction.SinglePointContext(x, y, z)));
    }

    public double peaksAndValleys(int x, int y, int z) {
        return TropicraftTerrainProvider.peaksAndValleys((float) randomState.sampler().weirdness().compute(new DensityFunction.SinglePointContext(x, y, z)));
    }

    public int prelimSurfaceLevel(int x, int z) {
        NoiseSettings settings = noiseGeneratorSettings.noiseSettings();
        int minY = settings.minY();
        for (int y = minY + settings.height(); y >= minY; y -= settings.getCellHeight()) {
            if (randomState.router().initialDensityWithoutJaggedness().compute(new DensityFunction.SinglePointContext(x, y, z)) > PRELIMINARY_SURFACE_THRESHOLD) {
                return y;
            }
        }
        return Integer.MAX_VALUE;
    }
}
*/
