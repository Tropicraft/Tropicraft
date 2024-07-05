package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureEntityInfo;

public class AdjustBuildingHeightProcessor extends CheatyStructureProcessor {
    public static final MapCodec<AdjustBuildingHeightProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.optionalFieldOf("base", 126).forGetter(c -> c.base)
    ).apply(i, AdjustBuildingHeightProcessor::new));

    private final int base;

    public AdjustBuildingHeightProcessor(int base) {
        this.base = base;
    }

    @Override
    public StructureBlockInfo process(LevelReader worldReaderIn, BlockPos seedPos, BlockPos p, StructureBlockInfo p_215194_3_, StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, StructureTemplate template) {
        if (seedPos.getY() < base) {
            return new StructureBlockInfo(blockInfo.pos().above(), blockInfo.state(), blockInfo.nbt());
        }
        return blockInfo;
    }

    @Override
    public StructureEntityInfo processEntity(LevelReader world, BlockPos seedPos, StructureEntityInfo rawEntityInfo, StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        if (seedPos.getY() < base) {
            return new StructureEntityInfo(entityInfo.pos.add(0, 1, 0), entityInfo.blockPos.above(), entityInfo.nbt);
        }
        return entityInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.ADJUST_BUILDING_HEIGHT.get();
    }
}
