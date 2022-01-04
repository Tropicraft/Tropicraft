package net.tropicraft.core.common.dimension.noise;

import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

public final class TropicraftNoiseGenSettings {
    public TropicraftNoiseGenSettings(WorldgenDataConsumer<NoiseGeneratorSettings> noise) {
        noise.register(new ResourceLocation(Constants.MODID, "tropics"), createNoise());
    }
    
    private static NoiseGeneratorSettings createNoise() {
        // Constant ternaries are amplified, keeping temporarily until we figure out good noise values
        // TODO: hook up custom terrain shaper and surface rule data
        return new NoiseGeneratorSettings(
                new StructureSettings(false),
                NoiseSettings.create(-64, 384,
                        new NoiseSamplingSettings(1.0D, 1.0D, 80.0D, 160.0D),
                        new NoiseSlider(-0.078125D, 2, false ? 0 : 8),
                        new NoiseSlider(false ? 0.4D : 0.1171875D, 3, 0),
                        1, 2,
                        false, false, false, TerrainProvider.overworld(false)),
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                SurfaceRuleData.overworld(), 63, false, true, true, true, true, false
        );
    }
}
