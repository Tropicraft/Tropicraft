package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.tropicraft.core.common.block.tileentity.VolcanoTileEntity;

public class VolcanoBlock extends Block {

    public VolcanoBlock(Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new VolcanoTileEntity();
    }
}
