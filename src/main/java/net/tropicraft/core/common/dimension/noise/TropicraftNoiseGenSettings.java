package net.tropicraft.core.common.dimension.noise;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.tropicraft.core.common.TropicraftSurfaces;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.TropicraftTerrainShaper;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

import java.util.Map;
import java.util.Optional;

public final class TropicraftNoiseGenSettings {
    private final Map<StructureFeature<?>, StructureFeatureConfiguration> structureBiomeMap;

    public TropicraftNoiseGenSettings(WorldgenDataConsumer<NoiseGeneratorSettings> noise, TropicraftConfiguredStructures structures) {
        structureBiomeMap = ImmutableMap.of(
            TropicraftFeatures.HOME_TREE.get(), new StructureFeatureConfiguration(24, 8, 1010101010),
            TropicraftFeatures.KOA_VILLAGE.get(), new StructureFeatureConfiguration(24, 8, 1010101011)
        );

        noise.register(createNoise(structureBiomeMap));
    }
    
    private static NoiseGeneratorSettings createNoise(final Map<StructureFeature<?>, StructureFeatureConfiguration> structureBiomeMap) {
        // Constant ternaries are amplified, keeping temporarily until we figure out good noise values
        // TODO: hook up custom terrain shaper and surface rule data
        return new NoiseGeneratorSettings(
                new StructureSettings(Optional.empty(), structureBiomeMap),
                NoiseSettings.create(-64, 384,
                        new NoiseSamplingSettings(1.0D, 1.0D, 80.0D, 160.0D),
                        new NoiseSlider(-0.078125D, 2, false ? 0 : 8),
                        new NoiseSlider(false ? 0.4D : 0.1171875D, 3, 0),
                        1, 2,
                        false, false, false, TropicraftTerrainShaper.tropics()),
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                TropicraftSurfaces.tropics(true, false, true), 63, false, true, true, true, true, false
        );
    }
}
