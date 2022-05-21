package net.tropicraft.core.common.dimension.biome.simulate;

import net.minecraft.SharedConstants;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.Builder;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;

public class NoiseSimulationHelper {
    private final Climate.Sampler sampler;

    public NoiseSimulationHelper(long seed) {
        NoiseGeneratorSettings noisegen = TropicraftNoiseGenSettings.createNoise(false);

        RegistryAccess.Writable registries = RegistryAccess.builtinCopy();


        Registry.register(
                registries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                new ResourceLocation(Constants.MODID, "tropics"), noisegen
        );

        NoiseRouter router = noisegen.createNoiseRouter(registries.registryOrThrow(Registry.NOISE_REGISTRY), seed);

        this.sampler = new Climate.Sampler(router.temperature(), router.humidity(), router.continents(), router.erosion(), router.depth(), router.ridges(), router.spawnTarget());
    }

    public Climate.TargetPoint sample(int x, int y, int z) {
        return this.sampler.sample(x, y, z);
    }
}