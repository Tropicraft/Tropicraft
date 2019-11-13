package net.tropicraft.core.common.command.minigames;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.definitions.survive_the_tide.SurviveTheTideMinigameDefinition;

import static net.minecraft.command.Commands.literal;

public class CommandResetIsland {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("resetIsland").requires(s -> s.hasPermissionLevel(2))
			.executes(c -> {
				Entity entity = c.getSource().getEntity();

				if (entity != null && entity.getServer() != null) {
					ServerWorld world = DimensionManager.getWorld(entity.getServer(), TropicraftWorldUtils.SURVIVE_THE_TIDE_DIMENSION, false, false);

					if (world != null) {
						if (world.getPlayers().size() > 0) {
							c.getSource().sendFeedback(new StringTextComponent("FAILURE: Cannot reset the Survive The Tide dimension while" +
									" players are still in there. Please remove all players from the dimension first.").applyTextStyle(TextFormatting.RED), true);
						} else {
							DimensionManager.unloadWorld(world);
							c.getSource().sendFeedback(new StringTextComponent("FAILURE: Cannot reset the Survive The Tide dimension while" +
									" it is still loaded. Begun unloading, please try again in a few seconds.").applyTextStyle(TextFormatting.RED), true);
						}

						return 0;
					}

					CompoundNBT data = entity.getPersistentData();
					if (data.contains("hasConfirmedResetIsland")) {
						data.remove("hasConfirmedResetIsland");

						SurviveTheTideMinigameDefinition.fetchBaseMap(entity.getServer());

						c.getSource().sendFeedback(new StringTextComponent("Reset island back to saved state!")
								.applyTextStyle(TextFormatting.GREEN), true);

						return 1;
					} else {
						data.putBoolean("hasConfirmedResetIsland", true);

						c.getSource().sendFeedback(new StringTextComponent("WARNING: YOU ARE ABOUT TO RESET THE SURVIVE THE TIDE ISLAND." +
								"This will destroy any unsaved building progress in the dimension (use /minigame saveIsland to save progress)." +
								" To confirm, please run the command again.").applyTextStyle(TextFormatting.RED), true);

						return 1;
					}
				}

				return 0;
		})));
	}
}
