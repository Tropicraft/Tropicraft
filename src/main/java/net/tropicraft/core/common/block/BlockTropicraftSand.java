package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;

public class BlockTropicraftSand extends FallingBlock {
    public static final BooleanProperty UNDERWATER = BooleanProperty.create("underwater");

    public BlockTropicraftSand(final Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(UNDERWATER, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UNDERWATER);
    }
    
    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return Blocks.SAND.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final FluidState upState = context.getWorld().getFluidState(context.getPos().up());
        boolean waterAbove = false;
        if (!upState.isEmpty()) {
            waterAbove = true;
        }
        return this.getDefaultState().with(UNDERWATER, waterAbove);
    }

    @Override
    @Deprecated
    public void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos pos2, boolean isMoving) {
        final FluidState upState = world.getFluidState(pos.up());
        boolean underwater = upState.getFluid().isEquivalentTo(Fluids.WATER);
        if (underwater != state.get(UNDERWATER)) {
            world.setBlockState(pos, state.with(UNDERWATER, underwater), Constants.BlockFlags.BLOCK_UPDATE);
        }
        super.neighborChanged(state, world, pos, block, pos2, isMoving);
    }
}
