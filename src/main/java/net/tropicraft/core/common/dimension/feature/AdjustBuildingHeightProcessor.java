package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;

public class AdjustBuildingHeightProcessor extends CheatyStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":adjust_building_height", AdjustBuildingHeightProcessor::new);

    public AdjustBuildingHeightProcessor() {}

    public AdjustBuildingHeightProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        if (seedPos.getY() < 126) {
            return new BlockInfo(blockInfo.pos.up(), blockInfo.state, blockInfo.nbt);
        }
        return blockInfo;
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return null;
    }
}
