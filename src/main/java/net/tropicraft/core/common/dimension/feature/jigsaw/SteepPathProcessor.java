package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.TropicraftDimension;

public class SteepPathProcessor extends PathStructureProcessor {
    public static final Codec<SteepPathProcessor> CODEC = Codec.unit(new SteepPathProcessor());

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos seedPos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, StructureTemplate template) {
        BlockPos pos = blockInfo.pos();

        if (originalBlockInfo.pos().getY() != 1 || originalBlockInfo.state().getBlock() == TropicraftBlocks.BAMBOO_STAIRS.get() || originalBlockInfo.state().isAir()) {
            return blockInfo;
        }

        Direction.Axis axis = getPathDirection(level, seedPos, blockInfo, placementSettingsIn, template);
        if (axis == null) {
            return blockInfo;
        }
        
        // If this is true, we are "bridging" upwards past an air gap, handles overhangs
        int bridgeTo = -1;
        
        BlockState ladder = null;
        for (Direction.AxisDirection axisDir : Direction.AxisDirection.values()) {
            Direction dir = Direction.get(axisDir, axis);
            // Detect an overhang by checking if the heightmap between spots differs by >2
            BlockPos nextHeight = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.relative(dir)).below();
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
        Direction dir = ladder.getValue(LadderBlock.FACING).getOpposite();
        pos = pos.above();
        if (bridgeTo == pos.getY() && canPlaceLadderAt(level, pos.above(), dir) == null) {
        	if (pos.getY() > TropicraftDimension.SEA_LEVEL) {
	            // If the next spot up can't support a ladder, this is a one block step, so place a stair block
	            setBlockState(level, pos, TropicraftBlocks.THATCH_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, dir));
        	}
        } else {
            // Otherwise, place ladders upwards until we find air (bridging over an initial gap if required)
            while (bridgeTo >= pos.getY() || canPlaceLadderAt(level, pos, dir) != null) {
                setBlockState(level, pos, ladder);
                setBlockState(level, pos.relative(dir), TropicraftBlocks.THATCH_BUNDLE.get().defaultBlockState());
                pos = pos.above();
            }
        }
        return blockInfo;//new BlockInfo(blockInfo.pos, debugState, blockInfo.nbt);
    }
    
    // Check that there is a solid block behind the ladder at this pos, and return the correct ladder state
    // Returns null if placement is not possible
    private BlockState canPlaceLadderAt(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos check = pos.relative(dir);
        BlockState state = level.getBlockState(check);
        if (!state.isAir()) {
            BlockState ladderState = getLadderState(dir);
            if (ladderState.canSurvive(level, pos)) {
                return ladderState;
            }
        }
        return null;
    }
    
    private BlockState getLadderState(Direction dir) {
        return TropicraftBlocks.BAMBOO_LADDER.get().defaultBlockState().setValue(LadderBlock.FACING, dir.getOpposite());
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.STEEP_PATH.get();
    }

}
