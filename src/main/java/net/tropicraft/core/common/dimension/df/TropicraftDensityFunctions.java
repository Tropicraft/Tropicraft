package net.tropicraft.core.common.dimension.df;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.BoundedFloatFunction;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.noise.TropicraftNoises;

import static net.minecraft.world.level.levelgen.DensityFunctions.*;

public interface TropicraftDensityFunctions {
    ResourceKey<DensityFunction> HIGH_FREQ_WARP_X = createKey("tropics/high_freq_warp_x");
    ResourceKey<DensityFunction> HIGH_FREQ_WARP_Z = createKey("tropics/high_freq_warp_z");

    ResourceKey<DensityFunction> CONTINENTS = createKey("tropics/continents");
    ResourceKey<DensityFunction> OFFSET = createKey("tropics/offset");

    ResourceKey<DensityFunction> FINAL_DENSITY = createKey("tropics/final_density");

    static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction highFreqWarpScale = constant(80.0);
        DensityFunction highFreqWarpX = register(context, HIGH_FREQ_WARP_X, cache2d(mul(noise2d(noises, TropicraftNoises.HIGH_FREQ_WARP_X), highFreqWarpScale)));
        DensityFunction highFreqWarpZ = register(context, HIGH_FREQ_WARP_Z, cache2d(mul(noise2d(noises, TropicraftNoises.HIGH_FREQ_WARP_Z), highFreqWarpScale)));

        DensityFunction continents = registerContinents(context, highFreqWarpX, highFreqWarpZ);

        DensityFunction offset = registerOffset(context, continents);

        register(context, FINAL_DENSITY, interpolated(tropicsSlide(offsetToDepth(offset))));
    }

    private static DensityFunction registerContinents(BootstrapContext<DensityFunction> context, DensityFunction highFreqWarpX, DensityFunction highFreqWarpZ) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction continentsWarpScale = constant(250.0);
        DensityFunction continents = cache2d(shiftedNoise2d(
                add(mul(noise2d(noises, TropicraftNoises.CONTINENT_WARP_X), continentsWarpScale), highFreqWarpX),
                add(mul(noise2d(noises, TropicraftNoises.CONTINENT_WARP_Z), continentsWarpScale), highFreqWarpZ),
                1.0,
                noises.getOrThrow(TropicraftNoises.CONTINENTS)
        ));

        return register(context, CONTINENTS, continents);
    }

    private static DensityFunction registerOffset(
            BootstrapContext<DensityFunction> context,
            DensityFunction continents
    ) {
        DensityFunction offset = spline(createOffsetSpline(new Spline.Coordinate(continents)));
        return register(context, OFFSET, flatCache(offset));
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createOffsetSpline(I continents) {
        float continentX = 0.15f;
        float oceanSlope = slope(-1.0f, -1.0f, continentX, 0.0f);
        return CubicSpline.builder(continents)
                .addPoint(-1.0f, -1.0f)
                .addPoint(continentX, 0.0f, oceanSlope)
                .addPoint(0.5f, 1.0f)
                .build();
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
