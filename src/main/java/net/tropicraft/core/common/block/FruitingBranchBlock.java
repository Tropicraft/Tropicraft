package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public final class FruitingBranchBlock extends Block implements BonemealableBlock {
    public static final MapCodec<FruitingBranchBlock> CODEC = simpleCodec(FruitingBranchBlock::new);

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final int MAX_AGE = 2;
    private static final int GROW_CHANCE = 10;

    private static final VoxelShape NORTH_SHAPE = Block.box(4.0, 4.0, 2.0, 12.0, 12.0, 16.0);
    private static final VoxelShape SOUTH_SHAPE = Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 14.0);
    private static final VoxelShape WEST_SHAPE = Block.box(2.0, 4.0, 4.0, 16.0, 12.0, 12.0);
    private static final VoxelShape EAST_SHAPE = Block.box(0.0, 4.0, 4.0, 14.0, 12.0, 12.0);

    public FruitingBranchBlock(final Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    protected MapCodec<FruitingBranchBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> throw new UnsupportedOperationException();
        };
    }

    @Override
    public void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        if (random.nextInt(GROW_CHANCE) == 0) {
            level.setBlockAndUpdate(pos, state.cycle(AGE));
        }
    }

    @Override
    public boolean isRandomlyTicking(final BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public BlockState updateShape(final BlockState state, final Direction direction, final BlockState neighborState, final LevelAccessor level, final BlockPos pos, final BlockPos neighborPos) {
        final Direction facing = state.getValue(FACING);
        if (direction == facing.getOpposite()) {
            return neighborState.isFaceSturdy(level, neighborPos, facing) ? state : Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        final Direction facing = state.getValue(FACING);
        final BlockPos attachPos = pos.relative(facing.getOpposite());
        return level.getBlockState(attachPos).isFaceSturdy(level, attachPos, facing);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        final Direction facing = context.getClickedFace();
        if (facing.getAxis() == Direction.Axis.Y) {
            return null;
        }
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    @Override
    public boolean isValidBonemealTarget(final LevelReader level, final BlockPos pos, final BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(final Level level, final RandomSource random, final BlockPos pos, final BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel level, final RandomSource random, final BlockPos pos, final BlockState state) {
        level.setBlock(pos, state.cycle(AGE), Block.UPDATE_CLIENTS);
    }
}
