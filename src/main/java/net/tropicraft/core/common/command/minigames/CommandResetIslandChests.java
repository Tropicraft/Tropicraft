package net.tropicraft.core.common.command.minigames;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.minigames.MinigameManager;

import static net.minecraft.command.Commands.literal;

public class CommandResetIslandChests {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("resetIslandChests").requires(s -> s.hasPermissionLevel(2))
			.executes(c -> {
				World world = c.getSource().getWorld();

				if (world.getDimension().getType() != TropicraftWorldUtils.SURVIVE_THE_TIDE_DIMENSION) {
					c.getSource().sendFeedback(new StringTextComponent("Must use this command in survive the tide dimension"), true);
					return 0;
				}

				for (TileEntity te : world.loadedTileEntityList) {
					if (te instanceof ChestTileEntity) {
						ChestTileEntity cte = (ChestTileEntity)te;
						cte.clear();

						CompoundNBT tag = new CompoundNBT();
						cte.write(tag);

						tag.putString("LootTable", "tropicraft:survivethetide");

						cte.read(tag);
						cte.markDirty();
					}
				}
				return 1;
			}))
		);
	}
}
