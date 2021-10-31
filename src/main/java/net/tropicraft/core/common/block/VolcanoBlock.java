package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.block.tileentity.VolcanoTileEntity;

import javax.annotation.Nullable;

public class VolcanoBlock extends Block implements EntityBlock {

    public VolcanoBlock(Block.Properties properties) {
        super(properties);
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new VolcanoTileEntity(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == TropicraftTileEntityTypes.VOLCANO.get() ? (world1, pos, state1, be) -> VolcanoTileEntity.tick(world1, pos, state1, (VolcanoTileEntity) be) : null;
    }
}
