package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

public class WaterBarrierBlock extends BarrierBlock implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected WaterBarrierBlock(Properties properties) {
        super(properties);
        setDefaultState(getDefaultState().with(WATERLOGGED, true));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(WATERLOGGED, true);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(WATERLOGGED);
    }

}
