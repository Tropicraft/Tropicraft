package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockChunkOHead extends BlockTropicraft {

	public BlockChunkOHead() {
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("chunk");
	}

	public BlockChunkOHead(Material material) {
		super(material);
	}

}
