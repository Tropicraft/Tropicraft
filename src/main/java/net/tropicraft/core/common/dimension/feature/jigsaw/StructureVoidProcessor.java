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

public record StructureVoidProcessor() implements StructureProcessor {
    public static final MapCodec<StructureVoidProcessor> CODEC = MapCodec.unit(new StructureVoidProcessor());

    @Override
    public MapCodec<StructureVoidProcessor> codec() {
        return CODEC;
    }

    @Override
    public StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos p_230386_3_, StructureBlockInfo originalInfo, StructureBlockInfo blockInfo, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template) {
        if (blockInfo.state().is(Blocks.STRUCTURE_VOID)) {
            return new StructureBlockInfo(blockInfo.pos(), Blocks.AIR.defaultBlockState(), blockInfo.nbt());
        }
        return blockInfo;
    }
}
