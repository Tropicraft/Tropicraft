package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

// TODO: Try to not be special
public class KoaVillageStructure extends Structure {
    public static final Codec<KoaVillageStructure> CODEC = RecordCodecBuilder.create(i -> i.group(
            settingsCodec(i),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
            Codec.intRange(0, 7).fieldOf("size").forGetter(s -> s.maxDepth)
    ).apply(i, KoaVillageStructure::new));

    private final Holder<StructureTemplatePool> startPool;
    private final int maxDepth;

    public KoaVillageStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth) {
        super(settings);
        this.startPool = startPool;
        this.maxDepth = maxDepth;
    }

    private static boolean isFeatureChunk(GenerationContext context, BlockPos startPos) {
        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor level = context.heightAccessor();
        RandomState randomState = context.randomState();
        return isValid(generator, startPos.offset(-4, 0, -4), level, randomState) &&
                isValid(generator, startPos.offset(-4, 0, 4), level, randomState) &&
                isValid(generator, startPos.offset(4, 0, 4), level, randomState) &&
                isValid(generator, startPos.offset(4, 0, -4), level, randomState);
    }

    private static boolean isValid(ChunkGenerator generator, BlockPos pos, LevelHeightAccessor level, RandomState randomState) {
        return generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, randomState) == generator.getSeaLevel();
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX() + 8, 0, chunkPos.getMinBlockZ() + 8);
        if (!isFeatureChunk(context, startPos)) {
            return Optional.empty();
        }
        return JigsawPlacement.addPieces(context, startPool, Optional.empty(), maxDepth, startPos, true, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80);
    }

    @Override
    public StructureType<?> type() {
        return TropicraftStructureTypes.KOA_VILLAGE.get();
    }
}
