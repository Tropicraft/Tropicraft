package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

public class OsaRainforestSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    private static final SurfaceBuilderBaseConfiguration MUD = new SurfaceBuilderBaseConfiguration(TropicraftBlocks.MUD.get().defaultBlockState(), Blocks.DIRT.defaultBlockState(), TropicraftBlocks.MUD.get().defaultBlockState());

    public OsaRainforestSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, ChunkAccess chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int pMinSurfaceLevel, long seed, SurfaceBuilderBaseConfiguration config) {
        boolean muddy = noise + (random.nextDouble() * 0.2) > 4.5;

        SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, pMinSurfaceLevel, seed, muddy ? MUD : config);
    }
}
