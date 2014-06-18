package net.tropicraft.registry;

import net.tropicraft.command.CommandTropicsTeleport;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class TCCommandRegistry {

	public static void init(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTropicsTeleport());
	}
	
}
