package net.tropicraft.core.common.dimension.df;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.BoundedFloatFunction;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.noise.TropicraftNoises;

import static net.minecraft.world.level.levelgen.DensityFunctions.*;

public interface TropicraftDensityFunctions {
    ResourceKey<DensityFunction> CONTINENT_WARP_X = createKey("tropics/continent_warp_x");
    ResourceKey<DensityFunction> CONTINENT_WARP_Z = createKey("tropics/continent_warp_z");

    ResourceKey<DensityFunction> HIGH_FREQ_WARP_X = createKey("tropics/high_freq_warp_x");
    ResourceKey<DensityFunction> HIGH_FREQ_WARP_Z = createKey("tropics/high_freq_warp_z");

    ResourceKey<DensityFunction> CONTINENTS = createKey("tropics/continents");

    ResourceKey<DensityFunction> EROSION = createKey("tropics/erosion");

    ResourceKey<DensityFunction> OFFSET = createKey("tropics/offset");

    ResourceKey<DensityFunction> FINAL_DENSITY = createKey("tropics/final_density");

    float CONTINENTS_SHALLOW_OCEAN = 0.1f;
    float CONTINENTS_BEACH_START = 0.15f;
    float CONTINENTS_BEACH_END = 0.2f;
    float CONTINENTS_MAX = 0.6f;

    static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction continentsWarpScale = constant(250.0);
        DensityFunction continentWarpX = register(context, CONTINENT_WARP_X, mul(noise2d(noises, TropicraftNoises.CONTINENT_WARP_X), continentsWarpScale));
        DensityFunction continentWarpZ = register(context, CONTINENT_WARP_Z, mul(noise2d(noises, TropicraftNoises.CONTINENT_WARP_Z), continentsWarpScale));

        DensityFunction highFreqWarpScale = constant(80.0);
        DensityFunction highFreqWarpX = register(context, HIGH_FREQ_WARP_X, flatCache(mul(noise2d(noises, TropicraftNoises.HIGH_FREQ_WARP_X), highFreqWarpScale)));
        DensityFunction highFreqWarpZ = register(context, HIGH_FREQ_WARP_Z, flatCache(mul(noise2d(noises, TropicraftNoises.HIGH_FREQ_WARP_Z), highFreqWarpScale)));

        DensityFunction erosion = register(context, EROSION, shiftedNoise2d(highFreqWarpX, highFreqWarpZ, 1.0, noises.getOrThrow(TropicraftNoises.EROSION)));

        DensityFunction continents = registerContinents(context, add(continentWarpX, highFreqWarpX), add(continentWarpZ, highFreqWarpZ));

        DensityFunction offset = registerOffset(context, continents, erosion);

        register(context, FINAL_DENSITY, interpolated(tropicsSlide(offsetToDepth(offset))));
    }

    private static DensityFunction registerContinents(BootstrapContext<DensityFunction> context, DensityFunction continentWarpX, DensityFunction continentWarpZ) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction continents = cache2d(shiftedNoise2d(
                continentWarpX,
                continentWarpZ,
                1.0,
                noises.getOrThrow(TropicraftNoises.CONTINENTS)
        ));

        return register(context, CONTINENTS, continents);
    }

    private static DensityFunction registerOffset(
            BootstrapContext<DensityFunction> context,
            DensityFunction continents,
            DensityFunction erosion
    ) {
        DensityFunction offset = spline(createOffsetSpline(new Spline.Coordinate(continents), new Spline.Coordinate(erosion)));
        return register(context, OFFSET, flatCache(offset));
    }

    // TODO: Needs to be iterated upon a lot more :)
    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createOffsetSpline(I continents, I erosion) {
        // TODO: Inland lakes?
        return CubicSpline.builder(erosion)
                .addPoint(-0.1f, createLowlandsOffset(continents))
                .addPoint(0.3f, createMidlandsOffset(continents))
                .addPoint(0.5f, createHighlandsOffset(continents))
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createLowlandsOffset(I continents) {
        CubicSpline.Builder<I> spline = addOceanOffset(CubicSpline.builder(continents));
        float beachEndOffset = 0.01f;
        float maxOffset = 0.4f;
        return spline
                .addPoint(CONTINENTS_BEACH_START, 0.0f)
                .addPoint(CONTINENTS_BEACH_END, beachEndOffset, slope(CONTINENTS_BEACH_END, beachEndOffset, CONTINENTS_MAX, maxOffset))
                .addPoint(CONTINENTS_MAX, maxOffset)
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createMidlandsOffset(I continents) {
        CubicSpline.Builder<I> spline = addOceanOffset(CubicSpline.builder(continents));
        float beachEndOffset = 0.01f;
        float maxOffset = 1.0f;
        return spline
                .addPoint(CONTINENTS_BEACH_START, 0.0f)
                .addPoint(CONTINENTS_BEACH_END, beachEndOffset, 0.1f)
                .addPoint(CONTINENTS_MAX, maxOffset)
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createHighlandsOffset(I continents) {
        CubicSpline.Builder<I> spline = addOceanOffset(CubicSpline.builder(continents));
        float maxOffset = 1.5f;
        return spline
                .addPoint(CONTINENTS_BEACH_START, 0.0f, slope(CONTINENTS_BEACH_START, 0.0f, CONTINENTS_MAX, maxOffset))
                .addPoint(CONTINENTS_MAX, maxOffset)
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline.Builder<I> addOceanOffset(CubicSpline.Builder<I> spline) {
        return spline
                .addPoint(-1.0f, -1.0f)
                .addPoint(-0.4f, Mth.map(-0.4f, -1.0f, CONTINENTS_BEACH_START, -1.0f, 0.0f))
                .addPoint(CONTINENTS_SHALLOW_OCEAN, Mth.map(CONTINENTS_SHALLOW_OCEAN, -1.0f, CONTINENTS_BEACH_START, -1.0f, 0.0f));
    }

    private static float slope(float x1, float y1, float x2, float y2) {
        return (y2 - y1) / (x2 - x1);
    }

    static NoiseRouter tropics(HolderGetter<DensityFunction> functions) {
        return new NoiseRouter(
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                zero(),
                getOrThrow(functions, FINAL_DENSITY),
                zero(),
                zero(),
                zero()
        );
    }

    private static DensityFunction noise2d(HolderGetter<NormalNoise.NoiseParameters> noises, ResourceKey<NormalNoise.NoiseParameters> id) {
        return noise(noises.getOrThrow(id), 1.0, 0.0);
    }

    private static DensityFunction offsetToDepth(DensityFunction offset) {
        return add(yClampedGradient(-64, 318, 1.5, -1.5), offset);
    }

    private static DensityFunction tropicsSlide(DensityFunction function) {
        return slide(function, -64, 384, 80, 64, -0.078125, 0, 24, 0.1171875);
    }

    private static DensityFunction slide(DensityFunction function, int minY, int height, int topSliderLowerOffset, int topSlideUpperOffset, double topSlideTarget, int bottomSlideLowerOffset, int bottomSlideUpperOffset, double bottomSlideTarget) {
        DensityFunction topSlideFactor = yClampedGradient(minY + height - topSliderLowerOffset, minY + height - topSlideUpperOffset, 1.0, 0.0);
        DensityFunction bottomSlideFactor = yClampedGradient(minY + bottomSlideLowerOffset, minY + bottomSlideUpperOffset, 0.0, 1.0);
        return lerp(bottomSlideFactor, bottomSlideTarget, lerp(topSlideFactor, topSlideTarget, function));
    }

    private static DensityFunction getOrThrow(HolderGetter<DensityFunction> functions, ResourceKey<DensityFunction> key) {
        return wrap(functions.getOrThrow(key));
    }

    private static DensityFunction register(BootstrapContext<DensityFunction> context, ResourceKey<DensityFunction> key, DensityFunction function) {
        return wrap(context.register(key, function));
    }

    private static DensityFunctions.HolderHolder wrap(Holder.Reference<DensityFunction> holder) {
        return new DensityFunctions.HolderHolder(holder);
    }

    private static ResourceKey<DensityFunction> createKey(String name) {
        return Tropicraft.resourceKey(Registries.DENSITY_FUNCTION, name);
    }
}
