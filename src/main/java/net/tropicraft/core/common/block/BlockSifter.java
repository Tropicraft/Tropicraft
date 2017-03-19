package net.tropicraft.core.common.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockSifter extends BlockTropicraft implements ITileEntityProvider {

	public BlockSifter(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;///TileEntityFactory.getSifterTE();
	}

}
