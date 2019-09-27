package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;

import java.util.Random;
import java.util.function.Function;

public class TropicsSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    public TropicsSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> function) {
        super(function);
    }

    @Override
    public void buildSurface(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        // Modify this to change the amount of sand in the tropics biomes
        if (noise > 0.1) {
            SurfaceBuilder.DEFAULT.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG.get());
        } else {
            SurfaceBuilder.DEFAULT.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, TropicsBuilderConfigs.TROPICS_CONFIG.get());
        }
    }
}
