package net.tropicraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.event.BlockEvents;
import net.tropicraft.core.common.event.ItemEvents;
import net.tropicraft.core.proxy.CommonProxy;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.CommandRegistry;
import net.tropicraft.core.registry.CraftingRegistry;
import net.tropicraft.core.registry.EntityRegistry;
import net.tropicraft.core.registry.FluidRegistry;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.SoundRegistry;
import net.tropicraft.core.registry.TileEntityRegistry;

@Mod(modid = Info.MODID, version = Info.VERSION)
public class Tropicraft {

	@SidedProxy(clientSide = Info.CLIENT_PROXY, serverSide = Info.SERVER_PROXY)
	public static CommonProxy proxy;

	@Mod.Instance(Info.MODID)
	public static Tropicraft instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SoundRegistry.init();
		FluidRegistry.preInit();
		BlockRegistry.preInit();
		TileEntityRegistry.init();
		ItemRegistry.preInit();
		CraftingRegistry.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		EntityRegistry.init();
		proxy.init();
		MinecraftForge.EVENT_BUS.register(new ItemEvents());
		MinecraftForge.EVENT_BUS.register(new BlockEvents());
		BiomeGenTropicraft.registerBiomes();
		TropicraftWorldUtils.initializeDimension();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		FluidRegistry.postInit();
		Tropicraft.proxy.registerColoredBlock(BlockRegistry.sand);
	}

	/**
	 * Triggered when a server starts
	 * @param event
	 */
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		CommandRegistry.init(event);
	}

}
