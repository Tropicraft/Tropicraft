package net.tropicraft.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.tropicraft.block.BlockChunkOHead;
import net.tropicraft.block.BlockTropicraft;

public class TCBlockRegistry {
	
	public static final BlockTropicraft chunkOHead = new BlockChunkOHead();
	
	public static void init() {
		registerBlock(chunkOHead, "chunk");
	}
	
	/**
	 * Helper method for registering a block with Forge
	 * @param block Block instance
	 * @param name Name of the block
	 */
	private static void registerBlock(Block block, String name) {
		GameRegistry.registerBlock(block, "tile." + name);
	}
}
