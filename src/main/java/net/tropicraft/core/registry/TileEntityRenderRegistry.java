package net.tropicraft.core.registry;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.tropicraft.core.client.tileentity.TileEntityBambooChestRenderer;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;

public class TileEntityRenderRegistry {

	/**
	 * Register all tile entity special render mappings
	 */
	public static void init() {
		registerTileEntityRenderer(TileEntityBambooChest.class, new TileEntityBambooChestRenderer());
	}

	private static <T extends TileEntity> void registerTileEntityRenderer(Class<T> tileEntityClass, 
			TileEntitySpecialRenderer<T> specialRenderer) {
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
	}
}
