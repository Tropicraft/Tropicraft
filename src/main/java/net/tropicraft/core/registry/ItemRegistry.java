package net.tropicraft.core.registry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.ItemTropicsOre;

public class ItemRegistry extends TropicraftRegistry {

	// Ore gems
	public static Item azurite, eudialyte, zircon;
	
	public static void preInit() {
		azurite = registerItem(new ItemTropicsOre(), "azurite");
		eudialyte = registerItem(new ItemTropicsOre(), "eudialyte");
		zircon = registerItem(new ItemTropicsOre(), "zircon");
	}
	
	public static void init() {
		
	}

	private static Item registerItem(Item item, String name) {
		item.setUnlocalizedName(getNamePrefixed(name));
		item.setRegistryName(new ResourceLocation(Info.MODID, name));
		
		GameRegistry.register(item);
		item.setCreativeTab(CreativeTabRegistry.tropicraftTab);
		Tropicraft.proxy.registerItemVariantModel(item, name, 0);
		
		return item;
	}

}
