package net.tropicraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.tropicraft.client.gui.TropicraftGuiHandler;
import net.tropicraft.core.common.biome.BiomeGenTropicraft;
import net.tropicraft.core.common.capability.ExtendedPlayerStorage;
import net.tropicraft.core.common.capability.PlayerDataInstance;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.event.AchievementEvents;
import net.tropicraft.core.common.event.BlockEvents;
import net.tropicraft.core.common.event.ItemEvents;
import net.tropicraft.core.common.item.scuba.ScubaCapabilities;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.encyclopedia.Encyclopedia;
import net.tropicraft.core.proxy.CommonProxy;
import net.tropicraft.core.registry.AchievementRegistry;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.CommandRegistry;
import net.tropicraft.core.registry.CraftingRegistry;
import net.tropicraft.core.registry.EntityRegistry;
import net.tropicraft.core.registry.FluidRegistry;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.LootRegistry;
import net.tropicraft.core.registry.SoundRegistry;
import net.tropicraft.core.registry.TileEntityRegistry;

@Mod(modid = Info.MODID, version = Info.VERSION)
public class Tropicraft {

	@SidedProxy(clientSide = Info.CLIENT_PROXY, serverSide = Info.SERVER_PROXY)
	public static CommonProxy proxy;

	@Mod.Instance(Info.MODID)
	public static Tropicraft instance;
	
	public static Encyclopedia encyclopedia;
	
    @CapabilityInject(PlayerDataInstance.class)
    public static final Capability<PlayerDataInstance> PLAYER_DATA_INSTANCE = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ColorHelper.init();
		SoundRegistry.init();
		FluidRegistry.preInit();
		BlockRegistry.preInit();
		TileEntityRegistry.init();
		ItemRegistry.preInit();
		MixerRecipes.addMixerRecipes();
		proxy.registerBooks();
		CraftingRegistry.preInit();
		ScubaCapabilities.register();
		proxy.preInit();
		CapabilityManager.INSTANCE.register(PlayerDataInstance.class, new ExtendedPlayerStorage(), PlayerDataInstance.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new TropicraftGuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TCPacketHandler.init();
		AchievementRegistry.init();
		EntityRegistry.init();
		proxy.init();
		MinecraftForge.EVENT_BUS.register(new ItemEvents());
		MinecraftForge.EVENT_BUS.register(new BlockEvents());
		MinecraftForge.EVENT_BUS.register(new AchievementEvents());
		BiomeGenTropicraft.registerBiomes();
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

}
