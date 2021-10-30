package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.stream.IntStream;

public class MangroveSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    private static final SurfaceBuilderBaseConfiguration MUD = new SurfaceBuilderBaseConfiguration(TropicraftBlocks.MUD.get().defaultBlockState(), Blocks.DIRT.defaultBlockState(), TropicraftBlocks.MUD.get().defaultBlockState());

    private PerlinSimplexNoise mudNoise;
    private PerlinSimplexNoise streamNoise;
    private long seed;

    public MangroveSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, ChunkAccess chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderBaseConfiguration config) {
        double streamNoise = this.streamNoise.getValue(x * 0.025, z * 0.025, false);
        double mudNoise = this.mudNoise.getValue(x * 0.03125, z * 0.03125, false);
        boolean muddy = mudNoise > -0.1;

        if (streamNoise > -0.1 && streamNoise < 0.1) {
            this.placeStream(chunk, x, z, startHeight, defaultFluid, seaLevel);
        }

        if (streamNoise > -0.2 && streamNoise < 0.2) {
            double chance = 1 - (Math.abs(streamNoise) * 5);
            muddy = random.nextDouble() > chance;
        }

        SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, muddy ? MUD : config);
    }

    private void placeStream(ChunkAccess chunk, int x, int z, int startHeight, BlockState defaultFluid, int seaLevel) {
        int localX = x & 15;
        int localZ = z & 15;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int y = startHeight; y >= 0; y--) {
            mutablePos.set(localX, y, localZ);
            if (!chunk.getBlockState(mutablePos).isAir()) {
                if (y + 1 == seaLevel && !chunk.getBlockState(mutablePos).is(defaultFluid.getBlock())) {
                    chunk.setBlockState(mutablePos, defaultFluid, false);
                }
                break;
            }
        }
    }

    @Override
    public void initNoise(long seed) {
        if (this.seed != seed || this.mudNoise == null) {
            WorldgenRandom random = new WorldgenRandom(seed);
            this.mudNoise = new PerlinSimplexNoise(random, IntStream.rangeClosed(0, 2));
            this.streamNoise = new PerlinSimplexNoise(random, IntStream.rangeClosed(0, 2));
        }
        this.seed = seed;
    }
}
