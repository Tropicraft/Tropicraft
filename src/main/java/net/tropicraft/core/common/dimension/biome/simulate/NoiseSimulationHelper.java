package net.tropicraft.core.common.dimension.biome.simulate;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftTerrainShaper;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;

public class NoiseSimulationHelper {
    private final Climate.Sampler sampler;
    private final TerrainShaper tropics;
    private final NoiseGeneratorSettings noisegen;
    private final NoiseRouter router;

    public NoiseSimulationHelper(long seed) {
        noisegen = TropicraftNoiseGenSettings.createNoise(false);

        RegistryAccess.Writable registries = RegistryAccess.builtinCopy();


        Registry.register(
                registries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                new ResourceLocation(Constants.MODID, "tropics"), noisegen
        );


        router = noisegen.createNoiseRouter(registries.registryOrThrow(Registry.NOISE_REGISTRY), seed);

        this.tropics = TropicraftTerrainShaper.tropics();

        this.sampler = new Climate.Sampler(router.temperature(), router.humidity(), router.continents(), router.erosion(), router.depth(), router.ridges(), router.spawnTarget());
    }

    public Climate.TargetPoint sample(int x, int y, int z) {
        return this.sampler.sample(x, y, z);
    }

    // Depth
    public float offset(TerrainShaper.Point p) {
        return this.tropics.offset(p);
    }

    // Scale
    public float factor(TerrainShaper.Point p) {
        return this.tropics.factor(p);
    }

    public TerrainShaper.Point buildPoint(int x, int y, int z) {
        DensityFunction.SinglePointContext ctx = new DensityFunction.SinglePointContext(x, y, z);
        return TerrainShaper.makePoint((float) sampler.continentalness().compute(ctx), (float) sampler.erosion().compute(ctx), (float) sampler.weirdness().compute(ctx));
    }

    public double peaksAndValleys(int x, int y, int z) {
        return TropicraftTerrainShaper.peaksAndValleys((float) sampler.weirdness().compute(new DensityFunction.SinglePointContext(x, y, z)));
    }

    public double prelimSurfaceLevel(int x, int z) {
        NoiseSettings settings = noisegen.noiseSettings();
        for(int i = settings.getMinCellY() + settings.getCellCountY(); i >= settings.getMinCellY(); --i) {
            int j = i * settings.getCellHeight();
            double d1 = router.initialDensityWithoutJaggedness().compute(new DensityFunction.SinglePointContext(x, j, z)) + -0.703125D;
            double d2 = Mth.clamp(d1, -64.0D, 64.0D);
            d2 = applySlide(settings, d2, (double)j);
            if (d2 > 0.390625D) {
                return (double)j;
            }
        }

        return 2.147483647E9D;
    }

    private static double applySlide(NoiseSettings p_209499_, double p_209500_, double p_209501_) {
        double d0 = (double)((int)p_209501_ / p_209499_.getCellHeight() - p_209499_.getMinCellY());
        p_209500_ = p_209499_.topSlideSettings().applySlide(p_209500_, (double)p_209499_.getCellCountY() - d0);
        return p_209499_.bottomSlideSettings().applySlide(p_209500_, d0);
    }
}