package net.tropicraft.core.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraft.command.Commands.literal;

public class CommandReloadConfig {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal(getCommandName())
			.then(literal("client").executes(c -> {
				/** dummy literal for autocomplete sake, see EventHandlerForge.clientChat for what actually "intercepts" this */
				return Command.SINGLE_SUCCESS;
			})).then(literal("common").executes(c -> {
				ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FMLPaths.CONFIGDIR.get());
				System.out.println("reload common configs");
				return Command.SINGLE_SUCCESS;
			}))
		);
	}

	public static String getCommandName() {
		return "reloadconfig";
	}
}
