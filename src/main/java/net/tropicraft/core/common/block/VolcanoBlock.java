package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.block.tileentity.TropicraftBlockEntityTypes;
import net.tropicraft.core.common.block.tileentity.VolcanoBlockEntity;

import javax.annotation.Nullable;

public class VolcanoBlock extends BaseEntityBlock {

    public VolcanoBlock(Block.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TropicraftBlockEntityTypes.VOLCANO.get(), VolcanoBlockEntity::volcanoTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VolcanoBlockEntity(pos, state);
    }
}
