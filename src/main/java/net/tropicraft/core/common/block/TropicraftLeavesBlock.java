package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class TropicraftLeavesBlock extends LeavesBlock {
    public static final MapCodec<TropicraftLeavesBlock> CODEC = simpleCodec(TropicraftLeavesBlock::new);

    public static final List<BlockPos> AROUND_OFFSETS = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
            .map(BlockPos::immutable)
            .filter(pos -> !pos.equals(BlockPos.ZERO))
            .toList();
    public static final List<BlockPos> INDIRECT_NEIGHBOR_OFFSETS = AROUND_OFFSETS.stream()
            .filter(pos -> pos.distManhattan(BlockPos.ZERO) > 1)
            .toList();

    // TODO: Remove and datafix with a version bump - new_decay=false -> persistent=true
    public static final BooleanProperty NEW_DECAY = BooleanProperty.create("new_decay");

    public TropicraftLeavesBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(NEW_DECAY, false).setValue(DISTANCE, DECAY_DISTANCE).setValue(PERSISTENT, false).setValue(WATERLOGGED, false));
    }

    @Override
    public MapCodec<TropicraftLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, updateDistance(state, level, pos), Block.UPDATE_ALL);
    }

    @Override
    protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int flags, int recursionLeft) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Vec3i offset : INDIRECT_NEIGHBOR_OFFSETS) {
            mutablePos.setWithOffset(pos, offset);
            if (level.getBlockState(mutablePos).is(this)) {
                // Note: direction is arbitrary, as we only propagate the shape update to neighboring leaves which ignore it
                level.neighborShapeChanged(Direction.DOWN, state, mutablePos, pos, flags, recursionLeft);
            }
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        int newDistance = getDistanceAt(facingState) + 1;
        if (newDistance != 1 || state.getValue(DISTANCE) != newDistance) {
            level.scheduleTick(currentPos, this, 1);
        }

        return state;
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int minDistance = DECAY_DISTANCE;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Vec3i offset : AROUND_OFFSETS) {
            mutablePos.setWithOffset(pos, offset);
            minDistance = Math.min(minDistance, getDistanceAt(level.getBlockState(mutablePos)) + 1);
            if (minDistance == 1) {
                break;
            }
        }
        return state.setValue(DISTANCE, minDistance);
    }

    private static int getDistanceAt(BlockState neighbor) {
        return getOptionalDistanceAt(neighbor).orElse(DECAY_DISTANCE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = defaultBlockState().setValue(PERSISTENT, true).setValue(NEW_DECAY, true).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return updateDistance(blockState, context.getLevel(), context.getClickedPos());
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(NEW_DECAY);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(NEW_DECAY)) {
            super.randomTick(state, level, pos, random);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, NEW_DECAY);
    }
}
