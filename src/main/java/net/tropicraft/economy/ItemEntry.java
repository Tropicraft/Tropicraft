package net.tropicraft.economy;

import net.minecraft.item.ItemStack;

public class ItemEntry {

	public ItemStack item = null;
	public int value = 0;
	public boolean buyable = false;
	
	//Extra stuff
	public boolean compareNBT = false;
	public boolean compareDamage = false;
	
	public ItemEntry(ItemStack parItem, int parValue) {
		item = parItem;
		value = parValue;
	}
	
	public boolean matchesItem(ItemStack parItem) {
		if (compareNBT && !item.isItemEqual(parItem)) return false;
		if (compareDamage && item.getItemDamage() != parItem.getItemDamage()) return false;
		if (item != parItem) return false;
		return true;
	}
	
	/* gets the price for the full stack, accounting for price per stack amount, and the durability of the item if compareDamage == false */
	public int getTotalValue(ItemStack parItem) {
		double val = value;
		double priceFactor = value / (Math.max(1, item.stackSize));
		
		if (compareDamage == false && parItem.getMaxDamage() != 0) {
			val *= ((double)(parItem.getMaxDamage() - parItem.getItemDamage()) / (double)parItem.getMaxDamage());
		} else if (item.stackSize > 1) { 
			//priceFactor = 
			//val /= item.stackSize * parItem.stackSize;
		}
		
		if (parItem.stackSize > 1) {
			val = priceFactor * parItem.stackSize;
		}
		
		return (int) val;
	}
}
