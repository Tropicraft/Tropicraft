package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class PineappleBlock extends TallFlowerBlock implements IGrowable, IPlantable {

    /** Number of total random ticks it takes for this pineapple to grow */
    public static final int TOTAL_GROW_TICKS = 7;

    public static final IntegerProperty STAGE = BlockStateProperties.AGE_0_7;

    public PineappleBlock(final Properties properties) {
        super(properties);
        this.setDefaultState(super.getDefaultState().with(STAGE, 0));
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF, STAGE);
    }

    @Override
    public boolean canGrow(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState, boolean b) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState blockState) {
        return false;
    }

    @Override
    public void grow(final World world, final Random random, final BlockPos pos, final BlockState state) {
        final int currentStage = state.get(STAGE);
        if (currentStage < TOTAL_GROW_TICKS) {
            final BlockState growthState = state.with(STAGE, currentStage + 1);
            world.setBlockState(pos, growthState, 4);
        } else {
            final BlockState above = world.getBlockState(pos.up());

            // Don't bother placing if it's already there
            if (above.getBlock() == this) return;
            if (state.get(HALF) == DoubleBlockHalf.UPPER) return;

            // Place actual pineapple plant above stem
            final BlockState fullGrowth = state.with(HALF, DoubleBlockHalf.UPPER);
            world.setBlockState(pos.up(), fullGrowth, 3);
        }
    }

    @Override
    public void tick(final BlockState state, final World world, final BlockPos pos, final Random random) {
        checkFlowerChange(world, pos, state);
        if (pos.getY() > world.getHeight() - 2) {
            return;
        }

        // Current metadata
        int growth = state.get(STAGE);

        if (state.getBlock() == this && growth <= TOTAL_GROW_TICKS && world.isAirBlock(pos.up()) && state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (growth >= TOTAL_GROW_TICKS - 1) {
                // Set current state
                BlockState growthState = state.with(STAGE, TOTAL_GROW_TICKS);
                world.setBlockState(pos, growthState, 4);

                // Place actual pineapple plant above stem
                BlockState fullGrowth = state.with(HALF, DoubleBlockHalf.UPPER);
                world.setBlockState(pos.up(), fullGrowth, 3);
            } else {
                BlockState growthState = state.with(STAGE, growth + 1);
                world.setBlockState(pos, growthState, 4);
            }
        }
    }

    private boolean isFullyGrown(BlockState state) {
        return state.get(STAGE) == TOTAL_GROW_TICKS || state.get(HALF) == DoubleBlockHalf.UPPER;
    }

    @Deprecated
    public void neighborChanged(BlockState state, World world, BlockPos pos1, Block block, BlockPos pos2, boolean b) {
        super.neighborChanged(state, world, pos1, block, pos2, b);

        checkForDrop(world, pos1, state);
    }

    protected final boolean checkForDrop(World world, BlockPos pos, BlockState state) {
        if (canBlockStay(world, pos, state)) {
            return true;
        } else {
            spawnDrops(state, world, pos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            return false;
        }
    }

    protected void checkFlowerChange(World world, BlockPos pos, BlockState state) {
        if (!world.isRemote && !canBlockStay(world, pos, state)) {
            if (isFullyGrown(state)) {
                spawnDrops(state, world, pos);
            }
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        } else {
            BlockState iblockstate = worldIn.getBlockState(pos.up());
            if (iblockstate.getBlock() == this) {
                return false;
            } else { // If just the stem
                return canPlaceBlockAt(worldIn, pos);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();

        if (block.canSustainPlant(state, worldIn, pos.down(), Direction.UP, this)) {
            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        // override super behavior of placing top half of double flower by default
    }
}
