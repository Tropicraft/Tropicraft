package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jspecify.annotations.Nullable;

public record AirToCaveAirProcessor() implements StructureProcessor {

    public static final MapCodec<AirToCaveAirProcessor> CODEC = MapCodec.unit(new AirToCaveAirProcessor());

    @Override
    public StructureBlockInfo process(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureBlockInfo originalBlockInfo, StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (processedBlockInfo.state().is(Blocks.AIR)) {
            return new StructureBlockInfo(processedBlockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), processedBlockInfo.nbt());
        }
        return processedBlockInfo;
    }

    @Override
    public MapCodec<AirToCaveAirProcessor> codec() {
        return CODEC;
    }
}
