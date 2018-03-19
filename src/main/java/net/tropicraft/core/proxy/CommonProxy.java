package net.tropicraft.core.proxy;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.tropicraft.Info;
import net.tropicraft.core.common.item.IColoredItem;

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
	public void registerItemVariantModel(Item item, String name, int metadata, String variant) {}
	public void registerBlockVariantModels(Block block, Item item) {}
	public void registerBlockVariantModel(IBlockState state, Item item, int meta) {}
	public void registerArbitraryBlockVariants(String name, String... variants) {}
	public void registerItemWithSubtypes(Item item, CreativeTabs tabs) {}
	public void registerFluidBlockRendering(Block block, String name) {}
	public void registerColoredBlock(Block block) {}
    public <T extends Item & IColoredItem> void registerColoredItem(T item) {}
	public void registerBooks() {}
	
	public World getClientWorld() {
		return null;
	}

	public void registerClientPacketScuba() {

	}
	
	public void registerClientPacketBeachFloat() {
		
	}

	public boolean helloIsItMeYoureLookingFor(EntityPlayer player) {
		return true;
	}
}