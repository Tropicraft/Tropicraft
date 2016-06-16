package net.tropicraft.core.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabRegistry {

	public static final CreativeTabs tropicraftTab = new CreativeTabTropicraft("tropicraft");
	
	public static class CreativeTabTropicraft extends CreativeTabs {
		public CreativeTabTropicraft(String name) {
			super(name);
		}
		
		@Override
		public Item getTabIconItem() {
			return Items.APPLE;
		}
	}
}