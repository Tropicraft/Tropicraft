package net.tropicraft.core.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.tropicraft.core.common.minigames.MinigameManager;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class CommandReloadConfig {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal(getCommandName())
			.then(literal("client").executes(c -> {
				//TODO: this uh, wont run on clients if using dedicated server
				//ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.CLIENT, FMLPaths.CONFIGDIR.get());
				//fake it for now, let client side intercept
				return Command.SINGLE_SUCCESS;
			})).then(literal("server").executes(c -> {
				ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.SERVER, FMLPaths.CONFIGDIR.get());
				System.out.println("reload server configs");
				return Command.SINGLE_SUCCESS;
			}))
		);
	}

	public static String getCommandName() {
		return "reloadconfig";
	}
}
