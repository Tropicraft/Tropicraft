package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PineappleBlock extends TallFlowerBlock implements BonemealableBlock {

    /**
     * Number of total random ticks it takes for this pineapple to grow
     */
    public static final int TOTAL_GROW_TICKS = 7;

    public static final IntegerProperty STAGE = BlockStateProperties.AGE_7;

    public PineappleBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(super.defaultBlockState().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, STAGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockState) {
        return blockState.getBlock() == TropicraftBlocks.PINEAPPLE.get() && blockState.getValue(PineappleBlock.HALF) == DoubleBlockHalf.LOWER && level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel world, final RandomSource random, final BlockPos pos, final BlockState state) {
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
    public void tick(final BlockState state, final ServerLevel world, final BlockPos pos, final RandomSource random) {
        if (pos.getY() > world.getMaxBuildHeight() - 2) {
            return;
        }

        // Current metadata
        int growth = state.getValue(STAGE);

        if (state.getBlock() == this && growth <= TOTAL_GROW_TICKS && world.isEmptyBlock(pos.above()) && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (growth >= TOTAL_GROW_TICKS - 1) {
                // Set current state
                BlockState growthState = state.setValue(STAGE, TOTAL_GROW_TICKS);
                world.setBlock(pos, growthState, Block.UPDATE_ALL | Block.UPDATE_NONE);

                // Place actual pineapple plant above stem
                BlockState fullGrowth = growthState.setValue(HALF, DoubleBlockHalf.UPPER);
                world.setBlock(pos.above(), fullGrowth, Block.UPDATE_ALL);
            } else {
                BlockState growthState = state.setValue(STAGE, growth + 1);
                world.setBlock(pos, growthState, Block.UPDATE_ALL | Block.UPDATE_NONE);
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return super.playerWillDestroy(worldIn, pos, state, player);
        } else {
            worldIn.levelEvent(player, 2001, pos, getId(state));
            dropResources(state, worldIn, pos, null, player, player.getMainHandItem());
            return state;
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (canSurvive(stateIn, worldIn, currentPos)) {
            return stateIn;
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.below()).getBlock() == TropicraftBlocks.PINEAPPLE.get();
        } else {
            return canPlaceBlockAt(worldIn, pos, state);
        }
    }

    private boolean canPlaceBlockAt(LevelReader worldIn, BlockPos pos, BlockState state) {
        final BlockState belowState = worldIn.getBlockState(pos.below());
        return belowState.getBlock().canSustainPlant(belowState, worldIn, pos.below(), Direction.UP, state).isTrue();
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // override super behavior of placing top half of double flower by default
    }
}
