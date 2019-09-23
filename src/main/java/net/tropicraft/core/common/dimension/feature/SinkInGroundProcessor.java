package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class SinkInGroundProcessor extends CheatyStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":sink_in_ground", SinkInGroundProcessor::new);

    public SinkInGroundProcessor() {}

    public SinkInGroundProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn) {
        pos = blockInfo.pos;

        // y == 0, we're under the path, so extend fences downwards if in water, or delete them if not
        if (p_215194_3_.pos.getY() == 0) {
            if (!isAirOrWater(worldReaderIn, pos)) {
                return null;
            }
            if (p_215194_3_.state.getBlock().isIn(BlockTags.FENCES)) {
                BlockPos fencePos = pos.down();
                while (isAirOrWater(worldReaderIn, fencePos)) {
                    ((IWorld)worldReaderIn).setBlockState(fencePos, TropicraftBlocks.BAMBOO_FENCE.getDefaultState().with(FenceBlock.WATERLOGGED, true), 2);
                    fencePos = fencePos.down();
                }
            }
            return blockInfo;
        }
        // Get height of the ground at this spot
        BlockPos groundCheck = worldReaderIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
        // y == 2, we're above the path, remove fence blocks that are above sea level or next to some other block
        if (p_215194_3_.pos.getY() == 2 && p_215194_3_.state.getBlock() == TropicraftBlocks.BAMBOO_FENCE) {
            if (groundCheck.getY() > 127 || !isAirOrWater(worldReaderIn, pos.down(2))) {
                return null;
            }
            for (int i = 0; i < 4; i++) {
                if (!worldReaderIn.isAirBlock(pos.offset(Direction.byHorizontalIndex(i)))) {
                    return null;
                }
            }
        }
        
        // Convert slabs to bundles over land
        if (!isAirOrWater(worldReaderIn, pos.down()) && p_215194_3_.state.getBlock() == TropicraftBlocks.THATCH_SLAB) {
            blockInfo = new BlockInfo(pos, TropicraftBlocks.THATCH_BUNDLE.getDefaultState(), null);
        }
        
        // If above sea level, sink into the ground by one block
        if (groundCheck.getY() > 127) {
            // Only sink solid blocks, or blocks that are above air/water -- delete all others
            if (Block.isOpaque(blockInfo.state.getShape(worldReaderIn, pos.down())) || isAirOrWater(worldReaderIn, pos.down())) {
                return new BlockInfo(pos.down(), blockInfo.state, blockInfo.nbt);
            }
            return null;
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
