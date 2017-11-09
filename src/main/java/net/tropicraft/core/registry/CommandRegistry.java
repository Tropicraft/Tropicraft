package net.tropicraft.core.registry;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.tropicraft.core.common.command.CommandTropicsTeleport;
import net.tropicraft.core.common.command.CommandTropicsVillage;

public class CommandRegistry {

	public static void init(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTropicsTeleport());
		event.registerServerCommand(new CommandTropicsVillage());
		//for debug only
		//event.registerServerCommand(new CommandTropicraft());
	}
}
