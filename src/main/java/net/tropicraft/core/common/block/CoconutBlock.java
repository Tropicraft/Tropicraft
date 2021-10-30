package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CoconutBlock extends DirectionalBlock {
    private static final VoxelShape COCONUT_AABB = Block.box(4, 0.0D, 4, 12, 10, 12);

    public CoconutBlock(final Properties properties) {
        super(properties);
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        BlockPos checkPos = pos.relative(dir);
        return worldIn.getBlockState(checkPos).getBlock() == Blocks.GRINDSTONE // coconut yeeters allowed
                || Block.canSupportCenter(worldIn, checkPos, dir.getOpposite());
    }

    @Override
    @Deprecated
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return stateIn.getValue(FACING) == facing && !stateIn.canSurvive(worldIn, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos, final CollisionContext context) {
        return COCONUT_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos, final CollisionContext context) {
        return COCONUT_AABB;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
       return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirrorIn) {
       return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
       return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }
}
