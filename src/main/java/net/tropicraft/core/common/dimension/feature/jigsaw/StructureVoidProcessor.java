package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;

import javax.annotation.Nullable;

public class StructureVoidProcessor extends StructureProcessor {
    static final IStructureProcessorType<StructureVoidProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":structure_void", () -> Codec.unit(new StructureVoidProcessor()));

    @Override
    public BlockInfo process(IWorldReader world, BlockPos pos, BlockPos p_230386_3_, BlockInfo originalInfo, BlockInfo blockInfo, PlacementSettings placementSettings, @Nullable Template template) {
        if (blockInfo.state.getBlock() == Blocks.STRUCTURE_VOID) {
            return new BlockInfo(blockInfo.pos, Blocks.AIR.getDefaultState(), blockInfo.nbt);
        }
        return blockInfo;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }
}
