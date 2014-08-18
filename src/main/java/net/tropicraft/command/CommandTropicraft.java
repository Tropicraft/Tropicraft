package net.tropicraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.tropicraft.world.location.TownKoaVillage;
import CoroUtil.forge.CoroAI;
import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;

public class CommandTropicraft extends CommandBase {

	@Override
	public String getCommandName() {
		return "tc";
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		EntityPlayerMP player = this.getCommandSenderAsPlayer(commandSender);
		
		if (args.length > 0) {
			if (args[0].equals("test")) {
				int x = MathHelper.floor_double(player.posX);
				int z = MathHelper.floor_double(player.posZ);
				int y = player.worldObj.getHeightValue(x, z);
				TownKoaVillage village = new TownKoaVillage();
				
				WorldDirector wd = WorldDirectorManager.instance().getWorldDirector(CoroAI.modID, player.worldObj);
				if (wd == null) {
					wd = new WorldDirector();
					WorldDirectorManager.instance().registerWorldDirector(wd, CoroAI.modID, player.worldObj);
				}
				//questionable ID setting
				int newID = wd.lookupTickingManagedLocations.size();
				village.initData(newID, player.worldObj.provider.dimensionId, new ChunkCoordinates(x, y, z));
				village.initFirstTime();
				wd.addTickingLocation(village);
				//StructureObject bb = StructureMapping.newTown(player.worldObj.provider.dimensionId, "command", new ChunkCoordinates(x, y, z));
				//bb.init();
				//bb.location.initFirstTime();
			}
		}
	}

}
