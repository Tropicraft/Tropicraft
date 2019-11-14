package net.tropicraft.core.common.command.minigames;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.tropicraft.core.common.config.ConfigLT;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class CommandIslandSetStartPos {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("islandSetStartPos").requires(s -> s.hasPermissionLevel(2))
            .then(argument("playerIndex", IntegerArgumentType.integer())
			.executes(c -> {
			    int index = IntegerArgumentType.getInteger(c, "playerIndex");
			    int maxPlayerCount = ConfigLT.MINIGAME_SURVIVE_THE_TIDE.maximumPlayerCount.get();

			    if (index >= maxPlayerCount) {
                    c.getSource().sendFeedback(new StringTextComponent("Player index is over the max player count, which is " + maxPlayerCount), true);

                    return 0;
                }

                ForgeConfigSpec.ConfigValue<String> val = ConfigLT.MINIGAME_SURVIVE_THE_TIDE.minigame_SurviveTheTide_playerPositions;
			    BlockPos[] positions = ConfigLT.getAsBlockPosArray(val.get());

			    positions[index] = new BlockPos(c.getSource().getPos());

			    val.set(ConfigLT.blockPositionsString(positions));
			    val.save();

                c.getSource().sendFeedback(new StringTextComponent("Saved starting position for player " + String.valueOf(index + 1) + " to config!"), true);

                return 1;
            })))
		);
	}
}
