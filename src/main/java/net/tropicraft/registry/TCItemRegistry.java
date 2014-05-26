package net.tropicraft.registry;

import net.minecraft.item.Item;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemCoffeeBean;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.item.ItemTropicraftFood;
import net.tropicraft.item.ItemTropicraftMulti;
import net.tropicraft.item.ItemWaterWand;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCItemRegistry {

	public static final ItemTropicraft frogLeg = new ItemTropicraft();
	public static final ItemTropicraftFood cookedFrogLeg = new ItemTropicraftFood(2, 0.15F);
	public static final ItemTropicraft poisonFrogSkin = new ItemTropicraft();
	
	public static final ItemTropicraftFood freshMarlin = new ItemTropicraftFood(2, 0.3F);
	public static final ItemTropicraftFood searedMarlin = new ItemTropicraftFood(8, 0.65F);
	
	public static final ItemTropicraftFood grapefruit = new ItemTropicraftFood(2, 0.2F);
	public static final ItemTropicraftFood lemon = new ItemTropicraftFood(2, 0.2F);
	public static final ItemTropicraftFood lime = new ItemTropicraftFood(2, 0.2F);
	public static final ItemTropicraftFood orange = new ItemTropicraftFood(2, 0.2F);
	
	public static final ItemTropicraft scale = new ItemTropicraft();
	
	public static final ItemTropicraftFood coconutChunk = new ItemTropicraftFood(1, 0.1F);
	public static final ItemTropicraftFood pineappleCubes = new ItemTropicraftFood(1, 0.1F);
	
	public static final ItemTropicraft bambooStick = new ItemTropicraft();
	public static final ItemTropicraftFood seaUrchinRoe = new ItemTropicraftFood(3, 0.3F);
	
	public static final ItemTropicraft pearl = new ItemTropicraftMulti(TCNames.pearlNames);
	public static final ItemTropicraft ore = new ItemTropicraftMulti(TCNames.oreNames);
	
	public static final ItemTropicraft waterWand = new ItemWaterWand();
	public static final ItemTropicraft fishingNet = new ItemTropicraft();
	
	public static final ItemTropicraft coffeeBean = new ItemCoffeeBean();
	
	/**
	 * Register all the items
	 */
	public static void init() {
		registerItem(frogLeg, TCNames.frogLeg);
		registerItem(cookedFrogLeg, TCNames.cookedFrogLeg);
		registerItem(poisonFrogSkin, TCNames.poisonFrogSkin);
		
		registerItem(freshMarlin, TCNames.freshMarlin);
		registerItem(searedMarlin, TCNames.searedMarlin);
		
		registerItem(grapefruit, TCNames.grapefruit);
		registerItem(lemon, TCNames.lemon);
		registerItem(lime, TCNames.lime);
		registerItem(orange, TCNames.orange);
		
		registerItem(scale, TCNames.scale);
		
		registerItem(coconutChunk, TCNames.coconutChunk);
		registerItem(pineappleCubes, TCNames.pineappleCubes);
		
		registerItem(bambooStick, TCNames.bambooStick);
		registerItem(seaUrchinRoe, TCNames.seaUrchinRoe);
		
		registerItem(pearl, TCNames.pearl);
		registerItem(ore, TCNames.ore);
		
		registerItem(waterWand, TCNames.waterWand);
		registerItem(fishingNet, TCNames.fishingNet);
		
		registerItem(coffeeBean, TCNames.coffeeBean);
	}
	
	/**
	 * Register an item with the game and give it a name
	 * @param item Item to register
	 * @param name Name to give
	 */
	private static void registerItem(Item item, String name) {
		GameRegistry.registerItem(item, name);
		item.setUnlocalizedName(name);
	}
}
