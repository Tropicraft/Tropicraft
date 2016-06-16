package net.tropicraft.core.common.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.enums.TropicraftOres;

public class ItemTropicraftOreBlock extends ItemBlock {

	public ItemTropicraftOreBlock(Block block) {
		super(block);
		
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "tile.tropicraft:" + TropicraftOres.VALUES[itemstack.getMetadata()].getName();
	}

}
