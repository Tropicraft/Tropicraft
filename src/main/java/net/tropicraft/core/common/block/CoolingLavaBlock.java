package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.tropicraft.core.common.block.tileentity.CoolingLavaBlockEntity;

import javax.annotation.Nullable;

public class CoolingLavaBlock extends LiquidBlock implements EntityBlock {
    public static final MapCodec<LiquidBlock> CODEC = LiquidBlock.CODEC;

    public CoolingLavaBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public MapCodec<LiquidBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, TropicraftBlocks.COOLING_LAVA_ENTITY.get(),
                CoolingLavaBlockEntity::coolingLavaTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoolingLavaBlockEntity(TropicraftBlocks.COOLING_LAVA_ENTITY.get(), pos, state);
    }
}
