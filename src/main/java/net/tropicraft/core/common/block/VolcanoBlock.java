package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.tropicraft.core.common.block.tileentity.VolcanoTileEntity;

import javax.annotation.Nullable;

public class VolcanoBlock extends Block implements ITileEntityProvider {

    public VolcanoBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(Integer.MAX_VALUE));
        // TODO: 1.14 - make unbreakable?
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(final IBlockReader world) {
        return new VolcanoTileEntity();
    }
}
