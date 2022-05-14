package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import javax.annotation.Nullable;

public class StructureVoidProcessor extends StructureProcessor {
    public static final Codec<StructureVoidProcessor> CODEC = Codec.unit(new StructureVoidProcessor());

    @Override
    public StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos p_230386_3_, StructureBlockInfo originalInfo, StructureBlockInfo blockInfo, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template) {
        if (blockInfo.state.getBlock() == Blocks.STRUCTURE_VOID) {
            return new StructureBlockInfo(blockInfo.pos, Blocks.AIR.defaultBlockState(), blockInfo.nbt);
        }
        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.STRUCTURE_VOID.get();
    }
}
