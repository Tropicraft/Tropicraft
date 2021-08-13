package net.tropicraft.core.common.dimension.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

public class MangroveSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    private static final SurfaceBuilderConfig CONFIG = new SurfaceBuilderConfig(TropicraftBlocks.MUD.get().getDefaultState(), TropicraftBlocks.MUD.get().getDefaultState(), TropicraftBlocks.MUD.get().getDefaultState());
    public MangroveSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        if (noise > 1.0D) {
            SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);
        }
    }
}
