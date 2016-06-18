package net.tropicraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.tropicraft.core.proxy.CommonProxy;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.EntityRegistry;
import net.tropicraft.core.registry.ItemRegistry;

@Mod(modid = Info.MODID, version = Info.VERSION)
public class Tropicraft {
	
	@SidedProxy(clientSide = "net.tropicraft.core.proxy.ClientProxy", serverSide = "net.tropicraft.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance(Info.MODID)
    public static Tropicraft instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		BlockRegistry.preInit();
		ItemRegistry.preInit();
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
        EntityRegistry.init();
        proxy.init();
    }
}
