package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.stream.IntStream;

public class MangroveSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    private static final SurfaceBuilderConfig MUD = new SurfaceBuilderConfig(TropicraftBlocks.MUD.get().getDefaultState(), Blocks.DIRT.getDefaultState(), TropicraftBlocks.MUD.get().getDefaultState());

    private PerlinNoiseGenerator mudNoise;
    private PerlinNoiseGenerator streamNoise;
    private long seed;

    public MangroveSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void buildSurface(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        double streamNoise = this.streamNoise.noiseAt(x * 0.025, z * 0.025, false);
        double mudNoise = this.mudNoise.noiseAt(x * 0.03125, z * 0.03125, false);
        boolean placeMud = mudNoise > 0.25;

        if (streamNoise > -0.1 && streamNoise < 0.1) {
            this.placeStream(chunk, x, z, startHeight, defaultFluid, seaLevel);
        }

        if (streamNoise > -0.2 && streamNoise < 0.2) {
            double chance = 1 - (Math.abs(streamNoise) * 5);
            placeMud = random.nextDouble() > chance;
        }


        SurfaceBuilder.DEFAULT.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, placeMud ? MUD : config);
    }

    private void placeStream(IChunk chunk, int x, int z, int startHeight, BlockState defaultFluid, int seaLevel) {
        int localX = x & 15;
        int localZ = z & 15;

        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        for (int y = startHeight; y >= 0; y--) {
            mutablePos.setPos(localX, y, localZ);
            if (!chunk.getBlockState(mutablePos).isAir()) {
                if (y + 1 == seaLevel && !chunk.getBlockState(mutablePos).matchesBlock(defaultFluid.getBlock())) {
                    chunk.setBlockState(mutablePos, defaultFluid, false);
                }
                break;
            }
        }
    }

    @Override
    public void setSeed(long seed) {
        if (this.seed != seed || this.mudNoise == null) {
            SharedSeedRandom random = new SharedSeedRandom(seed);
            this.mudNoise = new PerlinNoiseGenerator(random, IntStream.rangeClosed(0, 2));
            this.streamNoise = new PerlinNoiseGenerator(random, IntStream.rangeClosed(0, 2));
        }
        this.seed = seed;
    }
}
