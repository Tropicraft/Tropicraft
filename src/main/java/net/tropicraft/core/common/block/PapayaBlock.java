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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class PapayaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    public static final MapCodec<PapayaBlock> CODEC = simpleCodec(PapayaBlock::new);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    protected static final VoxelShape EAST_AABB = Block.box(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D);
    protected static final VoxelShape WEST_AABB = Block.box(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D);

    public PapayaBlock(Block.Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    protected MapCodec<PapayaBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 2;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        int i = state.getValue(AGE);
        if (i < 1 && worldIn.random.nextInt(5) == 0) {
            worldIn.setBlock(pos, state.setValue(AGE, i + 1), 2);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Block block = level.getBlockState(pos.relative(state.getValue(HORIZONTAL_FACING))).getBlock();
        return block == TropicraftBlocks.PAPAYA_LOG.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (state.getValue(HORIZONTAL_FACING)) {
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            default -> NORTH_AABB;
        };
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = defaultBlockState();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(HORIZONTAL_FACING, direction);
                if (blockstate.canSurvive(level, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == stateIn.getValue(HORIZONTAL_FACING) && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < 1;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        worldIn.setBlock(pos, state.setValue(AGE, 1), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, AGE);
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }
}
