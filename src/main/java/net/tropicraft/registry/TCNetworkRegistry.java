package net.tropicraft.registry;

import net.tropicraft.info.TCInfo;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class TCNetworkRegistry {

	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(TCInfo.MODID);
	
	public static void init() {

	}
}
