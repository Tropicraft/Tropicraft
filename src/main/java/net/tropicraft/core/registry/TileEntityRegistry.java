package net.tropicraft.core.registry;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Names;
import net.tropicraft.core.common.block.BlockSeaweed.TileSeaweed;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.block.tileentity.TileEntitySifter;
import net.tropicraft.core.common.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.core.common.block.tileentity.TileEntityVolcano;

public class TileEntityRegistry {

	public static void init() {
		registerTE(TileEntityBambooChest.class, Names.TE_BAMBOO_CHEST);
		registerTE(TileEntityVolcano.class, Names.TE_VOLCANO);
		registerTE(TileEntityDrinkMixer.class, Names.TE_DRINK_MIXER);
		registerTE(TileEntitySifter.class, Names.TE_SIFTER);
		registerTE(TileEntityTropicraftFlowerPot.class, Names.TE_FLOWER_POT);
		registerTE(TileSeaweed.class, "seaweed");
		registerTE(TileEntityAirCompressor.class, Names.AIR_COMPRESSOR);
	}
	
	private static void registerTE(Class<? extends TileEntity> clazz, String name) {
		GameRegistry.registerTileEntity(clazz, name);
	}
}
