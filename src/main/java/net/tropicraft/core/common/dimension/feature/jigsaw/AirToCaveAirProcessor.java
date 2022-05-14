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

public class AirToCaveAirProcessor extends StructureProcessor {

    public static final Codec<AirToCaveAirProcessor> CODEC = Codec.unit(new AirToCaveAirProcessor());

    @Override
    @Nullable
    public StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos pos2, StructureBlockInfo originalInfo, StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, @Nullable StructureTemplate template) {
        if (blockInfo.state.getBlock() == Blocks.AIR) {
            return new StructureBlockInfo(blockInfo.pos, Blocks.CAVE_AIR.defaultBlockState(), blockInfo.nbt);
        }
        return super.process(world, pos, pos2, originalInfo, blockInfo, placementSettingsIn, template);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.AIR_TO_CAVE_AIR.get();
    }
}
