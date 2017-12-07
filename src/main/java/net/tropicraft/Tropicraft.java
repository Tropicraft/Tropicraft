package net.tropicraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.client.gui.TropicraftGuiHandler;
import net.tropicraft.core.common.BuildEvents;
import net.tropicraft.core.common.biome.BiomeTropicraft;
import net.tropicraft.core.common.capability.ExtendedPlayerStorage;
import net.tropicraft.core.common.capability.ExtendedWorldStorage;
import net.tropicraft.core.common.capability.PlayerDataInstance;
import net.tropicraft.core.common.capability.WorldDataInstance;
import net.tropicraft.core.common.config.TropicsConfigs;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.event.AchievementEvents;
import net.tropicraft.core.common.event.BlockEvents;
import net.tropicraft.core.common.event.ItemEvents;
import net.tropicraft.core.common.event.MiscEvents;
import net.tropicraft.core.common.event.ScubaHandlerCommon;
import net.tropicraft.core.common.event.SpawnEvents;
import net.tropicraft.core.common.item.scuba.ScubaCapabilities;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.common.worldgen.overworld.TCWorldGenerator;
import net.tropicraft.core.encyclopedia.Encyclopedia;
import net.tropicraft.core.proxy.CommonProxy;
import net.tropicraft.core.registry.CommandRegistry;
import net.tropicraft.core.registry.FluidRegistry;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.LootRegistry;
import net.tropicraft.core.registry.SmeltingRegistry;
import net.tropicraft.core.registry.SoundRegistry;
import net.tropicraft.core.registry.TileEntityRegistry;

@Mod(modid = Info.MODID, version = Info.VERSION, dependencies = "after:Forge@[12.18.3.2221,)", guiFactory = Info.GUI_FACTORY)
public class Tropicraft {

	@SidedProxy(clientSide = Info.CLIENT_PROXY, serverSide = Info.SERVER_PROXY)
	public static CommonProxy proxy;

	@Mod.Instance(Info.MODID)
	public static Tropicraft instance;
	
	public static Encyclopedia encyclopedia;
	
    @CapabilityInject(PlayerDataInstance.class)
    public static final Capability<PlayerDataInstance> PLAYER_DATA_INSTANCE = null;

	@CapabilityInject(WorldDataInstance.class)
	public static final Capability<WorldDataInstance> WORLD_DATA_INSTANCE = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(proxy);

	    TropicsConfigs.init(event.getSuggestedConfigurationFile());

		ColorHelper.init();
		SoundRegistry.init();
		FluidRegistry.preInit();
		//BlockRegistry.preInit();
	    proxy.preInit();
		TileEntityRegistry.init();
		//ItemRegistry.preInit();
		//CraftingRegistry.init();
		ScubaCapabilities.register();
		CapabilityManager.INSTANCE.register(PlayerDataInstance.class, new ExtendedPlayerStorage(), PlayerDataInstance.class);
		CapabilityManager.INSTANCE.register(WorldDataInstance.class, new ExtendedWorldStorage(), WorldDataInstance.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new TropicraftGuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TCPacketHandler.init();
	//	ItemRegistry.init();
//		AchievementRegistry.init();
		//EntityRegistry.init();
		proxy.init();
		MixerRecipes.addMixerRecipes();
		proxy.registerBooks();
		SmeltingRegistry.init();
		MinecraftForge.EVENT_BUS.register(new ItemEvents());
		MinecraftForge.EVENT_BUS.register(new BlockEvents());
		MinecraftForge.EVENT_BUS.register(new AchievementEvents());
		MinecraftForge.EVENT_BUS.register(new BuildEvents());
		MinecraftForge.EVENT_BUS.register(new MiscEvents());
		MinecraftForge.EVENT_BUS.register(new SpawnEvents());
		MinecraftForge.EVENT_BUS.register(new ScubaHandlerCommon());
		BiomeTropicraft.registerBiomes();
		GameRegistry.registerWorldGenerator(new TCWorldGenerator(), 10);
		TropicraftWorldUtils.initializeDimension();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		FluidRegistry.postInit();
		LootRegistry.postInit();
	}

	/**
	 * Triggered when a server starts
	 * @param event
	 */
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		CommandRegistry.init(event);
	}
	
    @EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
    		event.applyModContainer(ForgeModContainer.getInstance());
        for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
	        	if (imcMessage.getSender().equals(Info.MODID) || !imcMessage.key.equalsIgnoreCase("loaderFarewellSkip")) {
	    			return;
	    		}
            if (imcMessage.isNBTMessage()) {
                	NBTTagCompound n = imcMessage.getNBTValue();
                	int id = n.getInteger("dim_id");
                	String name = n.getString("dim_name"); 
                	System.out.println("Received IMC request to add dimension \""+name+":"+id+"\" to farewellSkipDimensions from modid:"+imcMessage.getSender());
            }
        }
    }

}
