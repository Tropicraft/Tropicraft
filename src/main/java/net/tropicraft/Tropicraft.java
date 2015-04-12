package net.tropicraft;

import modconfig.ConfigMod;
import net.minecraftforge.common.MinecraftForge;
import net.tropicraft.config.ConfigStuff;
import net.tropicraft.drinks.MixerRecipes;
import net.tropicraft.encyclopedia.Encyclopedia;
import net.tropicraft.event.TCPacketEvents;
import net.tropicraft.event.TCBlockEvents;
import net.tropicraft.event.TCItemEvents;
import net.tropicraft.event.TCMiscEvents;
import net.tropicraft.info.TCInfo;
import net.tropicraft.proxy.ISuperProxy;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCommandRegistry;
import net.tropicraft.registry.TCCraftingRegistry;
import net.tropicraft.registry.TCEntityRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;
import net.tropicraft.registry.TCTileEntityRegistry;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftWorldUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * Mod file for the Tropicraft mod
 *
 */
@Mod(modid = TCInfo.MODID, name = TCInfo.NAME, version = TCInfo.VERSION)
public class Tropicraft {
	
	@SidedProxy(clientSide = TCInfo.CLIENT_PROXY, serverSide = TCInfo.SERVER_PROXY)
	public static ISuperProxy proxy;
	
	@Mod.Instance(TCInfo.MODID)
    public static Tropicraft instance;
	
	public static Encyclopedia encyclopedia;
	
	public static String eventChannelName = "tropicraft";
	public static final FMLEventChannel eventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(eventChannelName);
	
	/**
	 * Triggered when a server starts
	 * @param event
	 */
	@EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
		TCCommandRegistry.init(event);
    }

	/**
	 * Triggered before the game loads
	 * @param event
	 */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	ConfigMod.addConfigFile(event, "tc", new ConfigStuff());
    	ColorHelper.init();
    	TCFluidRegistry.init();
    	TCBlockRegistry.init();
    	TCTileEntityRegistry.init();
    	TCItemRegistry.init();
    	TCFluidRegistry.postInit();
    	TCKoaCurrencyRegistry.init();
    	TCCraftingRegistry.init();
    	eventChannel.register(new TCPacketEvents());
    //TODO	proxy.preInit(); // On client, this initializes the encyclopedia
    }
	
	/**
	 * Called on initialization
	 * @param event
	 */
	@EventHandler
    public void init(FMLInitializationEvent event) {
		proxy.initRenderHandlersAndIDs();
		TCEntityRegistry.init();
		proxy.initRenderRegistry();
		MixerRecipes.addMixerRecipes();
		MinecraftForge.EVENT_BUS.register(new TCBlockEvents());
		MinecraftForge.EVENT_BUS.register(new TCItemEvents());
		TCMiscEvents misc = new TCMiscEvents();
		MinecraftForge.EVENT_BUS.register(misc);
		FMLCommonHandler.instance().bus().register(misc);
		TropicraftWorldUtils.initializeDimension();
    }
	
	/**
	 * Triggered after initialization
	 * @param event
	 */
	@EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

	/**
	 * Triggered when communicating with other mods
	 * @param event
	 */
    @EventHandler
    public void handleIMCMessages(IMCEvent event) {
    	//this event is for non runtime messages
    	
    }
    
    public static void dbg(Object obj) {
    	boolean consoleDebug = true;
    	if (consoleDebug) {
    		System.out.println(obj);
    	}
    }
}
