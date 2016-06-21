package net.tropicraft.core.common.dimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraftforge.common.DimensionManager;

public class TropicraftWorldUtils {

	// TODO make configurable
	public static final int TROPICS_DIMENSION_ID = -127;
	public static final DimensionType tropicsDimension = DimensionType.register("Tropics", "_tropics", -127, WorldProviderTropicraft.class, false);

	public static void initializeDimension() {
		DimensionManager.registerDimension(TROPICS_DIMENSION_ID, tropicsDimension);
	}

	public static void teleportPlayer(EntityPlayerMP player)
	{
		long time = System.currentTimeMillis();
		if (player.dimension == TROPICS_DIMENSION_ID) {
			Teleporter tropicsTeleporter = new Teleporter(player.getServer().worldServerForDimension(0));
			PlayerList pl = player.getServer().getPlayerList();
			pl.transferPlayerToDimension(player, 0, tropicsTeleporter);
		} 
		else {
			Teleporter tropicsTeleporter = new Teleporter(player.getServer().worldServerForDimension(TROPICS_DIMENSION_ID));
			PlayerList pl = player.getServer().getPlayerList();
			pl.transferPlayerToDimension(player, TROPICS_DIMENSION_ID, tropicsTeleporter);
		}

		long time2 = System.currentTimeMillis();

		System.out.printf("It took %f seconds to teleport\n", (time2 - time) / 1000.0F);
	}

}
