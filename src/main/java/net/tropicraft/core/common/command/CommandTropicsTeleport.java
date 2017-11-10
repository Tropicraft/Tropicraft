package net.tropicraft.core.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

public class CommandTropicsTeleport extends CommandBase {

	@Override
	public String getName() {
		return "tropics";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
	    return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {
	    if (!(sender.getCommandSenderEntity() instanceof EntityPlayerMP)) {
	        throw new CommandException("Cannot teleport non-players!");
	    }
		EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
		TropicraftWorldUtils.teleportPlayer(player);
	}
}
