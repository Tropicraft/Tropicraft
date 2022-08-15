package net.tropicraft.core.common.block.experimental;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.Random;

@SuppressWarnings("NullableProblems")
public class TropicraftExperimentalLeaveBlock extends LeavesBlock {

    private static final int MAX_DISTANCE = 20;

    private final int maxDistanceBeforeDecay;

    public IntegerProperty CUSTOM_DISTANCE;

    //Property to check if there has even been a block update to prevent unwanted decay instantly
    public static BooleanProperty TEMPORARY_IGNORE_DECAY = BooleanProperty.create("temp_ignore_decay");

    public TropicraftExperimentalLeaveBlock(Properties settings, int maxDistance){
        super(settings);

        //Cursedness inbound
        this.CUSTOM_DISTANCE = IntegerProperty.create("custom_distance", 1, maxDistance);
        this.maxDistanceBeforeDecay = maxDistance;

        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);

        this.createBlockStateDefinition(builder);
        builder.add(CUSTOM_DISTANCE);

        ObfuscationReflectionHelper.setPrivateValue(Block.class, this, builder.create(Block::defaultBlockState, BlockState::new), "f_49792_");

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PERSISTENT, false)
                .setValue(CUSTOM_DISTANCE, maxDistanceBeforeDecay)
                .setValue(TEMPORARY_IGNORE_DECAY, false));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) { //*
        return state.getValue(CUSTOM_DISTANCE) == maxDistanceBeforeDecay && !(Boolean)state.getValue(PERSISTENT);
    }

    //Similar to LeavesBlock Random tick but just checks for the changed Distance
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        //Checks for leaf's extra distance and also checks the first cycle value to prevent random decay to happen prematurely
        if (!state.getValue(PERSISTENT) && state.getValue(CUSTOM_DISTANCE) == maxDistanceBeforeDecay && state.getValue(TEMPORARY_IGNORE_DECAY)) {
            dropResources(state, world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        world.setBlock(pos, updateDistanceFromLeaves(state, world, pos, this), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos currentPos, BlockPos neighborPos) {
        int i = getDistanceFromLeave(neighborState, isBlackListedLogPos(currentPos, neighborPos), this) + 1;
        if (i != 1 || state.getValue(CUSTOM_DISTANCE) >= i) {
            world.scheduleTick(currentPos, this, 1);
        }

        return state;
    }

    //Used to update diagonal or Indirect blocks to propagate the extra distance value for our leaves
    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int maxUpdateDepth) {
        for(BlockPos neighbourPos : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))){
            if(neighbourPos.equals(pos)) continue;

            BlockState neighbourState = world.getBlockState(neighbourPos);
            if (neighbourState != Blocks.AIR.defaultBlockState() && neighbourState.getBlock() == this) {
                BlockState updatedNeighborState = updateShape(neighbourState, Direction.UP, state, world, neighbourPos, pos); //isBlackListedLogPos(dx, dy, dz)
                Block.updateOrDestroy(neighbourState, updatedNeighborState, world, neighbourPos, flags, maxUpdateDepth);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { //*
        builder.add(PERSISTENT, TEMPORARY_IGNORE_DECAY); //, (!hasCustomBeenSet ? IntegerProperty.create("temp_1", 0, 1) : CUSTOM_DISTANCE)
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return updateDistanceFromLeaves(this.defaultBlockState().setValue(PERSISTENT, Boolean.valueOf(true)), pContext.getLevel(), pContext.getClickedPos(), this);
    }

    //------------------------------------------------------------------------------------------------

    private static BlockState updateDistanceFromLeaves(BlockState state, LevelAccessor world, BlockPos pos, TropicraftExperimentalLeaveBlock block) {
        int currentExtraDistance = block.maxDistanceBeforeDecay;

        for(BlockPos neighborPos : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))){
            if(neighborPos.equals(pos)) continue;

            int distanceFromOrigin = neighborPos.distManhattan(pos);

            currentExtraDistance = Math.min(currentExtraDistance, getDistanceFromLeave(world.getBlockState(neighborPos), isBlackListedLogPos(neighborPos, pos), block) + distanceFromOrigin); //isBlackListedLogPos(dx, dy, dz)
            if (currentExtraDistance == 1) {
                break;
            }
        }

        return state.setValue(block.CUSTOM_DISTANCE, currentExtraDistance).setValue(TEMPORARY_IGNORE_DECAY, true);
    }

    //Used as to get distance info from log block if not blacklisted and distance info from all leaf block's around it
    private static int getDistanceFromLeave(BlockState state, boolean blackListBlock, TropicraftExperimentalLeaveBlock block) {
        return (state.is(BlockTags.LOGS) && !blackListBlock) ? 0 : ((state.getBlock() instanceof TropicraftExperimentalLeaveBlock) ? state.getValue(block.CUSTOM_DISTANCE) : block.maxDistanceBeforeDecay);
    }

    /**
     * Used to black list logs in the bottom layer of checks surrounding the bottom block.
     *
     * There is an issue if you include a ring of logs under a central block, it will cause
     * the leave block never to be properly updated due to the logs not diagonally block updating the leaves
     */

    private static boolean isBlackListedLogPos(BlockPos currentPos, BlockPos neighborPos){
        int dy = neighborPos.getY() - currentPos.getY();
        int dx = neighborPos.getX() - currentPos.getX();
        int dz = neighborPos.getZ() - currentPos.getZ();

        return dy == -1 && !(dx == 0 && dz == 0);
    }

}
