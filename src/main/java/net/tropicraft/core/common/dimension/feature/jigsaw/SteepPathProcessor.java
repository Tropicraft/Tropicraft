package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.Rotation;
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

        if (originalBlockInfo.pos.getY() != 1 || originalBlockInfo.state.getBlock() == TropicraftBlocks.BAMBOO_STAIRS.get() || originalBlockInfo.state.isAir()) {
            return blockInfo;
        }

        Direction.Axis axis = getPathDirection(worldReaderIn, seedPos, blockInfo, placementSettingsIn, template);
        if (axis == null) {
            return blockInfo;
        }
        
        // If this is true, we are "bridging" upwards past an air gap, handles overhangs
        int bridgeTo = -1;
        
        BlockState ladder = null;
        for (AxisDirection axisDir : AxisDirection.values()) {
            Direction dir = Direction.getFacingFromAxis(axisDir, axis);
            // Detect an overhang by checking if the heightmap between spots differs by >2
            BlockPos nextHeight = worldReaderIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.offset(dir)).down();
            if (nextHeight.getY() > pos.getY()) {
                ladder = getLadderState(dir);
                bridgeTo = nextHeight.getY();
            }
            if (ladder != null) {
                break;
            }
        }
//        Rotation antiRotate = placementSettingsIn.getRotation();
//        if (antiRotate == Rotation.CLOCKWISE_90) {
//        	antiRotate = Rotation.COUNTERCLOCKWISE_90;
//        } else if (antiRotate == Rotation.COUNTERCLOCKWISE_90) {
//        	antiRotate = Rotation.CLOCKWISE_90;
//        }
//        Mirror antiMirror = placementSettingsIn.getMirror();
//        BlockState debugState = Blocks.MAGENTA_GLAZED_TERRACOTTA.getDefaultState()
//        		.with(GlazedTerracottaBlock.HORIZONTAL_FACING, Direction.getFacingFromAxis(AxisDirection.POSITIVE, axis))
//        		.rotate(antiRotate)
//        		.mirror(antiMirror);
        if (ladder == null) {
            return blockInfo;//new BlockInfo(blockInfo.pos, debugState, blockInfo.nbt); // Nothing to do here, we're on flat ground
        }
        // The facing the ladder stores is opposite to the direction it's placed (i.e. it faces "outward")
        Direction dir = ladder.get(LadderBlock.FACING).getOpposite();
        pos = pos.up();
        if (bridgeTo == pos.getY() && canPlaceLadderAt(worldReaderIn, pos.up(), dir) == null) {
        	if (pos.getY() > 127) {
	            // If the next spot up can't support a ladder, this is a one block step, so place a stair block
	            setBlockState(worldReaderIn, pos, TropicraftBlocks.THATCH_STAIRS.get().getDefaultState().with(StairsBlock.FACING, dir));
        	}
        } else {
            // Otherwise, place ladders upwards until we find air (bridging over an initial gap if required)
            while (bridgeTo >= pos.getY() || canPlaceLadderAt(worldReaderIn, pos, dir) != null) {
                setBlockState(worldReaderIn, pos, ladder);
                setBlockState(worldReaderIn, pos.offset(dir), TropicraftBlocks.THATCH_BUNDLE.get().getDefaultState());
                pos = pos.up();
            }
        }
        return blockInfo;//new BlockInfo(blockInfo.pos, debugState, blockInfo.nbt);
    }
    
    // Check that there is a solid block behind the ladder at this pos, and return the correct ladder state
    // Returns null if placement is not possible
    private BlockState canPlaceLadderAt(IWorldReader worldReaderIn, BlockPos pos, Direction dir) {
        BlockPos check = pos.offset(dir);
        BlockState state = worldReaderIn.getBlockState(check);
        if (!state.isAir(worldReaderIn, check)) {
            BlockState ladderState = getLadderState(dir);
            if (ladderState.isValidPosition(worldReaderIn, pos)) {
                return ladderState;
            }
        }
        return null;
    }
    
    private BlockState getLadderState(Direction dir) {
        return TropicraftBlocks.BAMBOO_LADDER.get().getDefaultState().with(LadderBlock.FACING, dir.getOpposite());
    }
    
    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }

}
