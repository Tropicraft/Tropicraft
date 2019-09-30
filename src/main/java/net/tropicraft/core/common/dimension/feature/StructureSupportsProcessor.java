package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;

public class StructureSupportsProcessor extends CheatyStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":structure_supports", StructureSupportsProcessor::new);

    public StructureSupportsProcessor() {}

    public StructureSupportsProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        BlockPos pos = blockInfo.pos;
        if (p_215194_3_.pos.getY() <= 1 && blockInfo.state.getBlock().isIn(BlockTags.FENCES)) {
            if (!isAirOrWater(worldReaderIn, pos)) {
                // Delete fences that would generate inside land
                return null;
            }
            if (p_215194_3_.pos.getY() == 0) {
                // Don't generate fences underneath solid land
                if (!isAirOrWater(worldReaderIn, pos.up())) {
                    return null;
                }
                BlockPos fencePos = pos.down();
                // Extend fences at the bottom of a structure down to the ground
                while (isAirOrWater(worldReaderIn, fencePos)) {
                    setBlockState(worldReaderIn, fencePos, blockInfo.state.with(FenceBlock.WATERLOGGED, worldReaderIn.getBlockState(fencePos).getBlock() == Blocks.WATER));
                    fencePos = fencePos.down();
                }
            }
        }
        return blockInfo;
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }
}
