package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
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
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new VolcanoTileEntity();
    }
}
