package net.tropicraft.core.proxy;


import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.tropicraft.Info;

public class CommonProxy {

	public CommonProxy() {

	}

	public void preInit() {}
	public void init() {}

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Info.MODID, name);
	}

	public static String getNamePrefixed(String name) {
		return Info.MODID + ":" + name;
	}

	public void registerItemVariantModel(Item item, String name, int metadata) {}
	public void registerItemVariantModel(Item item, String registryName, int metadata, String variantName) {}
	public void registerArbitraryBlockVariants(String name, String... variants) {}
	public void registerItemWithSubtypes(Item item, CreativeTabs tabs) {}
	public void registerFluidBlockRendering(Block block, String name) {}
	public void registerColoredBlock(Block block) {}
	public void registerColoredItem(Item item) {}
	public void registerBooks() {}
	
	public World getClientWorld() {
		return null;
	}

}