package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jspecify.annotations.Nullable;

public record SmoothingGravityProcessor(
        Heightmap.Types heightmap,
        int offset,
        GravityProcessor baseline
) implements PathStructureProcessor {
    public static final MapCodec<SmoothingGravityProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Types.CODEC.fieldOf("heightmap").forGetter(p -> p.heightmap),
            Codec.INT.fieldOf("offset").forGetter(p -> p.offset)
    ).apply(i, SmoothingGravityProcessor::new));

    public SmoothingGravityProcessor(Types heightmap, int offset) {
        this(heightmap, offset, new GravityProcessor(heightmap, offset));
    }

    @Override
    public StructureBlockInfo process(LevelReader level, BlockPos seedPos, BlockPos pos2, StructureBlockInfo originalBlockInfo, StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, @Nullable StructureTemplate template) {
        Axis pathDir = getPathDirection(level, seedPos, blockInfo, placementSettingsIn, template);
        if (pathDir == null) {
            pathDir = Axis.X; // Better than nothing
        }
        BlockPos pos = blockInfo.pos();
        BlockPos posForward = pos.relative(Direction.get(AxisDirection.POSITIVE, pathDir));
        BlockPos posBackward = pos.relative(Direction.get(AxisDirection.NEGATIVE, pathDir));
        int heightForward = level.getHeight(heightmap, posForward.getX(), posForward.getZ()) + offset;
        int heightBackward = level.getHeight(heightmap, posBackward.getX(), posBackward.getZ()) + offset;
        int height = level.getHeight(heightmap, pos.getX(), pos.getZ()) + offset;
        if (heightForward > height && heightBackward > height) {
            return new StructureBlockInfo(new BlockPos(pos.getX(), Math.min(heightForward, heightBackward), pos.getZ()), blockInfo.state(), blockInfo.nbt());
        }
        return baseline.process(level, seedPos, pos2, originalBlockInfo, blockInfo, placementSettingsIn, template);
    }

    @Override
    public MapCodec<SmoothingGravityProcessor> codec() {
        return CODEC;
    }
}
