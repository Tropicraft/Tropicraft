package net.tropicraft;

import net.minecraftforge.common.MinecraftForge;
import net.tropicraft.event.TCBlockEvents;
import net.tropicraft.info.TCInfo;
import net.tropicraft.proxy.ISuperProxy;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
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
	
	/**
	 * Triggered when a server starts
	 * @param event
	 */
	@EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

    }

	/**
	 * Triggered before the game loads
	 * @param event
	 */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	TCBlockRegistry.init();
    	TCItemRegistry.init();
    }
	
	/**
	 * Called on initialization
	 * @param event
	 */
	@EventHandler
    public void init(FMLInitializationEvent event) {
		proxy.initRenderHandlersAndIDs();
		MinecraftForge.EVENT_BUS.register(new TCBlockEvents());
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

    }
}
