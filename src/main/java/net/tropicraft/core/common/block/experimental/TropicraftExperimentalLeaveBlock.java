package net.tropicraft.core.common.block.experimental;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Random;

public class TropicraftExperimentalLeaveBlock extends LeavesBlock {
    public static IntegerProperty EXTRADISTANCE;
    public static BooleanProperty TEMPIGNORERANDOMTICK; //Property to check if there has even been a block update to prevent unwanted decay instantly

    public TropicraftExperimentalLeaveBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(DISTANCE, 7).setValue(EXTRADISTANCE, 20).setValue(TEMPIGNORERANDOMTICK, false));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) { //*
        //Checks if it is set Persistent but also checks for the custom leaf distance
        return state.getValue(EXTRADISTANCE) == 20 && !(Boolean)state.getValue(PERSISTENT);
    }

    //Similar to LeavesBlock Random tick but just checks for the changed Distance
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        //Checks for leaf's extra distance and also checks the first cycle value to prevent random decay to happen prematurely
        if (!state.getValue(PERSISTENT) && state.getValue(EXTRADISTANCE) == 20 && state.getValue(TEMPIGNORERANDOMTICK)) {
            dropResources(state, world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        world.setBlock(pos, updateDistanceFromLeaves(state, world, pos), 3);
    }

    public BlockState updateShapeBlackList(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos, boolean blackListActive) {
        int i = getDistanceFromLeave(neighborState, blackListActive) + 1;
        if (i != 1 || state.getValue(EXTRADISTANCE) >= i) {
            world.getBlockTicks().scheduleTick(pos, this, 1);
        }

        return state;
    }

    // Similar to base leaf block but with the diagonal directions
    private static BlockState updateDistanceFromLeaves(BlockState state, LevelAccessor world, BlockPos pos) { //*
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        DiagonalDirections[] directionDiagonals = DiagonalDirections.values();

        int currentExtraDistance = 20; //*
        int length = directionDiagonals.length;

        for(int i = 0; i < length; ++i) {
            DiagonalDirections direction = directionDiagonals[i];
            mutable.setWithOffset(pos, direction.getX(), direction.getY(), direction.getZ());
            currentExtraDistance = Math.min(currentExtraDistance, getDistanceFromLeave(world.getBlockState(mutable), isBlackListedLogPos(direction)) + direction.additionAmount);
            if (currentExtraDistance == 1) {
                break;
            }
        }

        return state.setValue(EXTRADISTANCE, currentExtraDistance).setValue(TEMPIGNORERANDOMTICK, true);
    }

    //Used to update diagonal or Indirect blocks to propagate the extra distance value for our leaves
    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int maxUpdateDepth) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        DiagonalDirections[] directions = DiagonalDirections.values();
        int LENGTH = directions.length;

        for (int index = 0; index < LENGTH; ++index) {
            DiagonalDirections direction = directions[index];
            mutable.setWithOffset(pos, direction.getX(), direction.getY(), direction.getZ());
            BlockState blockState = world.getBlockState(mutable);
            if (blockState != Blocks.AIR.defaultBlockState() && blockState.getBlock() == this) {
                BlockState blockState2 = updateShapeBlackList(blockState, Direction.UP, state, world, mutable, pos, isBlackListedLogPos(direction));
                Block.updateOrDestroy(blockState, blockState2, world, mutable, flags, maxUpdateDepth);
            }
        }
    }

    //Used as to get distance info from log block if not blacklisted and distance info from all leaf block's around it
    private static int getDistanceFromLeave(BlockState state, boolean blackListBlock) {
        return (BlockTags.LOGS.contains(state.getBlock()) && !blackListBlock) ? 0 : ((state.getBlock() instanceof TropicraftExperimentalLeaveBlock) ? state.getValue(EXTRADISTANCE) : 20);
    }

    //Used to black list logs in the bottom layer of checks surrounding the bottom block
    private static boolean isBlackListedLogPos(DiagonalDirections direction){
        return direction.getY() == -1 && !(direction.getX() == 0 && direction.getZ() == 0);
    }

    //Still holds the default Property values of leaves just encase
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { //*
        builder.add(new Property[]{DISTANCE, PERSISTENT, EXTRADISTANCE, TEMPIGNORERANDOMTICK});
    }

    static {
        EXTRADISTANCE = IntegerProperty.create("extradistance", 1, 20);
        TEMPIGNORERANDOMTICK = BooleanProperty.create("pastfirstcycle");
    }
}
