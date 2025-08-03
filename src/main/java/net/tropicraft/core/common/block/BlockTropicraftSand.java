package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;

public class BlockTropicraftSand extends FallingBlock {
    public static final MapCodec<BlockTropicraftSand> CODEC = simpleCodec(BlockTropicraftSand::new);

    public static final BooleanProperty UNDERWATER = BooleanProperty.create("underwater");

    private final int dustColor;

    public BlockTropicraftSand(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(UNDERWATER, false));
        dustColor = ARGB.opaque(defaultMapColor().col);
    }

    @Override
    protected MapCodec<? extends BlockTropicraftSand> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNDERWATER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState upState = context.getLevel().getFluidState(context.getClickedPos().above());
        return defaultBlockState().setValue(UNDERWATER, !upState.isEmpty());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction neighborDirection, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (neighborDirection == Direction.UP) {
            state = state.setValue(UNDERWATER, neighborState.getFluidState().is(FluidTags.WATER));
        }
        return super.updateShape(state, level, tickAccess, pos, neighborDirection, neighborPos, neighborState, random);
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return dustColor;
    }
}
