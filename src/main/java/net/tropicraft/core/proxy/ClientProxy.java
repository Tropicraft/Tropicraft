package net.tropicraft.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.tropicraft.Info;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {

	}

	public void registerItemRender(Item item, int meta, String unlocalizedName) {
		//ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Tropicraft.modID + ":" + unlocalizedName, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(Info.MODID + ":" + unlocalizedName, "inventory"));
	}

	public void registerItemRender(Item item, int meta, String unlocalizedName, String location) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "variant=" + unlocalizedName));
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(Tropicraft.modID + ":" + unlocalizedName, location));
	}

	@Override
	public void registerItemVariantModel(Item item, String name, int metadata) {
		if (item != null) { 
			//     ModelBakery.registerItemVariants(item, new ResourceLocation(Info.MODID + ":" + name));
			ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Info.MODID + ":" + name, "inventory"));
		}
	}

	@Override
	public void registerItemVariantModel(Item item, String registryName, int metadata, String variantName) {
		if (item != null) {
			// ModelResourceLocation mrl = new ModelResourceLocation(Info.MODID + ":" + registryName, variantName);
			// ModelLoader.setCustomModelResourceLocation(item, metadata, mrl);
			ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Info.MODID + ":" + variantName, null));
		}
	}
}
