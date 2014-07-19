package net.tropicraft.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSifter extends BlockTropicraft implements ITileEntityProvider {

    public BlockSifter() {
        super(Material.wood);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
