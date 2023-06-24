package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftTerrainProvider;

public final class TropicraftNoiseRouterData {
    public static final DeferredRegister<DensityFunction> REGISTER = DeferredRegister.create(Registry.DENSITY_FUNCTION_REGISTRY, Constants.MODID);

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

    public static final RegistryObject<DensityFunction> OFFSET = REGISTER.register("tropics/offset", () ->
            splineWithBlending(DensityFunctions.add(
                    DensityFunctions.constant(-0.50375F),
                    DensityFunctions.spline(TropicraftTerrainProvider.offset(
                            splineCoordinate(NoiseRouterData.CONTINENTS),
                            splineCoordinate(NoiseRouterData.EROSION),
                            splineCoordinate(NoiseRouterData.RIDGES_FOLDED)
                    ))
            ), DensityFunctions.blendOffset()));

    public static final RegistryObject<DensityFunction> FACTOR = REGISTER.register("tropics/factor", () ->
            splineWithBlending(DensityFunctions.spline(TropicraftTerrainProvider.factor(
                    splineCoordinate(NoiseRouterData.CONTINENTS),
                    splineCoordinate(NoiseRouterData.EROSION),
                    splineCoordinate(NoiseRouterData.RIDGES),
                    splineCoordinate(NoiseRouterData.RIDGES_FOLDED)
            )), BLENDING_FACTOR));

    public static final RegistryObject<DensityFunction> DEPTH = REGISTER.register("tropics/depth", () -> DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), getFunction(OFFSET)));

    public static final RegistryObject<DensityFunction> JAGGEDNESS = REGISTER.register("tropics/jaggedness", () ->
            splineWithBlending(DensityFunctions.spline(TropicraftTerrainProvider.jaggedness(
                    splineCoordinate(NoiseRouterData.CONTINENTS),
                    splineCoordinate(NoiseRouterData.EROSION),
                    splineCoordinate(NoiseRouterData.RIDGES),
                    splineCoordinate(NoiseRouterData.RIDGES_FOLDED)
            )), BLENDING_JAGGEDNESS));

    public static final RegistryObject<DensityFunction> SLOPED_CHEESE = REGISTER.register("tropics/sloped_cheese", () -> {
        DensityFunction jagged = DensityFunctions.mul(getFunction(JAGGEDNESS), DensityFunctions.noise(getNoise(Noises.JAGGED), 1500.0, 0.0).halfNegative());
        return DensityFunctions.add(
                noiseGradientDensity(getFunction(FACTOR), DensityFunctions.add(getFunction(DEPTH), jagged)),
                getFunction(BuiltinRegistries.DENSITY_FUNCTION, BASE_3D_NOISE_OVERWORLD)
        );
    });

    private static DensityFunctions.Spline.Coordinate splineCoordinate(ResourceKey<DensityFunction> key) {
        return new DensityFunctions.Spline.Coordinate(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(key));
    }

    private static DensityFunction splineWithBlending(DensityFunction p_224454_, DensityFunction p_224455_) {
        DensityFunction densityfunction = DensityFunctions.lerp(DensityFunctions.blendAlpha(), p_224455_, p_224454_);
        return DensityFunctions.flatCache(DensityFunctions.cache2d(densityfunction));
    }

    public static NoiseRouter tropics(Registry<DensityFunction> registry) {
        DensityFunction aquiferBarrier = DensityFunctions.noise(getNoise(Noises.AQUIFER_BARRIER), 0.5);
        DensityFunction aquiferFluidLevelFloodedness = DensityFunctions.noise(getNoise(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction aquiferFluidLevelSpread = DensityFunctions.noise(getNoise(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 1.0 / 1.4);
        DensityFunction aquiferLava = DensityFunctions.noise(getNoise(Noises.AQUIFER_LAVA));
        DensityFunction shiftX = getFunction(registry, SHIFT_X);
        DensityFunction shiftZ = getFunction(registry, SHIFT_Z);
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, getNoise(Noises.TEMPERATURE));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, getNoise(Noises.VEGETATION));
        DensityFunction factor = getFunction(FACTOR);
        DensityFunction depth = getFunction(DEPTH);
        DensityFunction initialDensityWithoutJaggedness = noiseGradientDensity(DensityFunctions.cache2d(factor), depth);
        DensityFunction slopedCheese = getFunction(SLOPED_CHEESE);
        DensityFunction densityfunction12 = DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0), getFunction(registry, ENTRANCES)));
        DensityFunction densityfunction13 = DensityFunctions.rangeChoice(slopedCheese, -1000000.0, 1.5625, densityfunction12, underground(registry, slopedCheese));
        DensityFunction finalDensity = DensityFunctions.min(postProcess(slideTropics(densityfunction13)), getFunction(registry, NOODLE));
        DensityFunction y = getFunction(registry, Y);
        int j = -60;
        int k = 50;
        DensityFunction veinToggle = yLimitedInterpolatable(y, DensityFunctions.noise(getNoise(Noises.ORE_VEININESS), 1.5, 1.5), j, k, 0);
        DensityFunction oreVeinA = yLimitedInterpolatable(y, DensityFunctions.noise(getNoise(Noises.ORE_VEIN_A), 4.0, 4.0), j, k, 0).abs();
        DensityFunction oreVeinB = yLimitedInterpolatable(y, DensityFunctions.noise(getNoise(Noises.ORE_VEIN_B), 4.0, 4.0), j, k, 0).abs();
        DensityFunction veinRidged = DensityFunctions.add(DensityFunctions.constant(-0.08F), DensityFunctions.max(oreVeinA, oreVeinB));
        DensityFunction veinGap = DensityFunctions.noise(getNoise(Noises.ORE_GAP));
        return new NoiseRouter(
                aquiferBarrier,
                aquiferFluidLevelFloodedness,
                aquiferFluidLevelSpread,
                aquiferLava,
                temperature,
                vegetation,
                getFunction(registry, NoiseRouterData.CONTINENTS),
                getFunction(registry, NoiseRouterData.EROSION),
                depth,
                getFunction(registry, NoiseRouterData.RIDGES),
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

    private static Holder<NormalNoise.NoiseParameters> getNoise(ResourceKey<NormalNoise.NoiseParameters> key) {
        return BuiltinRegistries.NOISE.getHolderOrThrow(key);
    }

    private static ResourceKey<DensityFunction> vanillaKey(String name) {
        return ResourceKey.create(Registry.DENSITY_FUNCTION_REGISTRY, new ResourceLocation(name));
    }

    private static DensityFunctions.HolderHolder getFunction(RegistryObject<DensityFunction> function) {
        return new DensityFunctions.HolderHolder(function.getHolder().orElseThrow());
    }

    private static DensityFunction getFunction(Registry<DensityFunction> registry, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(registry.getHolderOrThrow(key));
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, p_209474_, p_209475_ + 1, p_209473_, DensityFunctions.constant(p_209476_)));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0), densityfunction.quarterNegative());
    }

    private static DensityFunction underground(Registry<DensityFunction> registry, DensityFunction slopedCheese) {
        DensityFunction spaghetti2d = getFunction(registry, SPAGHETTI_2D);
        DensityFunction spaghettiRoughness = getFunction(registry, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction caveLayer = DensityFunctions.noise(getNoise(Noises.CAVE_LAYER), 8.0);
        DensityFunction densityfunction3 = DensityFunctions.mul(DensityFunctions.constant(4.0), caveLayer.square());
        DensityFunction caveCheese = DensityFunctions.noise(getNoise(Noises.CAVE_CHEESE), 1.0 / 1.5);
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
        DensityFunction caves = DensityFunctions.min(DensityFunctions.min(densityfunction6, getFunction(registry, ENTRANCES)), DensityFunctions.add(spaghetti2d, spaghettiRoughness));
        DensityFunction pillars = getFunction(registry, PILLARS);
        DensityFunction pillarsThreshold = DensityFunctions.rangeChoice(pillars, -1000000.0, 0.03, DensityFunctions.constant(-1000000.0), pillars);
        return DensityFunctions.max(caves, pillarsThreshold);
    }
}
