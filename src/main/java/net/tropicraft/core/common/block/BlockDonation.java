package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockDonation extends Block implements ITileEntityProvider {

	public BlockDonation() {
		super(Material.ROCK);
		this.setCreativeTab(null);
		this.setBlockUnbreakable();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return TileEntityFactory.getDonationTE();
	}

}
