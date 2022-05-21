package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class TropicraftNoiseGen {
    private static final float ORE_THICKNESS = 0.08F;
    private static final double VEININESS_FREQUENCY = 1.5D;
    private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5D;
    private static final double SURFACE_DENSITY_THRESHOLD = 1.5625D;
    private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0D);
    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();
    private static final ResourceKey<DensityFunction> ZERO = createKey("zero");
    private static final ResourceKey<DensityFunction> Y = createKey("y");
    private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE = createKey("overworld/base_3d_noise");
    private static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
    private static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
    private static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
    private static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
    private static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
    private static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKey("overworld_large_biomes/continents");
    private static final ResourceKey<DensityFunction> EROSION_LARGE = createKey("overworld_large_biomes/erosion");
    private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKey("overworld_large_biomes/factor");
    private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKey("overworld_large_biomes/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKey("overworld_large_biomes/sloped_cheese");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_END = createKey("end/sloped_cheese");
    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");
    private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
    private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");
    private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");

    public static NoiseRouterWithOnlyNoises overworldWithNewCaves(NoiseSettings p_212283_, boolean p_212284_) {
        DensityFunction densityfunction = DensityFunctions.noise(getNoise(Noises.AQUIFER_BARRIER), 0.5D);
        DensityFunction densityfunction1 = DensityFunctions.noise(getNoise(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
        DensityFunction densityfunction2 = DensityFunctions.noise(getNoise(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
        DensityFunction densityfunction3 = DensityFunctions.noise(getNoise(Noises.AQUIFER_LAVA));
        DensityFunction densityfunction4 = getFunction(SHIFT_X);
        DensityFunction densityfunction5 = getFunction(SHIFT_Z);
        DensityFunction densityfunction6 = DensityFunctions.shiftedNoise2d(densityfunction4, densityfunction5, 0.25D, getNoise(p_212284_ ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
        DensityFunction densityfunction7 = DensityFunctions.shiftedNoise2d(densityfunction4, densityfunction5, 0.25D, getNoise(p_212284_ ? Noises.VEGETATION_LARGE : Noises.VEGETATION));
        DensityFunction densityfunction8 = getFunction(p_212284_ ? FACTOR_LARGE : FACTOR);
        DensityFunction densityfunction9 = getFunction(p_212284_ ? DEPTH_LARGE : DEPTH);
        DensityFunction densityfunction10 = noiseGradientDensity(DensityFunctions.cache2d(densityfunction8), densityfunction9);
        DensityFunction densityfunction11 = getFunction(p_212284_ ? SLOPED_CHEESE_LARGE : SLOPED_CHEESE);
        DensityFunction densityfunction12 = DensityFunctions.min(densityfunction11, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(ENTRANCES)));
        DensityFunction densityfunction13 = DensityFunctions.rangeChoice(densityfunction11, -1000000.0D, 1.5625D, densityfunction12, underground(densityfunction11));
        DensityFunction densityfunction14 = DensityFunctions.min(postProcess(p_212283_, densityfunction13), getFunction(NOODLE));
        DensityFunction densityfunction15 = getFunction(Y);
        int i = p_212283_.minY();
        int j = -60;
        int k = 50;
        DensityFunction densityfunction16 = yLimitedInterpolatable(densityfunction15, DensityFunctions.noise(getNoise(Noises.ORE_VEININESS), 1.5D, 1.5D), j, k, 0);
        float f = 4.0F;
        DensityFunction densityfunction17 = yLimitedInterpolatable(densityfunction15, DensityFunctions.noise(getNoise(Noises.ORE_VEIN_A), 4.0D, 4.0D), j, k, 0).abs();
        DensityFunction densityfunction18 = yLimitedInterpolatable(densityfunction15, DensityFunctions.noise(getNoise(Noises.ORE_VEIN_B), 4.0D, 4.0D), j, k, 0).abs();
        DensityFunction densityfunction19 = DensityFunctions.add(DensityFunctions.constant((double)-0.08F), DensityFunctions.max(densityfunction17, densityfunction18));
        DensityFunction densityfunction20 = DensityFunctions.noise(getNoise(Noises.ORE_GAP));
        return new NoiseRouterWithOnlyNoises(densityfunction, densityfunction1, densityfunction2, densityfunction3, densityfunction6, densityfunction7, getFunction(p_212284_ ? CONTINENTS_LARGE : CONTINENTS), getFunction(p_212284_ ? EROSION_LARGE : EROSION), getFunction(p_212284_ ? DEPTH_LARGE : DEPTH), getFunction(RIDGES), densityfunction10, densityfunction14, densityfunction16, densityfunction19, densityfunction20);
    }

    private static DensityFunction postProcess(NoiseSettings p_212275_, DensityFunction p_212276_) {
        DensityFunction densityfunction = DensityFunctions.slide(p_212275_, p_212276_);
        DensityFunction densityfunction1 = DensityFunctions.blendDensity(densityfunction);
        return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction1), DensityFunctions.constant(0.64D)).squeeze();
    }

    private static Holder<NormalNoise.NoiseParameters> getNoise(ResourceKey<NormalNoise.NoiseParameters> p_209543_) {
        return BuiltinRegistries.NOISE.getHolderOrThrow(p_209543_);
    }

    private static ResourceKey<DensityFunction> createKey(String p_209537_) {
        return ResourceKey.create(Registry.DENSITY_FUNCTION_REGISTRY, new ResourceLocation(p_209537_));
    }

    private static DensityFunction getFunction(ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(key));
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, (double)p_209474_, (double)(p_209475_ + 1), p_209473_, DensityFunctions.constant((double)p_209476_)));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction.quarterNegative());
    }

    private static DensityFunction underground(DensityFunction p_209470_) {
        DensityFunction densityfunction = getFunction(SPAGHETTI_2D);
        DensityFunction densityfunction1 = getFunction(SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction densityfunction2 = DensityFunctions.noise(getNoise(Noises.CAVE_LAYER), 8.0D);
        DensityFunction densityfunction3 = DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction2.square());
        DensityFunction densityfunction4 = DensityFunctions.noise(getNoise(Noises.CAVE_CHEESE), 0.6666666666666666D);
        DensityFunction densityfunction5 = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27D), densityfunction4).clamp(-1.0D, 1.0D), DensityFunctions.add(DensityFunctions.constant(1.5D), DensityFunctions.mul(DensityFunctions.constant(-0.64D), p_209470_)).clamp(0.0D, 0.5D));
        DensityFunction densityfunction6 = DensityFunctions.add(densityfunction3, densityfunction5);
        DensityFunction densityfunction7 = DensityFunctions.min(DensityFunctions.min(densityfunction6, getFunction(ENTRANCES)), DensityFunctions.add(densityfunction, densityfunction1));
        DensityFunction densityfunction8 = getFunction(PILLARS);
        DensityFunction densityfunction9 = DensityFunctions.rangeChoice(densityfunction8, -1000000.0D, 0.03D, DensityFunctions.constant(-1000000.0D), densityfunction8);
        return DensityFunctions.max(densityfunction7, densityfunction9);
    }
}
