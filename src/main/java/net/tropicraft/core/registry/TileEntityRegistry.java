package net.tropicraft.core.registry;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Names;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;

public class TileEntityRegistry {

	public static void init() {
		registerTE(TileEntityBambooChest.class, Names.TE_BAMBOO_CHEST);
	}
	
	private static void registerTE(Class<? extends TileEntity> clazz, String name) {
		GameRegistry.registerTileEntity(clazz, name);
	}
}
