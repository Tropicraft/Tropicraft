package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

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
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState blockState, boolean b) {
        return blockState.getBlock() == TropicraftBlocks.PINEAPPLE.get() && blockState.get(PineappleBlock.HALF) == DoubleBlockHalf.LOWER && world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void grow(final ServerWorld world, final Random random, final BlockPos pos, final BlockState state) {
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
    public void tick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        if (pos.getY() > world.getHeight() - 2) {
            return;
        }

        // Current metadata
        int growth = state.get(STAGE);

        if (state.getBlock() == this && growth <= TOTAL_GROW_TICKS && world.isAirBlock(pos.up()) && state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (growth >= TOTAL_GROW_TICKS - 1) {
                // Set current state
                BlockState growthState = state.with(STAGE, TOTAL_GROW_TICKS);
                world.setBlockState(pos, growthState, Constants.BlockFlags.DEFAULT | Constants.BlockFlags.NO_RERENDER);

                // Place actual pineapple plant above stem
                BlockState fullGrowth = growthState.with(HALF, DoubleBlockHalf.UPPER);
                world.setBlockState(pos.up(), fullGrowth, Constants.BlockFlags.DEFAULT);
            } else {
                BlockState growthState = state.with(STAGE, growth + 1);
                world.setBlockState(pos, growthState, Constants.BlockFlags.DEFAULT | Constants.BlockFlags.NO_RERENDER);
            }
        }
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            super.onBlockHarvested(worldIn, pos, state, player);
        } else {
            worldIn.playEvent(player, 2001, pos, getStateId(state));
            spawnDrops(state, worldIn, pos, null, player, player.getHeldItemMainhand());
        }
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (isValidPosition(stateIn, worldIn, currentPos)) {
            return stateIn;
        }
        return Blocks.AIR.getDefaultState();
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == TropicraftBlocks.PINEAPPLE.get();
        } else {
            return canPlaceBlockAt(worldIn, pos);
        }
    }

    private boolean canPlaceBlockAt(IWorldReader worldIn, BlockPos pos) {
        final BlockState belowState = worldIn.getBlockState(pos.down());
        return belowState.getBlock().canSustainPlant(belowState, worldIn, pos.down(), Direction.UP, this);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // override super behavior of placing top half of double flower by default
    }
}
