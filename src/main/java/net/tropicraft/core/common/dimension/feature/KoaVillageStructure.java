package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class KoaVillageStructure extends JigsawFeature {
    public KoaVillageStructure(Codec<JigsawConfiguration> codec) {
        super(codec, 0, true, true, (context) -> KoaVillageStructure.isFeatureChunk(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                context.heightAccessor()));
    }

    private static boolean isFeatureChunk(ChunkGenerator generator, BiomeSource biomes, long seed, ChunkPos chunkPos, LevelHeightAccessor level) {
        BlockPos pos = new BlockPos((chunkPos.x << 4) + 8, 0, (chunkPos.z << 4) + 8);
        return isValid(generator, pos.offset(-4, 0, -4), level) &&
                isValid(generator, pos.offset(-4, 0, 4), level) &&
                isValid(generator, pos.offset(4, 0, 4), level) &&
                isValid(generator, pos.offset(4, 0, -4), level);
    }

    private static boolean isValid(ChunkGenerator generator, BlockPos pos, LevelHeightAccessor level) {
        return generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level) == generator.getSeaLevel();
    }
}
