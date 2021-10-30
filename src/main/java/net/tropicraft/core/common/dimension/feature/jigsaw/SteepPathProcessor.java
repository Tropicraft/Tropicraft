package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class SteepPathProcessor extends PathStructureProcessor {
    public static final Codec<SteepPathProcessor> CODEC = Codec.unit(new SteepPathProcessor());

    static final IStructureProcessorType<SteepPathProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":steep_path", () -> CODEC);

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockPos pos2, BlockInfo originalBlockInfo, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        BlockPos pos = blockInfo.pos;

        if (originalBlockInfo.pos.getY() != 1 || originalBlockInfo.state.getBlock() == TropicraftBlocks.BAMBOO_STAIRS.get()) {
            return blockInfo;
        }

        Direction.Axis axis = getPathDirection(seedPos, blockInfo, placementSettingsIn, template);
        if (axis == null) {
            return blockInfo;
        }
        
        // If this is true, we are "bridging" upwards past an air gap, handles overhangs
        int bridgeTo = -1;
        
        BlockState ladder = null;
        for (AxisDirection axisDir : AxisDirection.values()) {
            Direction dir = Direction.get(axisDir, axis);
            // Detect an overhang by checking if the heightmap between spots differs by >2
            BlockPos nextHeight = worldReaderIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, pos.relative(dir)).below();
            if (nextHeight.getY() > pos.getY()) {
                ladder = getLadderState(dir);
                bridgeTo = nextHeight.getY();
            }
            if (ladder != null) {
                break;
            }
        }
        if (ladder == null) {
            return blockInfo; // Nothing to do here, we're on flat ground
        }
        // The facing the ladder stores is opposite to the direction it's placed (i.e. it faces "outward")
        Direction dir = ladder.getValue(LadderBlock.FACING).getOpposite();
        pos = pos.above();
        if (bridgeTo == pos.getY() && canPlaceLadderAt(worldReaderIn, pos.above(), dir) == null) {
            // If the next spot up can't support a ladder, this is a one block step, so place a stair block
            setBlockState(worldReaderIn, pos, TropicraftBlocks.THATCH_STAIRS.get().defaultBlockState().setValue(StairsBlock.FACING, dir));
        } else {
            // Otherwise, place ladders upwards until we find air (bridging over an initial gap if required)
            while (bridgeTo >= pos.getY() || canPlaceLadderAt(worldReaderIn, pos, dir) != null) {
                setBlockState(worldReaderIn, pos, ladder);
                setBlockState(worldReaderIn, pos.relative(dir), TropicraftBlocks.THATCH_BUNDLE.get().defaultBlockState());
                pos = pos.above();
            }
        }
        return blockInfo;
    }
    
    // Check that there is a solid block behind the ladder at this pos, and return the correct ladder state
    // Returns null if placement is not possible
    private BlockState canPlaceLadderAt(IWorldReader worldReaderIn, BlockPos pos, Direction dir) {
        BlockPos check = pos.relative(dir);
        BlockState state = worldReaderIn.getBlockState(check);
        if (!state.isAir(worldReaderIn, check)) {
            BlockState ladderState = getLadderState(dir);
            if (ladderState.canSurvive(worldReaderIn, pos)) {
                return ladderState;
            }
        }
        return null;
    }
    
    private BlockState getLadderState(Direction dir) {
        return TropicraftBlocks.BAMBOO_LADDER.get().defaultBlockState().setValue(LadderBlock.FACING, dir.getOpposite());
    }
    
    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }

}
