package net.tropicraft.core.registry;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.tropicraft.core.client.tileentity.TileEntityBambooChestRenderer;
import net.tropicraft.core.client.tileentity.TileEntityDrinkMixerRenderer;
import net.tropicraft.core.client.tileentity.TileEntitySifterRenderer;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.block.tileentity.TileEntitySifter;

public class TileEntityRenderRegistry {

	/**
	 * Register all tile entity special render mappings
	 */
	public static void init() {
		registerTileEntityRenderer(TileEntityBambooChest.class, new TileEntityBambooChestRenderer());
		registerTileEntityRenderer(TileEntityDrinkMixer.class, new TileEntityDrinkMixerRenderer());
		registerTileEntityRenderer(TileEntitySifter.class, new TileEntitySifterRenderer());
	}

	private static <T extends TileEntity> void registerTileEntityRenderer(Class<T> tileEntityClass, 
			TileEntitySpecialRenderer<T> specialRenderer) {
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
	}
}
