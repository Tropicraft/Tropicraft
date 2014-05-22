package net.tropicraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

/**
 * Mod file for the Tropicraft mod
 *
 */
@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION)
public class TropicraftMod {
	
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
    	
    }
	
	/**
	 * Called on initialization
	 * @param event
	 */
	@EventHandler
    public void init(FMLInitializationEvent event) {
		
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
