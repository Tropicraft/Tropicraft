package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftTerrainProvider;

public final class TropicraftNoiseRouterData {
    private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0);
    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();

    private static final ResourceKey<DensityFunction> Y = vanillaKey("y");
    private static final ResourceKey<DensityFunction> SHIFT_X = vanillaKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = vanillaKey("shift_z");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = vanillaKey("overworld/base_3d_noise");
    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = vanillaKey("overworld/caves/spaghetti_roughness_function");
    private static final ResourceKey<DensityFunction> ENTRANCES = vanillaKey("overworld/caves/entrances");
    private static final ResourceKey<DensityFunction> NOODLE = vanillaKey("overworld/caves/noodle");
    private static final ResourceKey<DensityFunction> PILLARS = vanillaKey("overworld/caves/pillars");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = vanillaKey("overworld/caves/spaghetti_2d");

    public static final ResourceKey<DensityFunction> OFFSET = createKey("tropics/offset");
    public static final ResourceKey<DensityFunction> FACTOR = createKey("tropics/factor");
    public static final ResourceKey<DensityFunction> DEPTH = createKey("tropics/depth");
    public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("tropics/jaggedness");
    public static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("tropics/sloped_cheese");

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);

        DensityFunctions.Spline.Coordinate continents = new DensityFunctions.Spline.Coordinate(densityFunctions.getOrThrow(NoiseRouterData.CONTINENTS));
        DensityFunctions.Spline.Coordinate erosion = new DensityFunctions.Spline.Coordinate(densityFunctions.getOrThrow(NoiseRouterData.EROSION));
        DensityFunctions.Spline.Coordinate ridges = new DensityFunctions.Spline.Coordinate(densityFunctions.getOrThrow(NoiseRouterData.RIDGES_FOLDED));
        DensityFunctions.Spline.Coordinate weirdness = new DensityFunctions.Spline.Coordinate(densityFunctions.getOrThrow(NoiseRouterData.RIDGES));

        Holder.Reference<DensityFunction> offset = context.register(OFFSET, splineWithBlending(DensityFunctions.add(
                DensityFunctions.constant(-0.50375f),
                DensityFunctions.spline(TropicraftTerrainProvider.offset(
                        continents,
                        erosion,
                        ridges
                ))
        ), DensityFunctions.blendOffset()));

        Holder.Reference<DensityFunction> factor = context.register(FACTOR, splineWithBlending(DensityFunctions.spline(TropicraftTerrainProvider.factor(
                continents,
                erosion,
                weirdness,
                ridges
        )), BLENDING_FACTOR));

        Holder.Reference<DensityFunction> depth = context.register(DEPTH, DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), wrap(offset)));

        Holder.Reference<DensityFunction> jaggedness = context.register(JAGGEDNESS, splineWithBlending(DensityFunctions.spline(TropicraftTerrainProvider.jaggedness(
                continents,
                erosion,
                weirdness,
                ridges
        )), BLENDING_JAGGEDNESS));

        DensityFunction jagged = DensityFunctions.mul(wrap(jaggedness), DensityFunctions.noise(context.lookup(Registries.NOISE).getOrThrow(Noises.JAGGED), 1500.0, 0.0).halfNegative());

        context.register(SLOPED_CHEESE, DensityFunctions.add(
                noiseGradientDensity(wrap(factor), DensityFunctions.add(wrap(depth), jagged)),
                wrap(densityFunctions.getOrThrow(BASE_3D_NOISE_OVERWORLD))
        ));
    }

    private static DensityFunction splineWithBlending(DensityFunction p_224454_, DensityFunction p_224455_) {
        DensityFunction densityfunction = DensityFunctions.lerp(DensityFunctions.blendAlpha(), p_224455_, p_224454_);
        return DensityFunctions.flatCache(DensityFunctions.cache2d(densityfunction));
    }

    public static NoiseRouter tropics(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        DensityFunction aquiferBarrier = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_BARRIER), 0.5);
        DensityFunction aquiferFluidLevelFloodedness = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction aquiferFluidLevelSpread = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 1.0 / 1.4);
        DensityFunction aquiferLava = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction shiftX = getFunction(densityFunctions, SHIFT_X);
        DensityFunction shiftZ = getFunction(densityFunctions, SHIFT_Z);
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction factor = getFunction(densityFunctions, FACTOR);
        DensityFunction depth = getFunction(densityFunctions, DEPTH);
        DensityFunction initialDensityWithoutJaggedness = noiseGradientDensity(DensityFunctions.cache2d(factor), depth);
        DensityFunction slopedCheese = getFunction(densityFunctions, SLOPED_CHEESE);
        DensityFunction densityfunction12 = DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0), getFunction(densityFunctions, ENTRANCES)));
        DensityFunction densityfunction13 = DensityFunctions.rangeChoice(slopedCheese, -1000000.0, 1.5625, densityfunction12, underground(densityFunctions, noiseParameters, slopedCheese));
        DensityFunction finalDensity = DensityFunctions.min(postProcess(slideTropics(densityfunction13)), getFunction(densityFunctions, NOODLE));
        DensityFunction y = getFunction(densityFunctions, Y);
        int j = -60;
        int k = 50;
        DensityFunction veinToggle = yLimitedInterpolatable(y, DensityFunctions.noise(noiseParameters.getOrThrow(Noises.ORE_VEININESS), 1.5, 1.5), j, k, 0);
        DensityFunction oreVeinA = yLimitedInterpolatable(y, DensityFunctions.noise(noiseParameters.getOrThrow(Noises.ORE_VEIN_A), 4.0, 4.0), j, k, 0).abs();
        DensityFunction oreVeinB = yLimitedInterpolatable(y, DensityFunctions.noise(noiseParameters.getOrThrow(Noises.ORE_VEIN_B), 4.0, 4.0), j, k, 0).abs();
        DensityFunction veinRidged = DensityFunctions.add(DensityFunctions.constant(-0.08f), DensityFunctions.max(oreVeinA, oreVeinB));
        DensityFunction veinGap = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.ORE_GAP));
        return new NoiseRouter(
                aquiferBarrier,
                aquiferFluidLevelFloodedness,
                aquiferFluidLevelSpread,
                aquiferLava,
                temperature,
                vegetation,
                getFunction(densityFunctions, NoiseRouterData.CONTINENTS),
                getFunction(densityFunctions, NoiseRouterData.EROSION),
                depth,
                getFunction(densityFunctions, NoiseRouterData.RIDGES),
                initialDensityWithoutJaggedness,
                finalDensity,
                veinToggle,
                veinRidged,
                veinGap
        );
    }

    private static DensityFunction postProcess(DensityFunction function) {
        DensityFunction blendedFunction = DensityFunctions.blendDensity(function);
        return DensityFunctions.mul(DensityFunctions.interpolated(blendedFunction), DensityFunctions.constant(0.64)).squeeze();
    }

    private static DensityFunction slideTropics(DensityFunction function) {
        return slide(function, -64, 384, 80, 64, -0.078125, 0, 24, 0.1171875);
    }

    private static DensityFunction slide(DensityFunction function, int minY, int height, int topSliderLowerOffset, int topSlideUpperOffset, double topSlideTarget, int bottomSlideLowerOffset, int bottomSlideUpperOffset, double bottomSlideTarget) {
        DensityFunction topSlideFactor = DensityFunctions.yClampedGradient(minY + height - topSliderLowerOffset, minY + height - topSlideUpperOffset, 1.0, 0.0);
        DensityFunction bottomSlideFactor = DensityFunctions.yClampedGradient(minY + bottomSlideLowerOffset, minY + bottomSlideUpperOffset, 0.0, 1.0);
        return DensityFunctions.lerp(bottomSlideFactor, bottomSlideTarget, DensityFunctions.lerp(topSlideFactor, topSlideTarget, function));
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> registry, ResourceKey<DensityFunction> key) {
        return wrap(registry.getOrThrow(key));
    }

    private static DensityFunctions.HolderHolder wrap(Holder.Reference<DensityFunction> holder) {
        return new DensityFunctions.HolderHolder(holder);
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, p_209474_, p_209475_ + 1, p_209473_, DensityFunctions.constant(p_209476_)));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0), densityfunction.quarterNegative());
    }

    private static DensityFunction underground(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters, DensityFunction slopedCheese) {
        DensityFunction spaghetti2d = getFunction(densityFunctions, SPAGHETTI_2D);
        DensityFunction spaghettiRoughness = getFunction(densityFunctions, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction caveLayer = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.CAVE_LAYER), 8.0);
        DensityFunction densityfunction3 = DensityFunctions.mul(DensityFunctions.constant(4.0), caveLayer.square());
        DensityFunction caveCheese = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.CAVE_CHEESE), 1.0 / 1.5);
        DensityFunction densityfunction5 = DensityFunctions.add(
                DensityFunctions.add(
                        DensityFunctions.constant(0.27),
                        caveCheese
                ).clamp(-1.0, 1.0),
                DensityFunctions.add(
                        DensityFunctions.constant(1.5),
                        DensityFunctions.mul(DensityFunctions.constant(-0.64), slopedCheese)
                ).clamp(0.0, 0.5)
        );
        DensityFunction densityfunction6 = DensityFunctions.add(densityfunction3, densityfunction5);
        DensityFunction caves = DensityFunctions.min(DensityFunctions.min(densityfunction6, getFunction(densityFunctions, ENTRANCES)), DensityFunctions.add(spaghetti2d, spaghettiRoughness));
        DensityFunction pillars = getFunction(densityFunctions, PILLARS);
        DensityFunction pillarsThreshold = DensityFunctions.rangeChoice(pillars, -1000000.0, 0.03, DensityFunctions.constant(-1000000.0), pillars);
        return DensityFunctions.max(caves, pillarsThreshold);
    }

    private static ResourceKey<DensityFunction> vanillaKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.withDefaultNamespace(name));
    }

    private static ResourceKey<DensityFunction> createKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }
}
