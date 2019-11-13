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

public class CommandSaveIsland {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("saveIsland").requires(s -> s.hasPermissionLevel(2))
			.executes(c -> {
				Entity entity = c.getSource().getEntity();

				if (entity != null && entity.getServer() != null) {
					ServerWorld world = DimensionManager.getWorld(entity.getServer(), TropicraftWorldUtils.SURVIVE_THE_TIDE_DIMENSION, false, false);

					if (world != null) {
						if (world.getPlayers().size() > 0) {
							c.getSource().sendFeedback(new StringTextComponent("FAILURE: Cannot save the Survive The Tide dimension while" +
									" players are still in there. Please remove all players from the dimension first.").applyTextStyle(TextFormatting.RED), true);
						} else {
							DimensionManager.unloadWorld(world);
							c.getSource().sendFeedback(new StringTextComponent("FAILURE: Cannot save the Survive The Tide dimension while" +
									" it is still loaded. Begun unloading, please try again in a few seconds.").applyTextStyle(TextFormatting.RED), true);
						}

						return 0;
					}

					CompoundNBT data = entity.getPersistentData();
					if (data.contains("hasConfirmedSaveIsland")) {
						data.remove("hasConfirmedSaveIsland");

						SurviveTheTideMinigameDefinition.saveBaseMap(entity.getServer());

						c.getSource().sendFeedback(new StringTextComponent("Saved island as a base so it will be used next time the minigame runs!")
								.applyTextStyle(TextFormatting.GREEN), true);

						return 1;
					} else {
						data.putBoolean("hasConfirmedSaveIsland", true);

						c.getSource().sendFeedback(new StringTextComponent("WARNING: YOU ARE ABOUT TO SAVE THE SURVIVE THE TIDE ISLAND." +
								"This will overwrite the saved base map that is currently used when running the minigame." +
								" To confirm, please run the command again.").applyTextStyle(TextFormatting.RED), true);

						return 1;
					}
				}

				return 0;
		})));
	}
}
