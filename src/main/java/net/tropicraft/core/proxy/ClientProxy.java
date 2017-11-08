package net.tropicraft.core.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
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
import net.minecraftforge.fml.relauncher.Side;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.ChairColorHandler;
import net.tropicraft.core.client.CocktailColorHandler;
import net.tropicraft.core.client.PlayerSwimDataClientHandler;
import net.tropicraft.core.client.ScubaHandler;
import net.tropicraft.core.client.ScubaOverlayHandler;
import net.tropicraft.core.client.TropicraftWaterRenderFixer;
import net.tropicraft.core.common.block.ITropicraftBlock;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.common.item.ItemTropicraftColored;
import net.tropicraft.core.common.network.MessagePlayerSwimData;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.encyclopedia.Encyclopedia;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.CraftingRegistry;
import net.tropicraft.core.registry.EntityRenderRegistry;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.TileEntityRenderRegistry;

public class ClientProxy extends CommonProxy {
    
    private final Map<Block, IStateMapper> stateMappers = new HashMap<>();

	public ClientProxy() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void preInit() {
		super.preInit();
		ignoreProperties(BlockRegistry.coral, BlockFluidBase.LEVEL);
		ignoreProperties(BlockRegistry.bambooFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.chunkFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.mahoganyFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.palmFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.thatchFenceGate, BlockFenceGate.POWERED);
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
		MinecraftForge.EVENT_BUS.register(new ScubaOverlayHandler());

		// For rendering drink mixer in inventory
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockRegistry.drinkMixer), 0, TileEntityDrinkMixer.class);
	}
	
	private void ignoreProperties(Block block, IProperty<?>... props) {
	    setStateMapper(block, new StateMap.Builder().ignore(props).build());
	}
	
	private void setStateMapper(Block block, IStateMapper mapper) {
	    this.stateMappers.put(block, mapper);
	    ModelLoader.setCustomStateMapper(block, mapper);
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
	    registerItemVariantModel(item, name, metadata, "inventory");
    }

    @Override
    public void registerItemVariantModel(Item item, String name, int metadata, String variant) {
        if (item != null) {
            //     ModelBakery.registerItemVariants(item, new ResourceLocation(Info.MODID + ":" + name));
            ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Info.MODID + ":" + name, variant));
        }
    }
    
    private Map<IBlockState, ModelResourceLocation> getStates(Block block) {
        return stateMappers.getOrDefault(block, new DefaultStateMapper()).putStateModelLocations(block);
    }
    
    @Override
    public void registerBlockVariantModels(Block block, Item item) {
        for (Entry<IBlockState, ModelResourceLocation> e : getStates(block).entrySet()) {
            ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(e.getKey()), e.getValue());
        }
    }
    
    @Override
    public void registerBlockVariantModel(IBlockState state, Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getStates(state.getBlock()).get(state));
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
		final ModelResourceLocation fluidLocation = new ModelResourceLocation(Info.MODID + ":" + name, "normal");

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

	@Override
	public void registerClientPacketScuba() {
		super.registerClientPacketScuba();
		TCPacketHandler.INSTANCE.registerMessage(PlayerSwimDataClientHandler.class, MessagePlayerSwimData.class, 4, Side.CLIENT);
	}
}
