package net.tropicraft;

import net.minecraftforge.common.MinecraftForge;
import net.tropicraft.event.TCBlockEvents;
import net.tropicraft.event.TCItemEvents;
import net.tropicraft.info.TCInfo;
import net.tropicraft.proxy.ISuperProxy;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCommandRegistry;
import net.tropicraft.registry.TCEntityRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;
import net.tropicraft.registry.TCTileEntityRegistry;
import net.tropicraft.util.ColorHelper;
import net.tropicraft.util.TropicraftWorldUtils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

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
    	ColorHelper.init();
    	TCFluidRegistry.init();
    	TCBlockRegistry.init();
    	TCTileEntityRegistry.init();
    	TCItemRegistry.init();
    	TCFluidRegistry.postInit();
    	TCKoaCurrencyRegistry.init();
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
		MinecraftForge.EVENT_BUS.register(new TCBlockEvents());
		MinecraftForge.EVENT_BUS.register(new TCItemEvents());
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
