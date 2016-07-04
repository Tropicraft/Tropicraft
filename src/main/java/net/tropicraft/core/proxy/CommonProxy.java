package net.tropicraft.core.proxy;


import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.registry.SoundRegistry;

public class CommonProxy {

	public CommonProxy() {

	}

	public void init() {
		SoundRegistry.init();
	}

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Info.MODID, name);
	}

	public static String getNamePrefixed(String name) {
		return Info.MODID + ":" + name;
	}

	public void registerItemVariantModel(Item item, String name, int metadata) {}
	public void registerItemVariantModel(Item item, String registryName, int metadata, String variantName) {}
	public void registerItemWithSubtypes(Item item, CreativeTabs tabs) {}
	public void registerFluidBlockRendering(Block block, String name) {}
}