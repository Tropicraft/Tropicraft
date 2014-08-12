package net.tropicraft.economy;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.tropicraft.registry.TCItemRegistry;

public class ItemValueEntries {
	
	public static ItemStack currency;

	public static void initEconomy() {
		currency = new ItemStack(TCItemRegistry.pearl, 0, 1);
		addBuyableEntries();
		addValuedEntries();
	}
	
	public static void addBuyableEntries() {
		
		//Buyables
		ItemValues.addEntryBuyable(new ItemStack(Items.fish), 1);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.freshMarlin), 3);
		//missing z gem
		//missing e gem
		//missing a gem
		//missing spear
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.scaleChestplate), 15);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.scaleLeggings), 10);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.scaleHelm), 5);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.scaleBoots), 5);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.coconutBomb, 3), 15);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.leafBall, 15), 20);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.blowGun), 20);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.dart, 10), 30, true);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.fishingNet), 15);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.cocktail), 5);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.snorkel), 15);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.flippers), 15);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.recordEasternIsles), 1);
		ItemValues.addEntryBuyable(new ItemStack(TCItemRegistry.recordTradeWinds), 1);
		
		/*addValuedItem(Item.fishRaw, 1);
		addValuedItem(TropicraftMod.zirconItem, 5);
		addValuedItem(TropicraftMod.eudialyteItem, 10);
		addValuedItem(TropicraftMod.azuriteItem, 15);
		
		addValuedItem(TropicraftMod.bambooSpear, 10);
		addValuedItem(TropicraftMod.scaleChestplate, 15);
		addValuedItem(TropicraftMod.scaleLeggings, 10);
		addValuedItem(TropicraftMod.scaleHelm, 5);
		addValuedItem(TropicraftMod.scaleBoots, 5);
		
		addValuedItem(TropicraftMod.coconutBomb, 15, 3);
		addValuedItem(TropicraftMod.leafBall, 20, 15);
		addValuedItem(TropicraftMod.blowGun, 20);
		addValuedItem(TropicraftMod.paraDart, 30, 10);
		addValuedItem(TropicraftMod.fishingNet, 15);
		addValuedItem(TropicraftMod.bambooSpear, 10);
		
		addValuedItem(TropicraftMod.bambooMugFull, 5);
		
		addValuedItem(TropicraftMod.snorkel, 15);
		addValuedItem(TropicraftMod.flippers, 15);
		
		addValuedItem(TropicraftMod.froxEasternIsles, 50);
		addValuedItem(TropicraftMod.froxTradeWinds, 50);*/
	}
	
	public static void addValuedEntries() {

		//Non-Buyables
		ItemValues.addEntry(currency, 1);
		
		for (int i = 0; i < TCItemRegistry.getShellDisplayNames().length; i++) ItemValues.addEntry(new ItemStack(TCItemRegistry.shells, 1, i), 1, true);
		
		/*addValuedItem(TropicraftMod.shellCommon1, 1, false);
		addValuedItem(TropicraftMod.shellCommon2, 1, false);
		addValuedItem(TropicraftMod.shellCommon3, 1, false);
		addValuedItem(TropicraftMod.turtleShell, 1, false);
		addValuedItem(TropicraftMod.shellRare1, 3, false);*/
		
	}
	
}
