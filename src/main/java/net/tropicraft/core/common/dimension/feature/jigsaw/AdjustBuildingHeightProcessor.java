package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.Template.EntityInfo;
import net.tropicraft.Constants;

public class AdjustBuildingHeightProcessor extends CheatyStructureProcessor {
    public static final Codec<AdjustBuildingHeightProcessor> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.INT.optionalFieldOf("base", 126).forGetter(c -> c.base)
        ).apply(instance, AdjustBuildingHeightProcessor::new);
    });

    static final IStructureProcessorType<AdjustBuildingHeightProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":adjust_building_height", () -> CODEC);
    
    private final int base;

    public AdjustBuildingHeightProcessor(int base) {
        this.base = base;
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockPos p, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        if (seedPos.getY() < base) {
            return new BlockInfo(blockInfo.pos.above(), blockInfo.state, blockInfo.nbt);
        }
        return blockInfo;
    }
    
    @Override
    public EntityInfo processEntity(IWorldReader world, BlockPos seedPos, EntityInfo rawEntityInfo, EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
        if (seedPos.getY() < base) {
            return new EntityInfo(entityInfo.pos.add(0, 1, 0), entityInfo.blockPos.above(), entityInfo.nbt);
        }
        return entityInfo;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }
}
