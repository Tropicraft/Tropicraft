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

    public static final IntegerProperty STAGE = BlockStateProperties.AGE_7;

    public PineappleBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(super.defaultBlockState().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF, STAGE);
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState blockState, boolean b) {
        return blockState.getBlock() == TropicraftBlocks.PINEAPPLE.get() && blockState.getValue(PineappleBlock.HALF) == DoubleBlockHalf.LOWER && world.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(World world, Random random, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerWorld world, final Random random, final BlockPos pos, final BlockState state) {
        final int currentStage = state.getValue(STAGE);
        if (currentStage < TOTAL_GROW_TICKS) {
            final BlockState growthState = state.setValue(STAGE, currentStage + 1);
            world.setBlock(pos, growthState, 4);
        } else {
            final BlockState above = world.getBlockState(pos.above());

            // Don't bother placing if it's already there
            if (above.getBlock() == this) return;
            if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return;

            // Place actual pineapple plant above stem
            final BlockState fullGrowth = state.setValue(HALF, DoubleBlockHalf.UPPER);
            world.setBlock(pos.above(), fullGrowth, 3);
        }
    }

    @Override
    public void tick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        if (pos.getY() > world.getMaxBuildHeight() - 2) {
            return;
        }

        // Current metadata
        int growth = state.getValue(STAGE);

        if (state.getBlock() == this && growth <= TOTAL_GROW_TICKS && world.isEmptyBlock(pos.above()) && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (growth >= TOTAL_GROW_TICKS - 1) {
                // Set current state
                BlockState growthState = state.setValue(STAGE, TOTAL_GROW_TICKS);
                world.setBlock(pos, growthState, Constants.BlockFlags.DEFAULT | Constants.BlockFlags.NO_RERENDER);

                // Place actual pineapple plant above stem
                BlockState fullGrowth = growthState.setValue(HALF, DoubleBlockHalf.UPPER);
                world.setBlock(pos.above(), fullGrowth, Constants.BlockFlags.DEFAULT);
            } else {
                BlockState growthState = state.setValue(STAGE, growth + 1);
                world.setBlock(pos, growthState, Constants.BlockFlags.DEFAULT | Constants.BlockFlags.NO_RERENDER);
            }
        }
    }
    
    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            super.playerWillDestroy(worldIn, pos, state, player);
        } else {
            worldIn.levelEvent(player, 2001, pos, getId(state));
            dropResources(state, worldIn, pos, null, player, player.getMainHandItem());
        }
    }
    
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (canSurvive(stateIn, worldIn, currentPos)) {
            return stateIn;
        }
        return Blocks.AIR.defaultBlockState();
    }
    
    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.below()).getBlock() == TropicraftBlocks.PINEAPPLE.get();
        } else {
            return canPlaceBlockAt(worldIn, pos);
        }
    }

    private boolean canPlaceBlockAt(IWorldReader worldIn, BlockPos pos) {
        final BlockState belowState = worldIn.getBlockState(pos.below());
        return belowState.getBlock().canSustainPlant(belowState, worldIn, pos.below(), Direction.UP, this);
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // override super behavior of placing top half of double flower by default
    }
}
