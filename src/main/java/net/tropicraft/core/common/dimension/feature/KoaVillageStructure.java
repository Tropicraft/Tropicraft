package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class KoaVillageStructure extends JigsawStructure {
    public KoaVillageStructure(Codec<VillageConfig> codec) {
        super(codec, 0, true, true);
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider biomes, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos startChunkPos, VillageConfig config) {
        BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
        return isValid(generator, pos.add(-4, 0, -4)) &&
                isValid(generator, pos.add(-4, 0, 4)) &&
                isValid(generator, pos.add(4, 0, 4)) &&
                isValid(generator, pos.add(4, 0, -4));
    }

    private boolean isValid(ChunkGenerator generator, BlockPos pos) {
        return generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG) == generator.getSeaLevel();
    }
}
