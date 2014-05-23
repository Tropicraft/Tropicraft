package net.tropicraft.registry;

import net.minecraft.block.Block;
import net.tropicraft.block.BlockChunkOHead;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.BlockTropicraftOre;
import net.tropicraft.block.BlockTropicraftStairs;
import net.tropicraft.info.TCNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCBlockRegistry {
	
	public static final BlockTropicraft chunkOHead = new BlockChunkOHead();
	public static final BlockTropicraftStairs chunkStairs = new BlockTropicraftStairs(TCNames.chunkStairs, chunkOHead, 0);
	
	public static final BlockTropicraft eudialyteOre = new BlockTropicraftOre();
	public static final BlockTropicraft zirconOre = new BlockTropicraftOre();
	public static final BlockTropicraft azuriteOre = new BlockTropicraftOre();
	
	public static void init() {
		registerBlock(chunkOHead, TCNames.chunkOHead);
		registerBlock(chunkStairs, TCNames.chunkStairs);
		registerBlock(eudialyteOre, TCNames.eudialyteOre);
		registerBlock(zirconOre, TCNames.zirconOre);
		registerBlock(azuriteOre, TCNames.azuriteOre);
	}
	
	/**
	 * Helper method for registering a block with Forge
	 * @param block Block instance
	 * @param name Name of the block
	 */
	private static void registerBlock(Block block, String name) {
		GameRegistry.registerBlock(block, "tile." + name);
		block.setBlockName(name);
	}
}
