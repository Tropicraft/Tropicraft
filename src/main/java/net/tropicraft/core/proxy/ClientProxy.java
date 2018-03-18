package net.tropicraft.core.proxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.BasicColorHandler;
import net.tropicraft.core.client.CocktailColorHandler;
import net.tropicraft.core.client.PlayerSwimDataClientHandler;
import net.tropicraft.core.client.ScubaHandler;
import net.tropicraft.core.client.ScubaOverlayHandler;
import net.tropicraft.core.client.TropicraftLoadingListener;
import net.tropicraft.core.client.TropicraftWaterRenderFixer;
import net.tropicraft.core.common.block.BlockTropicraftFence;
import net.tropicraft.core.common.block.BlockTropicraftFence.WaterState;
import net.tropicraft.core.common.block.ITropicraftBlock;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.item.IColoredItem;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.common.item.ItemTropicraftColored;
import net.tropicraft.core.common.network.MessagePlayerSwimData;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.encyclopedia.Encyclopedia;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.EncyclopediaRegistry;
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
	}
	
	@SubscribeEvent
	public void registerStateMappers(ModelRegistryEvent event) {
		ignoreProperties(BlockRegistry.coral, BlockFluidBase.LEVEL);
		ignoreProperties(BlockRegistry.bambooFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.chunkFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.mahoganyFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.palmFenceGate, BlockFenceGate.POWERED);
		ignoreProperties(BlockRegistry.thatchFenceGate, BlockFenceGate.POWERED);
		
		// Fence water hacks
		ignoreAll(BlockRegistry.bambooFence, BlockRegistry.chunkFence, BlockRegistry.mahoganyFence, BlockRegistry.palmFence, BlockRegistry.thatchFence);
	}

	@SuppressWarnings("deprecation")
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
		MinecraftForge.EVENT_BUS.register(new TropicraftLoadingListener());

		// For rendering drink mixer in inventory
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockRegistry.drinkMixer), 0, TileEntityDrinkMixer.class);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockRegistry.airCompressor), 0, TileEntityAirCompressor.class);
	}
	
	private void ignoreAll(@Nonnull Block... blocks) {
	    for (Block block : blocks) {
	        ignoreProperties(block, block.getBlockState().getProperties().toArray(new IProperty<?>[0]));
	    }
	}
	
	private void ignoreProperties(@Nonnull Block block, @Nonnull IProperty<?>... props) {
	    setStateMapper(block, new StateMap.Builder().ignore(props).build());
	}
	
	private void setStateMapper(@Nonnull Block block, @Nonnull IStateMapper mapper) {
	    this.stateMappers.put(block, mapper);
	    ModelLoader.setCustomStateMapper(block, mapper);
	}

	@Override
    public void registerColoredBlock(Block block) {
		ITropicraftBlock tcBlock = (ITropicraftBlock)block;
		if (tcBlock.getBlockColor() != null) {
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(tcBlock.getBlockColor(), block);
		}

		if (tcBlock.getItemColor() != null) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(tcBlock.getItemColor(), block);
		}
	}

	@Override
    public <T extends Item & IColoredItem> void registerColoredItem(T item) {
		IItemColor itemColor = item.getColorHandler();
		if (itemColor != null) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColor, item);
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
		try {
		    IModel waterTop = ModelLoaderRegistry.getModel(new ResourceLocation("tropicraft:block/tropics_water_top"));
		    final IBakedModel waterTopBaked = waterTop.bake(waterTop.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
		    Block[] fences = new Block[] { BlockRegistry.bambooFence, BlockRegistry.chunkFence, BlockRegistry.mahoganyFence, BlockRegistry.palmFence, BlockRegistry.thatchFence };
            for (Block block : fences) {
                ModelResourceLocation loc = new ModelResourceLocation(block.getRegistryName(), "normal");
                final IBakedModel fenceModel = event.getModelRegistry().getObject(loc);
                event.getModelRegistry().putObject(loc, new IBakedModel() {

                    @Override
                    public @Nonnull List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
                        BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
                        if (layer == null || layer == BlockRenderLayer.SOLID) {
                            return fenceModel.getQuads(state, side, rand);
                        } else if (layer == BlockRenderLayer.TRANSLUCENT && state.getValue(BlockTropicraftFence.WATER) == WaterState.SURFACE) {
                            return waterTopBaked.getQuads(state, side, rand);
                        }
                        return Collections.emptyList();
                    }

                    @Override
                    public boolean isGui3d() {
                        return true;
                    }

                    @Override
                    public boolean isBuiltInRenderer() {
                        return false;
                    }

                    @Override
                    public boolean isAmbientOcclusion() {
                        return true;
                    }

                    @Override
                    public @Nonnull TextureAtlasSprite getParticleTexture() {
                        return fenceModel.getParticleTexture();
                    }

                    @Override
                    public @Nonnull ItemOverrideList getOverrides() {
                        return fenceModel.getOverrides();
                    }
                });
		    }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
	}

	// Another nice method based on code from BoP. Those guys rock :D
	/**
	 * Registers an item (in ItemRegistry) that has damage values
	 */
	@Override
	public void registerItemWithSubtypes(Item item, CreativeTabs tab) {
		if (item.getHasSubtypes()) {
			NonNullList<ItemStack> subItems = NonNullList.create();
			item.getSubItems(tab, subItems);
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
			protected @Nonnull ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return fluidLocation;
			}
		});
	}

	@Override
	public void registerBooks() {
		Tropicraft.encyclopedia = new Encyclopedia("etsave.dat",
				Info.TEXTURE_GUI_LOC + "encyclopedia_tropica.txt",
				"encyclopedia_tropica",
				"encyclopedia_tropica_inside");
		EncyclopediaRegistry.init(); // registers items for encyclopedia
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

	@Override
	public boolean helloIsItMeYoureLookingFor(EntityPlayer player) {
		return player == Minecraft.getMinecraft().player;
	}
}
