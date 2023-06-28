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

// TODO: Try not to be special
public class HomeTreeStructure extends Structure {
    public static final Codec<HomeTreeStructure> CODEC = RecordCodecBuilder.create(i -> i.group(
            settingsCodec(i),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
            Codec.intRange(0, 7).fieldOf("size").forGetter(s -> s.maxDepth)
    ).apply(i, HomeTreeStructure::new));

    private final Holder<StructureTemplatePool> startPool;
    private final int maxDepth;

    public HomeTreeStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth) {
        super(settings);
        this.startPool = startPool;
        this.maxDepth = maxDepth;
    }

    private static boolean isValid(ChunkGenerator generator, BlockPos pos, int startY, LevelHeightAccessor level, RandomState randomState) {
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, randomState);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    private static boolean checkLocation(GenerationContext context, BlockPos pos) {
        ChunkGenerator generator = context.chunkGenerator();
        RandomState randomState = context.randomState();
        LevelHeightAccessor level = context.heightAccessor();
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, randomState);
        return isValid(generator, pos.offset(-4, 0, -4), y, level, randomState) &&
                isValid(generator, pos.offset(-4, 0, 4), y, level, randomState) &&
                isValid(generator, pos.offset(4, 0, 4), y, level, randomState) &&
                isValid(generator, pos.offset(4, 0, -4), y, level, randomState);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ());
        if (!checkLocation(context, startPos)) {
            return Optional.empty();
        }
        return JigsawPlacement.addPieces(context, startPool, Optional.empty(), maxDepth, startPos, false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80);
    }

    @Override
    public StructureType<?> type() {
        return TropicraftStructureTypes.HOME_TREE.get();
    }
}
