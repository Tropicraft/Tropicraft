package net.tropicraft.core.common.dimension.feature.volcano;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureTypes;

import java.util.Optional;

public class VolcanoStructure extends Structure {
    public static final MapCodec<VolcanoStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            settingsCodec(i),
            HeightProvider.CODEC.fieldOf("height").forGetter(s -> s.height),
            IntProvider.CODEC.fieldOf("radius").forGetter(s -> s.radius)
    ).apply(i, VolcanoStructure::new));

    private final HeightProvider height;
    private final IntProvider radius;

    public VolcanoStructure(StructureSettings settings, HeightProvider height, IntProvider radius) {
        super(settings);
        this.height = height;
        this.radius = radius;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom random = context.random();
        int height = this.height.sample(random, new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        int radiusX = radius.sample(random);
        int radiusZ = radius.sample(random);
        long noiseSeed = random.nextLong();

        BlockPos pos = new BlockPos(chunkPos.getMinBlockX() + 8, height, chunkPos.getMinBlockZ() + 8);
        return Optional.of(new GenerationStub(pos, builder -> builder.addPiece(new VolcanoStructurePiece(context.heightAccessor(), pos, radiusX, radiusZ, noiseSeed))));
    }

    @Override
    public StructureType<?> type() {
        return TropicraftStructureTypes.VOLCANO.get();
    }
}
