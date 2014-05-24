package net.tropicraft.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.tropicraft.block.BlockBundle;
import net.tropicraft.block.BlockChunkOHead;
import net.tropicraft.block.BlockCoral;
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
	public static final BlockTropicraft thatchBundle = new BlockBundle(TCNames.thatchBundle);
	public static final BlockTropicraft coral = new BlockCoral(TCNames.coralNames);
	public static final BlockTropicraft bambooBundle = (BlockTropicraft) new BlockBundle(TCNames.bambooBundle).setHardness(1.0F).setResistance(0.1F);
	public static final BlockTropicraft logs = new BlockTropicraftLog(TCNames.logNames);
	public static final BlockTropicraft planks = new BlockTropicraftPlank(TCNames.plankNames);
	
	public static final BlockTropicraftStairs bambooStairs = new BlockTropicraftStairs(TCNames.bambooStairs, bambooBundle, 0);
	public static final BlockTropicraftStairs thatchStairs = new BlockTropicraftStairs(TCNames.thatchStairs, thatchBundle, 0);
	public static final BlockTropicraftStairs palmStairs = new BlockTropicraftStairs(TCNames.palmStairs, planks, 0);
	public static final BlockTropicraftStairs mahoganyStairs = new BlockTropicraftStairs(TCNames.mahoganyStairs, planks, 3);
	
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
		registerBlock(thatchBundle, TCNames.thatchBundle);
		registerMultiBlock(coral, TCNames.coral, TCNames.coralNames);
		registerBlock(bambooBundle, TCNames.bambooBundle);
		registerMultiBlock(logs, TCNames.log, TCNames.logNames);
		Blocks.fire.setFireInfo(TCBlockRegistry.logs, 5, 5);
		registerMultiBlock(planks, TCNames.plank, TCNames.plankNames);
		Blocks.fire.setFireInfo(TCBlockRegistry.planks, 5, 5);
		registerBlock(bambooStairs, TCNames.bambooStairs);
		registerBlock(thatchStairs, TCNames.thatchStairs);
		registerBlock(palmStairs, TCNames.palmStairs);
		registerBlock(mahoganyStairs, TCNames.mahoganyStairs);
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
