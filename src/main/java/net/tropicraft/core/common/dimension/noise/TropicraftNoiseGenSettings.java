package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.TropicraftSurfaces;

import java.util.List;

public final class TropicraftNoiseGenSettings {
    public static final DeferredRegister<NoiseGeneratorSettings> REGISTER = DeferredRegister.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, Constants.MODID);

    public static final RegistryObject<NoiseGeneratorSettings> TROPICS = REGISTER.register("tropics", () -> createNoise(true));

    public static NoiseGeneratorSettings createNoise(boolean tropisurface) {
        // Constant ternaries are amplified, keeping temporarily until we figure out good noise values
        NoiseSettings settings = NoiseSettings.create(-64, 384, 1, 2);

        final SurfaceRules.RuleSource surface = tropisurface ? TropicraftSurfaces.tropics() : SurfaceRuleData.overworld();
        return new NoiseGeneratorSettings(
                settings,
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                TropicraftNoiseRouterData.tropics(BuiltinRegistries.DENSITY_FUNCTION),
                surface,
                List.of(),
                TropicraftDimension.SEA_LEVEL,
                false,
                true,
                true,
                true
        );
    }
}
