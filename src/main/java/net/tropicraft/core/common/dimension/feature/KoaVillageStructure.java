package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class KoaVillageStructure extends JigsawFeature {
    public KoaVillageStructure(Codec<JigsawConfiguration> codec) {
        super(codec, 0, true, true);
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource biomes, long seed, WorldgenRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos startChunkPos, JigsawConfiguration config) {
        BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
        return isValid(generator, pos.offset(-4, 0, -4)) &&
                isValid(generator, pos.offset(-4, 0, 4)) &&
                isValid(generator, pos.offset(4, 0, 4)) &&
                isValid(generator, pos.offset(4, 0, -4));
    }

    private boolean isValid(ChunkGenerator generator, BlockPos pos) {
        return generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG) == generator.getSeaLevel();
    }
}
