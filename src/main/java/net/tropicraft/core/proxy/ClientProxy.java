package net.tropicraft.core.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.ChairColorHandler;
import net.tropicraft.core.client.CocktailColorHandler;
import net.tropicraft.core.client.ScubaHandler;
import net.tropicraft.core.client.TropicraftWaterRenderFixer;
import net.tropicraft.core.common.block.ITropicraftBlock;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.common.item.ItemTropicraftColored;
import net.tropicraft.core.encyclopedia.Encyclopedia;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.CraftingRegistry;
import net.tropicraft.core.registry.EntityRenderRegistry;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.TileEntityRenderRegistry;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void preInit() {
		super.preInit();
		ModelLoader.setCustomStateMapper(BlockRegistry.coral, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
	}

	@Override
	public void init() {
		super.init();

		ItemRegistry.clientProxyInit();
		BlockRegistry.clientProxyInit();
		
		EntityRenderRegistry.init();
		TileEntityRenderRegistry.init();

		MinecraftForge.EVENT_BUS.register(new TropicraftWaterRenderFixer());
		MinecraftForge.EVENT_BUS.register(new ScubaHandler());


		// For rendering drink mixer in inventory
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockRegistry.drinkMixer), 0, TileEntityDrinkMixer.class);
	}

	public void registerColoredBlock(Block block) {
		ITropicraftBlock tcBlock = (ITropicraftBlock)block;
		if (tcBlock.getBlockColor() != null) {
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(tcBlock.getBlockColor(), block);
		}

		if (tcBlock.getItemColor() != null) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(tcBlock.getItemColor(), block);
		}
	}

	public void registerColoredItem(Item item) {
		IItemColor itemColor = null;
		if (item instanceof ItemTropicraftColored) {
			itemColor = new ChairColorHandler();
		} else if (item instanceof ItemCocktail) {
			itemColor = new CocktailColorHandler();
		}
		if (itemColor != null) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ChairColorHandler(), item);
		} else {
			System.err.println("!!! FAILED TO REGISTER COLOR HANDLER FOR ITEM " + item.getUnlocalizedName() + " !!!");
		}
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
	
	private final Map<String, String[]> blockVariants = new HashMap<>();
	
	@Override
	public void registerArbitraryBlockVariants(String name, String... variants) {
		blockVariants.put(name, variants);
	}
	
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		for (Entry<String, String[]> e : blockVariants.entrySet()) {
			for (String variant : e.getValue()) {
				ModelResourceLocation loc = new ModelResourceLocation(Info.MODID + ":" + e.getKey(), variant);
				IModel model = ModelLoaderRegistry.getModelOrLogError(loc, "Could not load arbitrary block variant " + variant + " for block " + e.getKey());
				event.getModelRegistry().putObject(loc, model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter()));
			}
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

	// Another nice method based on code from BoP. Those guys rock :D
	/**
	 * Registers an item (in ItemRegistry) that has damage values
	 */
	@Override
	public void registerItemWithSubtypes(Item item, CreativeTabs tab) {
		if (item.getHasSubtypes()) {
			List<ItemStack> subItems = new ArrayList<ItemStack>();
			item.getSubItems(item, tab, subItems);
			for (ItemStack subItem : subItems) {
				String subItemName = item.getUnlocalizedName(subItem);
				subItemName =  subItemName.substring(subItemName.indexOf(".") + 1); // remove 'item.' from the front

				registerItemVariantModel(item, subItemName, subItem.getMetadata());
			}
		}
		else {
			registerItemVariantModel(item, item.delegate.name().getResourcePath(), 0);
		}
	}

	// Yet another method inspired by BoP :)
	@Override
	public void registerFluidBlockRendering(Block block, String name) {
		final ModelResourceLocation fluidLocation = new ModelResourceLocation(Info.MODID + ":" + name, name);

		// use a custom state mapper which will ignore the LEVEL property
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return fluidLocation;
			}
		});
	}

	@Override
	public void registerBooks() {
		Tropicraft.encyclopedia = new Encyclopedia("eTsave.dat",
				Info.TEXTURE_GUI_LOC + "EncyclopediaTropica.txt", 
				"encyclopediaTropica", 
				"encyclopediaTropicaInside");
		CraftingRegistry.addItemsToEncyclopedia(); // registers items for encyclopedia
	}
	
	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().world;
	}
}
