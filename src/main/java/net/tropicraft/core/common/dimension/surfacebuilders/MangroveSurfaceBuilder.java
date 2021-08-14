package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.stream.IntStream;

public class MangroveSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    private static final SurfaceBuilderConfig MUD = new SurfaceBuilderConfig(TropicraftBlocks.MUD.get().getDefaultState(), TropicraftBlocks.MUD.get().getDefaultState(), TropicraftBlocks.MUD.get().getDefaultState());

    private PerlinNoiseGenerator noise;
    private long seed;

    public MangroveSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void buildSurface(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        double mudNoise = this.noise.noiseAt(x * 0.03125, z * 0.03125, false);
        SurfaceBuilder.DEFAULT.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, mudNoise > 0.25 ? MUD : config);
    }

    @Override
    public void setSeed(long seed) {
        if (this.seed != seed || this.noise == null) {
            SharedSeedRandom random = new SharedSeedRandom(seed);
            this.noise = new PerlinNoiseGenerator(random, IntStream.rangeClosed(0, 2));
        }
        this.seed = seed;
    }
}
