package net.tropicraft.core.proxy;


import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;

public class CommonProxy {

    public CommonProxy() {
    	
    }

    public void init() {}
    
//	public void addBlock(Block parBlock, String unlocalizedName) {
//		//vanilla calls
//		GameRegistry.registerBlock(parBlock, ItemBlock.class, unlocalizedName);
//		
//		//new 1.8 stuff
//		parBlock.setUnlocalizedName(getNamePrefixed(unlocalizedName));
//		
//		parBlock.setCreativeTab(CreativeTabs.tabAllSearch);
//		//LanguageRegistry.addName(parBlock, "BLARG");
//	}
//	
    
	public static ResourceLocation getResource(String name) {
    	return new ResourceLocation(Info.MODID, name);
    }
    
    public static String getNamePrefixed(String name) {
    	return Info.MODID + ":" + name;
    }
    
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    public void registerItemVariantModel(Item item, String registryName, int metadata, String variantName) {}
}