package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockTropicraftSand extends FallingBlock {
    public static final MapCodec<BlockTropicraftSand> CODEC = simpleCodec(BlockTropicraftSand::new);

    public static final BooleanProperty UNDERWATER = BooleanProperty.create("underwater");

    private final int dustColor;

    public BlockTropicraftSand(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(UNDERWATER, false));
        dustColor = defaultMapColor().col | 0xFF000000;
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
    @Deprecated
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos pos2, boolean isMoving) {
        FluidState upState = world.getFluidState(pos.above());
        boolean underwater = upState.getType().isSame(Fluids.WATER);
        if (underwater != state.getValue(UNDERWATER)) {
            world.setBlock(pos, state.setValue(UNDERWATER, underwater), Block.UPDATE_CLIENTS);
        }
        super.neighborChanged(state, world, pos, block, pos2, isMoving);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return dustColor;
    }
}
