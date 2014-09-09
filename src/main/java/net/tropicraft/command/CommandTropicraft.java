package net.tropicraft.command;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.tropicraft.world.WorldProviderTropicraft;
import net.tropicraft.world.location.TownKoaVillage;
import net.tropicraft.world.location.TownKoaVillageGenHelper;
import CoroUtil.forge.CoroAI;
import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import CoroUtil.world.grid.chunk.PlayerDataGrid;
import CoroUtil.world.location.ManagedLocation;

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
			if (args[0].equals("village_new")) {
				int x = MathHelper.floor_double(player.posX);
				int z = MathHelper.floor_double(player.posZ);
				int y = player.worldObj.getHeightValue(x, z);
				
				if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
				
				TownKoaVillage village = new TownKoaVillage();
				
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
				//questionable ID setting
				int newID = wd.lookupTickingManagedLocations.size();
				village.initData(newID, player.worldObj.provider.dimensionId, new ChunkCoordinates(x, y, z));
				village.initFirstTime();
				wd.addTickingLocation(village);
				//StructureObject bb = StructureMapping.newTown(player.worldObj.provider.dimensionId, "command", new ChunkCoordinates(x, y, z));
				//bb.init();
				//bb.location.initFirstTime();
			} else if (args[0].equals("village_try")) {
				int x = MathHelper.floor_double(player.posX);
				int z = MathHelper.floor_double(player.posZ);
				int y = player.worldObj.getHeightValue(x, z);
				TownKoaVillageGenHelper.hookTryGenVillage(new ChunkCoordinates(x, y, z), player.worldObj);
			} else if (args[0].equals("village_clear")) {
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
				
				for (Map.Entry<Integer, ManagedLocation> entry : wd.lookupTickingManagedLocations.entrySet()) {
					entry.getValue().cleanup();
					wd.removeTickingLocation(entry.getValue());
				}
			} else if (args[0].equals("village_regen")) {
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
				Iterator it = wd.lookupTickingManagedLocations.values().iterator();
				while (it.hasNext()) {
					ManagedLocation ml = (ManagedLocation) it.next();
					ml.initFirstTime();
				}
			} else if (args[0].equals("village_repopulate")) {
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.worldObj);
				Iterator it = wd.lookupTickingManagedLocations.values().iterator();
				while (it.hasNext()) {
					
					ManagedLocation ml = (ManagedLocation) it.next();
					if (ml instanceof TownKoaVillage) {
						//((TownKoaVillage) ml).spawnEntities();
					}
				}
			}
		}
	}

}
