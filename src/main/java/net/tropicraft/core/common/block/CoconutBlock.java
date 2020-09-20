package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class CoconutBlock extends DirectionalBlock {
    private static final VoxelShape COCONUT_AABB = Block.makeCuboidShape(4, 0.0D, 4, 12, 10, 12);

    public CoconutBlock(final Properties properties) {
        super(properties);
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Direction dir = state.get(FACING);
		BlockPos checkPos = pos.offset(dir);
		return worldIn.getBlockState(checkPos).getBlock() == Blocks.GRINDSTONE // coconut yeeters allowed
				|| Block.hasEnoughSolidSide(worldIn, checkPos, dir.getOpposite());
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return stateIn.get(FACING) == facing && !stateIn.isValidPosition(worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return COCONUT_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return COCONUT_AABB;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
       return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.util.Mirror mirrorIn) {
       return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
       return this.getDefaultState().with(FACING, context.getFace().getOpposite());
    }
}
