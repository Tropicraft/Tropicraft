package net.tropicraft.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

import static net.minecraft.command.Commands.literal;

public class CommandTropicsTeleport {

	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				literal("tropics")
					.requires(s -> s.hasPermissionLevel(2))
					.executes(c -> teleport(c.getSource(), TropicraftWorldUtils.TROPICS_DIMENSION))
		);

		dispatcher.register(
				literal("hunger_games")
						.requires(s -> s.hasPermissionLevel(2))
						.executes(c -> teleport(c.getSource(), TropicraftWorldUtils.ISLAND_ROYALE_DIMENSION))
		);

		dispatcher.register(
				literal("signature_run")
						.requires(s -> s.hasPermissionLevel(2))
						.executes(c -> teleport(c.getSource(), TropicraftWorldUtils.SIGNATURE_RUN_DIMENSION))
		);
	}

	private static int teleport(final CommandSource source, DimensionType dimensionType) {
		if (source.getEntity().getType() != EntityType.PLAYER) {
			source.sendErrorMessage(new StringTextComponent("Cannot teleport non-players!"));
		}
		TropicraftWorldUtils.teleportPlayer((ServerPlayerEntity) source.getEntity(), dimensionType);
		return 1;
	}
}
