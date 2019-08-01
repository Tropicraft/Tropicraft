package net.tropicraft.core.common.dimension;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

public class TropicraftChunkGenerator extends ChunkGenerator<TropicraftGenerationSettings> {

    public TropicraftChunkGenerator(IWorld world, BiomeProvider biomeProvider, TropicraftGenerationSettings settings) {
        super(world, biomeProvider, settings);
    }

    @Override
    public void generateSurface(IChunk iChunk) {

    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public void makeBase(IWorld iWorld, IChunk iChunk) {

    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
