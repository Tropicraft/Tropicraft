package net.tropicraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.common.DimensionManager;
import net.tropicraft.world.TeleporterTropics;
import net.tropicraft.world.WorldProviderTropicraft;

public class TropicraftWorldUtils {

	public static final int TROPICS_DIMENSION_ID = -127;
	
	public static void initializeDimension() {
		DimensionManager.registerProviderType(TROPICS_DIMENSION_ID, WorldProviderTropicraft.class, true);
		DimensionManager.registerDimension(TROPICS_DIMENSION_ID, TROPICS_DIMENSION_ID);
	}
	
	public static void teleportPlayer(EntityPlayerMP player)
	{
		long time = System.currentTimeMillis();
		if (player.dimension == TROPICS_DIMENSION_ID) {
			TeleporterTropics tropicsTeleporter = new TeleporterTropics(MinecraftServer.getServer().worldServerForDimension(0));
			ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
			scm.transferPlayerToDimension(player, 0, tropicsTeleporter);
		} 
		else {
			TeleporterTropics tropicsTeleporter = new TeleporterTropics(MinecraftServer.getServer().worldServerForDimension(TROPICS_DIMENSION_ID));
			ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
			scm.transferPlayerToDimension(player, TROPICS_DIMENSION_ID, tropicsTeleporter);
		}
		
		long time2 = System.currentTimeMillis();
		
		System.out.printf("It took %f seconds to teleport\n", (time2 - time) / 1000.0F);
	}
	
}
