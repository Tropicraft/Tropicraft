package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.tropicraft.info.TCNames;

public class BlockChunkOHead extends BlockTropicraft {

	public BlockChunkOHead() {
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName(TCNames.chunkOHead);
		this.setHardness(2.0F);
		this.setResistance(30F);
	}

}
