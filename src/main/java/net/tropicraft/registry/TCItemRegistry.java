package net.tropicraft.registry;

import net.minecraft.item.Item;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.item.ItemTropicraftFood;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCItemRegistry {

	public static final ItemTropicraft frogLeg = new ItemTropicraft();
	public static final ItemTropicraftFood cookedFrogLeg = new ItemTropicraftFood(3, 0.2F);
	public static final ItemTropicraft poisonFrogSkin = new ItemTropicraft();
	
	public static void init() {
		registerItem(frogLeg, TCNames.frogLeg);
		registerItem(cookedFrogLeg, TCNames.cookedFrogLeg);
		registerItem(poisonFrogSkin, TCNames.poisonFrogSkin);
	}
	
	private static void registerItem(Item item, String name) {
		GameRegistry.registerItem(item, name);
		item.setUnlocalizedName(name);
	}
}
