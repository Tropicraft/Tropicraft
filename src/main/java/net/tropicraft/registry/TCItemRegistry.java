package net.tropicraft.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemTropicraft;

public class TCItemRegistry {

	public static final ItemTropicraft frogLeg = new ItemTropicraft();
	
	public static void init() {
		registerItem(frogLeg, TCNames.frogLeg);
	}
	
	private static void registerItem(Item item, String name) {
		GameRegistry.registerItem(item, name);
		item.setUnlocalizedName(name);
	}
}
