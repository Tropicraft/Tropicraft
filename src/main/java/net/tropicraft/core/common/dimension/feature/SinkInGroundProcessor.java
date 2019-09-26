package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
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

        if (p_215194_3_.pos.getY() == 0) {
            if (!isAirOrWater(worldReaderIn, pos)) {
                return null;
            }
            return blockInfo;
        }
        
        // Get height of the ground at this spot
        BlockPos groundCheck = worldReaderIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
        // y == 2, we're above the path, remove fence blocks that are above sea level or next to some other block
        if (p_215194_3_.pos.getY() == 2 && p_215194_3_.state.getBlock() == TropicraftBlocks.BAMBOO_FENCE.get()) {
            if (groundCheck.getY() > 127 || !isAirOrWater(worldReaderIn, pos.down(2))) {
                return null;
            }
            for (int i = 0; i < 4; i++) {
                if (!worldReaderIn.isAirBlock(pos.offset(Direction.byHorizontalIndex(i)))) {
                    return null;
                }
            }
        }
        
        // If above sea level, sink into the ground by one block
        if (groundCheck.getY() > 127) {
            // Convert slabs to bundles when they are over land
            if (!isAirOrWater(worldReaderIn, pos.down()) && p_215194_3_.state.getBlock() == TropicraftBlocks.THATCH_SLAB.get()) {
                blockInfo = new BlockInfo(pos, TropicraftBlocks.THATCH_BUNDLE.get().getDefaultState(), null);
            }
            
            // Only sink solid blocks, or blocks that are above air/water -- delete all others
            if (Block.isOpaque(blockInfo.state.getShape(worldReaderIn, pos.down())) || isAirOrWater(worldReaderIn, pos.down())) {
                return new BlockInfo(pos.down(), blockInfo.state, blockInfo.nbt);
            }
            return null;
        }
        
        removeObstructions(worldReaderIn, pos.up(), pos.up(2));

        return blockInfo;
    }
    
    private void removeObstructions(IWorldReader world, BlockPos... positions) {
        for (BlockPos pos : positions) {
            BlockState current = world.getBlockState(pos);
            if (current.isIn(BlockTags.LEAVES) || current.isIn(BlockTags.LOGS)) {
                setBlockState(world, pos, Blocks.AIR.getDefaultState());
            }
        }
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
