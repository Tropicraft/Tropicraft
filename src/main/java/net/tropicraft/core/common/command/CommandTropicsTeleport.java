package net.tropicraft.core.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

public class CommandTropicsTeleport extends CommandBase {

	@Override
	public String getCommandName() {
		return "tropics";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {
		EntityPlayerMP player = this.getCommandSenderAsPlayer(sender);
		
		TropicraftWorldUtils.teleportPlayer(player);
	}
}
