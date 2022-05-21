package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlider;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftSurfaces;
import net.tropicraft.core.common.dimension.TropicraftTerrainShaper;

public final class TropicraftNoiseGenSettings {
    public static final DeferredRegister<NoiseGeneratorSettings> REGISTER = DeferredRegister.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, Constants.MODID);

    public static final RegistryObject<NoiseGeneratorSettings> TROPICS = REGISTER.register("tropics", () -> createNoise(true));

    public static NoiseGeneratorSettings createNoise(boolean tropisurface) {
        // Constant ternaries are amplified, keeping temporarily until we figure out good noise values
        // TODO: hook up custom terrain shaper and surface rule data

        NoiseSettings settings = NoiseSettings.create(-64, 384,
                new NoiseSamplingSettings(1.0D, 1.0D, 80.0D, 160.0D),
                new NoiseSlider(-0.078125D, 2, false ? 0 : 8),
                new NoiseSlider(false ? 0.4D : 0.1171875D, 3, 0),
                1, 2,
                TropicraftTerrainShaper.tropics());

        return new NoiseGeneratorSettings(
                settings,
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                TropicraftNoiseGen.overworldWithNewCaves(settings, false), tropisurface ? TropicraftSurfaces.tropics(true, false, true) : SurfaceRuleData.overworld(), 63, false, true, true, true
        );
    }
}
