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

    ResourceKey<DensityFunction> ISLAND_MASK = createKey("tropics/island_mask");
    ResourceKey<DensityFunction> ISLANDS = createKey("tropics/islands");
    ResourceKey<DensityFunction> ISLANDS_RAW = createKey("tropics/islands_raw");
    ResourceKey<DensityFunction> ISLANDS_SMOOTH = createKey("tropics/islands_smooth");

    ResourceKey<DensityFunction> EROSION = createKey("tropics/erosion");

    ResourceKey<DensityFunction> OFFSET = createKey("tropics/offset");

    ResourceKey<DensityFunction> FINAL_DENSITY = createKey("tropics/final_density");

    float CONTINENTS_DEEP_OCEAN = -0.3f;
    float CONTINENTS_SHALLOW_OCEAN = 0.1f;
    float CONTINENTS_BEACH_START = 0.15f;
    float CONTINENTS_BEACH_END = 0.2f;
    float CONTINENTS_MAX = 0.6f;

    VoronoiGrid ISLANDS_GRID = new VoronoiGrid(
            80,
            Tropicraft.id("island_jitter"),
            0.75f
    );

    static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction continentsWarpScale = constant(250.0);
        DensityFunction continentWarpX = register(context, CONTINENT_WARP_X, mul(noise2d(noises, TropicraftNoises.CONTINENT_WARP_X), continentsWarpScale));
        DensityFunction continentWarpZ = register(context, CONTINENT_WARP_Z, mul(noise2d(noises, TropicraftNoises.CONTINENT_WARP_Z), continentsWarpScale));

        DensityFunction highFreqWarpScale = constant(80.0);
        DensityFunction highFreqWarpX = register(context, HIGH_FREQ_WARP_X, flatCache(mul(noise2d(noises, TropicraftNoises.HIGH_FREQ_WARP_X), highFreqWarpScale)));
        DensityFunction highFreqWarpZ = register(context, HIGH_FREQ_WARP_Z, flatCache(mul(noise2d(noises, TropicraftNoises.HIGH_FREQ_WARP_Z), highFreqWarpScale)));

        DensityFunction erosion = register(context, EROSION, shiftedNoise2d(highFreqWarpX, highFreqWarpZ, 1.0, noises.getOrThrow(TropicraftNoises.EROSION)));

        DensityFunction continents = registerContinents(context, continentWarpX, continentWarpZ, highFreqWarpX, highFreqWarpZ);

        double islandMaskRange = 0.4;
        DensityFunction islandMask = register(context, ISLAND_MASK, cache2d(noise2d(noises, TropicraftNoises.ISLAND_MASK)));
        DensityFunction islandsRaw = registerIslandsRaw(context, islandMask, islandMaskRange);
        DensityFunction islands = registerIslands(context, islandsRaw, highFreqWarpX, highFreqWarpZ);
        DensityFunction islandsSmooth = registerIslandsSmooth(context, islandsRaw, highFreqWarpX, highFreqWarpZ);

        DensityFunction offset = registerOffset(context, continents, islands, islandsSmooth, erosion);

        register(context, FINAL_DENSITY, interpolated(tropicsSlide(offsetToDepth(offset))));
    }

    private static DensityFunction registerContinents(BootstrapContext<DensityFunction> context, DensityFunction continentWarpX, DensityFunction continentWarpZ, DensityFunction highFreqWarpX, DensityFunction highFreqWarpZ) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction continents = shiftedNoise2d(
                continentWarpX,
                continentWarpZ,
                1.0,
                noises.getOrThrow(TropicraftNoises.CONTINENTS)
        );
        continents = applyShelves(continents, noises);
        continents = new DomainWarp(
                cache2d(continents),
                highFreqWarpX,
                zero(),
                highFreqWarpZ
        );

        return register(context, CONTINENTS, cache2d(continents));
    }

    // TODO: We might want to make these 3D and fall off as they get lower?
    private static DensityFunction applyShelves(DensityFunction continents, HolderGetter<NormalNoise.NoiseParameters> noises) {
        VoronoiGrid shelfGrid = new VoronoiGrid(80, Tropicraft.id("shelf_jitter"), 0.9f);
        return max(continents, new Voronoi(
                shelfGrid,
                0.5f,
                5,
                Voronoi.DistanceMode.EUCLIDEAN_SQUARED,
                rangeChoice(
                        noise2d(noises, TropicraftNoises.SHELFINESS),
                        0.0, 100.0,
                        continents,
                        constant(-1.0)
                )
        ));
    }

    private static DensityFunction registerIslandsRaw(BootstrapContext<DensityFunction> context, DensityFunction islandMask, double islandMaskRange) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction islands = cache2d(noise2d(noises, TropicraftNoises.ISLANDS));
        islands = rangeChoice(
                islands,
                0.3, 100.0,
                islands,
                constant(-1.0)
        );
        islands = rangeChoice(
                islandMask,
                -islandMaskRange, islandMaskRange,
                islands,
                constant(-1.0)
        );

        return register(context, ISLANDS_RAW, cache2d(islands));
    }

    private static DensityFunction registerIslands(BootstrapContext<DensityFunction> context, DensityFunction islandsRaw, DensityFunction highFreqWarpX, DensityFunction highFreqWarpZ) {
        DensityFunction islands = new Voronoi(
                ISLANDS_GRID,
                0.5f,
                3,
                Voronoi.DistanceMode.EUCLIDEAN_SQUARED,
                islandsRaw
        );
        islands = new DomainWarp(islands, highFreqWarpX, zero(), highFreqWarpZ);
        return register(context, ISLANDS, islands);
    }

    private static DensityFunction registerIslandsSmooth(BootstrapContext<DensityFunction> context, DensityFunction islandsRaw, DensityFunction highFreqWarpX, DensityFunction highFreqWarpZ) {
        DensityFunction islandsSmooth = new Voronoi(
                ISLANDS_GRID,
                5.0f,
                6,
                Voronoi.DistanceMode.EUCLIDEAN_SQUARED,
                rangeChoice(
                        islandsRaw,
                        0.0, 100.0,
                        constant(1.0),
                        constant(-1.0)
                )
        );
        islandsSmooth = new DomainWarp(islandsSmooth, highFreqWarpX, zero(), highFreqWarpZ);
        return register(context, ISLANDS_SMOOTH, islandsSmooth);
    }

    private static DensityFunction registerOffset(
            BootstrapContext<DensityFunction> context,
            DensityFunction continents,
            DensityFunction islands,
            DensityFunction islandsSmooth,
            DensityFunction erosion
    ) {
        // TODO: Improve on this somehow? It looks quite flat.
        DensityFunction islandFactor = clampedRemap(islandsSmooth, -1.0, -0.5, 0.0, 1.0);
        continents = lerp(islandFactor, continents, max(constant(-0.05), continents));

        DensityFunction offset = spline(createOffsetSpline(new Spline.Coordinate(continents), new Spline.Coordinate(islands), new Spline.Coordinate(erosion)));
        return register(context, OFFSET, flatCache(offset));
    }

    // TODO: Needs to be iterated upon a lot more :)
    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createOffsetSpline(I continents, I islands, I erosion) {
        // TODO: Inland lakes?
        return CubicSpline.builder(erosion)
                .addPoint(-0.1f, createLowlandsOffset(continents, islands))
                .addPoint(0.3f, createMidlandsOffset(continents, islands))
                .addPoint(0.5f, createHighlandsOffset(continents, islands))
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createLowlandsOffset(I continents, I islands) {
        CubicSpline.Builder<I> spline = addOceanAndIslandOffset(CubicSpline.builder(continents), islands, 0.1f);
        float beachEndOffset = 0.01f;
        float maxOffset = 0.4f;
        return spline
                .addPoint(CONTINENTS_BEACH_START, 0.0f)
                .addPoint(CONTINENTS_BEACH_END, beachEndOffset, slope(CONTINENTS_BEACH_END, beachEndOffset, CONTINENTS_MAX, maxOffset))
                .addPoint(CONTINENTS_MAX, maxOffset)
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createMidlandsOffset(I continents, I islands) {
        CubicSpline.Builder<I> spline = addOceanAndIslandOffset(CubicSpline.builder(continents), islands, 0.25f);
        float beachEndOffset = 0.01f;
        float maxOffset = 1.0f;
        return spline
                .addPoint(CONTINENTS_BEACH_START, 0.0f)
                .addPoint(CONTINENTS_BEACH_END, beachEndOffset, 0.1f)
                .addPoint(CONTINENTS_MAX, maxOffset)
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createHighlandsOffset(I continents, I islands) {
        CubicSpline.Builder<I> spline = addOceanAndIslandOffset(CubicSpline.builder(continents), islands, 0.3f);
        float maxOffset = 1.5f;
        return spline
                .addPoint(CONTINENTS_BEACH_START, 0.0f, slope(CONTINENTS_BEACH_START, 0.0f, CONTINENTS_MAX, maxOffset))
                .addPoint(CONTINENTS_MAX, maxOffset)
                .build();
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline.Builder<I> addOceanAndIslandOffset(CubicSpline.Builder<I> spline, I islands, float islandScale) {
        return spline
                .addPoint(-1.0f, -1.0f)
                .addPoint(-0.4f, Mth.map(-0.4f, -1.0f, CONTINENTS_BEACH_START, -1.0f, 0.0f))
                .addPoint(CONTINENTS_DEEP_OCEAN, createIslandOffset(islands, Mth.map(CONTINENTS_DEEP_OCEAN, -1.0f, CONTINENTS_BEACH_START, -1.0f, 0.0f), islandScale))
                .addPoint(CONTINENTS_SHALLOW_OCEAN, createIslandOffset(islands, Mth.map(CONTINENTS_SHALLOW_OCEAN, -1.0f, CONTINENTS_BEACH_START, -1.0f, 0.0f), islandScale));
    }

    private static <I extends BoundedFloatFunction<?>> CubicSpline<I> createIslandOffset(I islands, float oceanOffset, float islandScale) {
        return CubicSpline.builder(islands)
                .addPoint(-1.0f, oceanOffset)
                .addPoint(0.0f, 0.0f)
                .addPoint(1.0f, islandScale)
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

    private static DensityFunction remap(DensityFunction input, double fromMin, double fromMax, double toMin, double toMax) {
        double factor = (toMax - toMin) / (fromMax - fromMin);
        double offset = toMin - fromMin * factor;
        if (offset == 0.0) {
            return mul(input, constant(factor));
        } else if (factor == 1.0) {
            return add(input, constant(offset));
        }
        return add(mul(input, constant(factor)), constant(offset));
    }

    private static DensityFunction clampedRemap(DensityFunction input, double fromMin, double fromMax, double toMin, double toMax) {
        return remap(input.clamp(fromMin, fromMax), fromMin, fromMax, toMin, toMax);
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
