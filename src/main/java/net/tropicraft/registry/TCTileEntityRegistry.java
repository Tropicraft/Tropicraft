package net.tropicraft.registry;

import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntityFirePit;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCTileEntityRegistry {

	public static void init() {
		registerTE(TileEntityTropicraftFlowerPot.class, "TCFlowerPot");
		registerTE(TileEntityFirePit.class, "TCFirePit");
	}
	
	private static void registerTE(Class<? extends TileEntity> clazz, String name) {
		GameRegistry.registerTileEntity(clazz, name);
	}
}
