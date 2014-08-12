package net.tropicraft.economy;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

/* A class to assign values to ItemStacks with certain conditions, defaults to only compare item ID, supports for comparing damage, future for NBT when needed
 * Supports marking entry as buyable */
public class ItemValues {

	public static ArrayList<ItemEntry> items = new ArrayList<ItemEntry>();
	public static ArrayList<ItemEntry> itemsBuyable = new ArrayList<ItemEntry>();
	
	/* Add a basic non buyable entry */
	public static void addEntry(ItemStack parItem, int value) {
		addEntry(parItem, value, false);
	}
	
	/* Add a buyable entry that uses item damage to differenciate between items */
	public static void addEntryBuyable(ItemStack parItem, int value) {
		addEntry(parItem, value, true, false);
	}
	
	/* Add an entry that uses item damage to differenciate between items */
	public static void addEntry(ItemStack parItem, int value, boolean compareDamage) {
		addEntry(parItem, value, false, compareDamage);
	}
	
	public static void addEntryBuyable(ItemStack parItem, int value, boolean compareDamage) {
		addEntry(parItem, value, true, compareDamage);
	}
	
	/* Fully custom method */
	public static void addEntry(ItemStack parItem, int value, boolean buyable, boolean compareDamage) {
		ItemEntry ie = new ItemEntry(parItem, value);
		ie.compareDamage = compareDamage;
		items.add(ie);
		if (buyable) itemsBuyable.add(ie);
	}
	
	public static boolean getIsValuedItem(ItemStack parItem) {
		return getItemEntry(parItem, false) != null;
	}
	
	public static boolean getIsBuyableItem(ItemStack parItem) {
		return getItemEntry(parItem, true) != null;
	}
	
	public static ItemEntry getItemEntry(ItemStack parItem) {
		return getItemEntry(parItem, false);
	}
	
	public static ItemEntry getItemEntry(ItemStack parItem, boolean parNeedsBuyable) {
		//iterate list, look for match
		try {
			for (int i = 0; i < items.size(); i++) {
				ItemEntry ie = items.get(i);
				if (ie.matchesItem(parItem) && (!parNeedsBuyable || ie.buyable)) return ie;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
}
