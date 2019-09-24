package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;

public class StructureVoidProcessor extends StructureProcessor {
    
    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":structure_void", StructureVoidProcessor::new);

    public StructureVoidProcessor() {}

    public StructureVoidProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }
    
    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        if (blockInfo.state.getBlock() == Blocks.STRUCTURE_VOID) {
            return new BlockInfo(blockInfo.pos, Blocks.AIR.getDefaultState(), blockInfo.nbt);
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
