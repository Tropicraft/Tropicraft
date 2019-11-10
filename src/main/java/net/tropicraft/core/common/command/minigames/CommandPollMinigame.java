package net.tropicraft.core.common.command.minigames;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.minigames.MinigameManager;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class CommandPollMinigame {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("poll").requires(s -> s.hasPermissionLevel(2))
			.then(argument("minigame_id", StringArgumentType.greedyString()).requires(s -> s.hasPermissionLevel(2))
			.executes(c -> {
				ResourceLocation id = new ResourceLocation(StringArgumentType.getString(c, "minigame_id"));
				return CommandMinigame.executeMinigameAction(() -> MinigameManager.getInstance().startPolling(id), c.getSource());
		}))));
	}
}
