package net.tropicraft.core.registry;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.tropicraft.core.common.command.CommandTropicsTeleport;

public class CommandRegistry {

	public static void init(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTropicsTeleport());
		//for debug only
		//event.registerServerCommand(new CommandTropicraft());
	}
}
