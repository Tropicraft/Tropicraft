package net.tropicraft.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.tropicraft.block.BlockChunkOHead;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.BlockTropicraftMulti;
import net.tropicraft.block.BlockTropicraftOre;
import net.tropicraft.block.BlockTropicraftStairs;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemBlockTropicraft;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCBlockRegistry {
	
	public static final BlockTropicraft chunkOHead = new BlockChunkOHead();
	public static final BlockTropicraftStairs chunkStairs = new BlockTropicraftStairs(TCNames.chunkStairs, chunkOHead, 0);
	
	public static final BlockTropicraft eudialyteOre = new BlockTropicraftOre();
	public static final BlockTropicraft zirconOre = new BlockTropicraftOre();
	public static final BlockTropicraft azuriteOre = new BlockTropicraftOre();
	
	public static final BlockTropicraft oreBlocks = new BlockTropicraftMulti(TCNames.oreBlockNames);
	
	/**
	 * Register all the blocks
	 */
	public static void init() {
		registerBlock(chunkOHead, TCNames.chunkOHead);
		registerBlock(chunkStairs, TCNames.chunkStairs);
		registerBlock(eudialyteOre, TCNames.eudialyteOre);
		registerBlock(zirconOre, TCNames.zirconOre);
		registerBlock(azuriteOre, TCNames.azuriteOre);
		registerMultiBlock(oreBlocks, TCNames.oreBlock, TCNames.oreBlockNames);
	}
	
	/**
	 * Register a block with metadata
	 * @param block Block being registered
	 * @param name Name of the image prefix
	 * @param names Names of the images
	 */
	private static void registerMultiBlock(Block block, String name, String[] names) {
		List<String> namesList = new ArrayList<String>();
		Collections.addAll(namesList, names);
		GameRegistry.registerBlock(block, ItemBlockTropicraft.class, "tile." + name, TCInfo.MODID, namesList);
		block.setBlockName(name);
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
