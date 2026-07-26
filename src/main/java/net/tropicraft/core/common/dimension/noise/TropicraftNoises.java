package net.tropicraft.core.common.dimension.noise;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.tropicraft.Tropicraft;

public interface TropicraftNoises {
    ResourceKey<NormalNoise.NoiseParameters> CONTINENTS = createKey("tropics/continents");
    ResourceKey<NormalNoise.NoiseParameters> CONTINENT_WARP_X = createKey("tropics/continent_warp_x");
    ResourceKey<NormalNoise.NoiseParameters> CONTINENT_WARP_Z = createKey("tropics/continent_warp_z");

    ResourceKey<NormalNoise.NoiseParameters> HIGH_FREQ_WARP_X = createKey("tropics/high_freq_warp_x");
    ResourceKey<NormalNoise.NoiseParameters> HIGH_FREQ_WARP_Z = createKey("tropics/high_freq_warp_z");

    ResourceKey<NormalNoise.NoiseParameters> ISLAND_MASK = createKey("tropics/island_mask");
    ResourceKey<NormalNoise.NoiseParameters> ISLANDS = createKey("tropics/islands");

    ResourceKey<NormalNoise.NoiseParameters> EROSION = createKey("tropics/erosion");

    static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        register(context, CONTINENTS, -11, 0.5, 0.0, 2.0);
        register(context, CONTINENT_WARP_X, -9, 1.0);
        register(context, CONTINENT_WARP_Z, -9, 1.0);

        register(context, HIGH_FREQ_WARP_X, -8, 1.0, 0.0, 1.0, 1.0);
        register(context, HIGH_FREQ_WARP_Z, -8, 1.0, 0.0, 1.0, 1.0);

        register(context, ISLAND_MASK, -9, 1.0);
        register(context, ISLANDS, -6, 1.0);

        register(context, EROSION, -10, 1.0, 1.0, 0.0, 1.0, 1.0);
    }

    private static Holder.Reference<NormalNoise.NoiseParameters> register(BootstrapContext<NormalNoise.NoiseParameters> context, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double... amplitudes) {
        return context.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
    }

    private static ResourceKey<NormalNoise.NoiseParameters> createKey(String name) {
        return Tropicraft.resourceKey(Registries.NOISE, name);
    }
}
