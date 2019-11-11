package net.tropicraft.core.common.command.minigames;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.minigames.IMinigameDefinition;
import net.tropicraft.core.common.minigames.MinigameManager;

public class CommandPollMinigame {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("poll").requires(s -> s.hasPermissionLevel(2))
			.then(argument("minigame_id", ResourceLocationArgument.resourceLocation())
		              .suggests((ctx, sb) -> net.minecraft.command.ISuggestionProvider.suggest(
		                      MinigameManager.getInstance().getAllMinigames().stream()
		                          .map(IMinigameDefinition::getID)
		                          .map(net.minecraft.util.ResourceLocation::toString), sb))
		              .requires(s -> s.hasPermissionLevel(2))
			.executes(c -> {
				ResourceLocation id = ResourceLocationArgument.getResourceLocation(c, "minigame_id");
				return CommandMinigame.executeMinigameAction(() -> MinigameManager.getInstance().startPolling(id), c.getSource());
		}))));
	}
}
