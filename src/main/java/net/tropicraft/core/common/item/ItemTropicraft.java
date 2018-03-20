package net.tropicraft.core.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTropicraft extends Item {

	public ItemTropicraft() {

	}
	
	// Expose this method as public
	@Override
	public boolean isInCreativeTab(CreativeTabs targetTab) {
	    return super.isInCreativeTab(targetTab);
	}
}
