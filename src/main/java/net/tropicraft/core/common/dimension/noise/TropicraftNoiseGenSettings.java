package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.TropicraftSurfaces;

import java.util.List;

public final class TropicraftNoiseGenSettings {
    public static final ResourceKey<NoiseGeneratorSettings> TROPICS = createKey("tropics");

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParameters = context.lookup(Registries.NOISE);
        context.register(TROPICS, createNoise(densityFunctions, noiseParameters, true));
    }

    public static NoiseGeneratorSettings createNoise(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters, boolean tropisurface) {
        // Constant ternaries are amplified, keeping temporarily until we figure out good noise values
        NoiseSettings settings = NoiseSettings.create(-64, 384, 1, 2);

        SurfaceRules.RuleSource surface = tropisurface ? TropicraftSurfaces.tropics() : SurfaceRuleData.overworld();
        return new NoiseGeneratorSettings(
                settings,
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                TropicraftNoiseRouterData.tropics(densityFunctions, noiseParameters),
                surface,
                List.of(),
                TropicraftDimension.SEA_LEVEL,
                false,
                true,
                true,
                true
        );
    }

    private static ResourceKey<NoiseGeneratorSettings> createKey(String name) {
        return ResourceKey.create(Registries.NOISE_SETTINGS, Tropicraft.location(name));
    }
}
