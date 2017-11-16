package net.tropicraft.core.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabRegistry {

	public static final CreativeTabs tropicraftTab = new CreativeTabTropicraft("tropicraft");
	
	public static class CreativeTabTropicraft extends CreativeTabs {
		public CreativeTabTropicraft(String name) {
			super(name);
		}
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BlockRegistry.pineapple);
		}
	}
}