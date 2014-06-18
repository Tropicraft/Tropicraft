package net.tropicraft.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.tropicraft.block.BlockBambooChest;
import net.tropicraft.block.BlockBambooDoor;
import net.tropicraft.block.BlockBundle;
import net.tropicraft.block.BlockChunkOHead;
import net.tropicraft.block.BlockCoconut;
import net.tropicraft.block.BlockCoffeePlant;
import net.tropicraft.block.BlockCoral;
import net.tropicraft.block.BlockFirePit;
import net.tropicraft.block.BlockPineapple;
import net.tropicraft.block.BlockPortalWall;
import net.tropicraft.block.BlockRainStopper;
import net.tropicraft.block.BlockTallFlowers;
import net.tropicraft.block.BlockTikiTorch;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.BlockTropicraftFence;
import net.tropicraft.block.BlockTropicraftFenceGate;
import net.tropicraft.block.BlockTropicraftFlower;
import net.tropicraft.block.BlockTropicraftFlowerPot;
import net.tropicraft.block.BlockTropicraftLog;
import net.tropicraft.block.BlockTropicraftMulti;
import net.tropicraft.block.BlockTropicraftOre;
import net.tropicraft.block.BlockTropicraftPlank;
import net.tropicraft.block.BlockTropicraftSapling;
import net.tropicraft.block.BlockTropicraftSlab;
import net.tropicraft.block.BlockTropicraftStairs;
import net.tropicraft.block.BlockTropicsPortal;
import net.tropicraft.block.BlockTropicsWater;
import net.tropicraft.block.scuba.BlockAirCompressor;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemBlockTropicraft;
import net.tropicraft.item.ItemTallFlowers;
import net.tropicraft.item.ItemTropicraftSlab;
import cpw.mods.fml.common.registry.GameRegistry;

public class TCBlockRegistry {
	
	private static final Map<String, Class<? extends ItemBlock>> multiBlockMap = new HashMap<String, Class<? extends ItemBlock>>();
	
	static {
		multiBlockMap.put(TCNames.pineapple, ItemTallFlowers.class);
		multiBlockMap.put(TCNames.tallFlower, ItemTallFlowers.class);
		multiBlockMap.put(TCNames.singleSlabs, ItemSlab.class);
		multiBlockMap.put(TCNames.doubleSlabs, ItemSlab.class);
	}
	
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
	
	public static final BlockPineapple pineapple = new BlockPineapple(TCNames.pineappleNames);
	public static final BlockTallFlowers tallFlowers = new BlockTallFlowers(TCNames.tallFlowerNames);
	
	public static final BlockTropicraftFenceGate bambooFenceGate = new BlockTropicraftFenceGate(bambooBundle, 0, TCNames.bambooFenceGate, Material.plants);
	public static final BlockTropicraftFenceGate palmFenceGate = new BlockTropicraftFenceGate(planks, 1, TCNames.palmFenceGate, Material.wood);
	
	public static final BlockTropicraftFence bambooFence = new BlockTropicraftFence(TCNames.bambooFence, TCNames.bambooBundle + "_Side", bambooFenceGate, Material.plants);
//	public static final BlockTropicraftFence chunkFence = new BlockTropicraftFence(TCNames.chunkFence, TCNames.chunkOHead, Material.rock);
	public static final BlockTropicraftFence palmFence = new BlockTropicraftFence(TCNames.palmFence, TCNames.plank + "_" + TCNames.plankNames[0], palmFenceGate, Material.wood);
//	public static final BlockTropicraftFence thatchFence = new BlockTropicraftFence(TCNames.thatchFence, TCNames.thatchBundle + "_Side", Material.plants);
//	public static final BlockTropicraftFence mahoganyFence = new BlockTropicraftFence(TCNames.mahoganyFence, TCNames.plank + "_" + TCNames.plankNames[1], Material.wood);
	
	
	public static final BlockTropicraftSapling saplings = new BlockTropicraftSapling(TCNames.saplingNames);
	public static final BlockTropicraft coffeePlant = new BlockCoffeePlant();
	
	public static final BlockTropicraft tikiTorch = new BlockTikiTorch();
	public static final BlockDoor bambooDoor = new BlockBambooDoor();
	public static final BlockSlab singleSlabs = new BlockTropicraftSlab(false);
	public static final BlockSlab doubleSlabs = new BlockTropicraftSlab(true);
	
	public static final BlockTropicsWater tropicsWater = new BlockTropicsWater(TCFluidRegistry.tropicsWater, Material.water);
	
	public static final BlockTropicraft rainStopper = new BlockRainStopper();
	
	public static final BlockTropicraftFlower flowers = new BlockTropicraftFlower(TCNames.flowerIndices);
	public static final BlockTropicraftFlowerPot flowerPot = new BlockTropicraftFlowerPot();
	
	public static final Block firePit = new BlockFirePit();
	
	public static final BlockCoconut coconut = new BlockCoconut();
	
	public static final BlockPortalWall tropicsPortalWall = new BlockPortalWall();
	public static final BlockTropicsPortal tropicsPortal = new BlockTropicsPortal(TCFluidRegistry.tropicsPortal, Material.water);
	
	public static final BlockAirCompressor airCompressor = new BlockAirCompressor();
	
	public static final BlockBambooChest bambooChest = new BlockBambooChest();
	
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
		registerMultiBlock(tallFlowers, TCNames.tallFlower, TCNames.tallFlowerNames);
		registerMultiBlock(pineapple, TCNames.pineapple, TCNames.pineappleNames);
		registerBlockNoName(bambooFence, TCNames.bambooFence);
	//	registerBlockNoName(chunkFence, TCNames.chunkFence);
	//	registerBlockNoName(thatchFence, TCNames.thatchFence);
		registerBlockNoName(palmFence, TCNames.palmFence);
	//	registerBlockNoName(mahoganyFence, TCNames.mahoganyFence);
		registerMultiBlock(saplings, TCNames.sapling, TCNames.saplingNames);
		registerBlock(coffeePlant, TCNames.coffeePlant);
		registerBlock(bambooFenceGate, TCNames.bambooFenceGate);
		registerBlock(palmFenceGate, TCNames.palmFenceGate);
		Blocks.fire.setFireInfo(palmFenceGate, 5, 5);
		registerBlock(tikiTorch, TCNames.tikiTorch);
		registerBlock(bambooDoor, TCNames.bambooDoor);
		registerMultiBlock(singleSlabs, TCNames.singleSlabs, ItemTropicraftSlab.class, new Object[]{singleSlabs, doubleSlabs, false});
		registerMultiBlock(doubleSlabs, TCNames.doubleSlabs, ItemTropicraftSlab.class, new Object[]{doubleSlabs, singleSlabs, true});
		registerBlock(tropicsWater, TCNames.stillWater);
		registerBlock(rainStopper, TCNames.rainStopper);
		registerMultiBlock(flowers, TCNames.flower, TCNames.flowerIndices);
		registerBlock(flowerPot, TCNames.flowerPot);
		registerBlock(coconut, TCNames.coconut);
		registerBlock(firePit, TCNames.firePit);
		registerBlock(airCompressor, TCNames.airCompressor);
		registerBlock(bambooChest, TCNames.bambooChest);
		registerBlock(tropicsPortal, TCNames.portal);
		registerBlock(tropicsPortalWall, TCNames.portalWall);
	}
	
	private static void registerMultiBlock(Block block, String name, Class<? extends ItemBlock> c, Object[] params) {
		GameRegistry.registerBlock(block, c, "tile." + name, params);
		block.setBlockName(name);
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
		Class<? extends ItemBlock> clazz;
		clazz = multiBlockMap.containsKey(name) ? multiBlockMap.get(name) : ItemBlockTropicraft.class;
		GameRegistry.registerBlock(block, clazz, "tile." + name, namesList);
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
	
	/**
	 * Helper method for registering a block with Forge w/o setting the block name
	 * @param block Block instance
	 * @param name Name of the block
	 */
	private static void registerBlockNoName(Block block, String name) {
		GameRegistry.registerBlock(block, "tile." + name);
		//block.setBlockName(name);
	}
}
