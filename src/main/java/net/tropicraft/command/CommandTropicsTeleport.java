package net.tropicraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.tropicraft.util.TropicraftWorldUtils;

public class CommandTropicsTeleport extends CommandBase {

	@Override
	public String getCommandName() {
		return "tropics";
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		EntityPlayerMP player = this.getCommandSenderAsPlayer(commandSender);
		
		TropicraftWorldUtils.teleportPlayer(player);
	}

}
