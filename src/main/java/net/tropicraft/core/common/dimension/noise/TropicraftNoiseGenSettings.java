package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.TropicraftSurfaces;
import net.tropicraft.core.common.dimension.df.TropicraftDensityFunctions;

import java.util.List;

public final class TropicraftNoiseGenSettings {
    public static final ResourceKey<NoiseGeneratorSettings> TROPICS = createKey("tropics");

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(TROPICS, createNoise(densityFunctions, biomes, true));
    }

    public static NoiseGeneratorSettings createNoise(HolderGetter<DensityFunction> densityFunctions, HolderGetter<Biome> biomes, boolean tropisurface) {
        // Constant ternaries are amplified, keeping temporarily until we figure out good noise values
        NoiseSettings settings = NoiseSettings.create(-64, 384, 1, 2);

        SurfaceRules.RuleSource surface = tropisurface ? TropicraftSurfaces.tropics(biomes) : SurfaceRuleData.overworld(biomes);
        return new NoiseGeneratorSettings(
                settings,
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                TropicraftDensityFunctions.tropics(densityFunctions),
                // TODO: Re-introduce
                SurfaceRules.state(Blocks.STONE.defaultBlockState()),
                List.of(),
                TropicraftDimension.SEA_LEVEL,
                false,
                // TODO: Re-introduce
                false,
                false,
                true
        );
    }

    private static ResourceKey<NoiseGeneratorSettings> createKey(String name) {
        return Tropicraft.resourceKey(Registries.NOISE_SETTINGS, name);
    }
}
