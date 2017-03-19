package net.tropicraft.core.registry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.tropicraft.Info;
import net.tropicraft.Names;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.BlockBambooChest;
import net.tropicraft.core.common.block.BlockBambooShoot;
import net.tropicraft.core.common.block.BlockBundle;
import net.tropicraft.core.common.block.BlockChunkOHead;
import net.tropicraft.core.common.block.BlockCoconut;
import net.tropicraft.core.common.block.BlockCoral;
import net.tropicraft.core.common.block.BlockFruitLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.block.BlockTropicraftOre;
import net.tropicraft.core.common.block.BlockTropicraftOreBlock;
import net.tropicraft.core.common.block.BlockTropicraftPlank;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.block.BlockTropicraftStairs;
import net.tropicraft.core.common.block.BlockTropicsFlowers;
import net.tropicraft.core.common.block.BlockTropicsSapling;
import net.tropicraft.core.common.block.BlockTropicsWater;
import net.tropicraft.core.common.block.BlockVolcano;
import net.tropicraft.core.common.block.ITropicraftBlock;
import net.tropicraft.core.common.enums.TropicraftBundles;
import net.tropicraft.core.common.itemblock.ItemBlockTropicraft;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ObjectArrays;

public class BlockRegistry extends TropicraftRegistry {

	public static Block chunk;

	public static Block oreAzurite, oreEudialyte, oreZircon;
	public static Block oreBlock;

	public static Block flowers;
	public static Block logs;
	public static Block coral;

	// purified sand AND mineral sands. Oh variants, what can't you do?
	// TODO
	public static Block sands;

	/** Thatch and bamboo bundles */
	public static Block bundles;

	/** Log planks */
	public static Block planks;

	/** Bamboo chute (plant) */
	public static BlockBambooShoot bambooShoot;

	/** Stairs */
	public static Block chunkStairs;
	public static Block thatchStairs;
	public static Block bambooStairs;

	/** Fluids */
	public static BlockTropicsWater tropicsWater;

	/** Leaves */
	public static Block leaves;
	public static Block fruitLeaves;

	public static Block bambooChest;
	
	public static Block saplings;
	
	public static Block coconut;
	
	public static Block pineapple;
	
	public static Block volcano;

	/**
	 * Register blocks in preInit
	 */
	public static void preInit() {
		chunk = registerBlock(new BlockChunkOHead(), Names.BLOCK_CHUNK_O_HEAD);
		chunkStairs = registerBlock(new BlockTropicraftStairs(chunk.getDefaultState()), Names.BLOCK_CHUNK_O_HEAD_STAIRS);
		oreAzurite = registerBlock(new BlockTropicraftOre(), Names.BLOCK_AZURITE_ORE);
		oreEudialyte = registerBlock(new BlockTropicraftOre(), Names.BLOCK_EUDIALYTE_ORE);
		oreZircon = registerBlock(new BlockTropicraftOre(), Names.BLOCK_ZIRCON_ORE);
		oreBlock = registerMultiBlock(new BlockTropicraftOreBlock(Names.BLOCK_ORE_NAMES), ItemBlockTropicraft.class, "oreblock", asList(Names.BLOCK_ORE_NAMES));
		flowers = registerMultiBlock(new BlockTropicsFlowers(Names.FLOWER_NAMES), ItemBlockTropicraft.class, "flower", asList(Names.FLOWER_NAMES));
		logs = registerMultiBlock(new BlockTropicraftLog(Names.LOG_NAMES), ItemBlockTropicraft.class, "log", asList(Names.LOG_NAMES));
		coral = registerMultiBlock(new BlockCoral(Names.CORAL_NAMES), ItemBlockTropicraft.class, "coral", asList(Names.CORAL_NAMES));
		bundles = registerMultiBlock(new BlockBundle(Material.PLANTS, Names.BUNDLE_NAMES), ItemBlockTropicraft.class, "bundle", asList(Names.BUNDLE_NAMES));

		planks = registerMultiBlock(new BlockTropicraftPlank(Material.WOOD, Names.LOG_NAMES), ItemBlockTropicraft.class, "plank", asList(Names.LOG_NAMES));
		bambooShoot = registerBlock(new BlockBambooShoot(), Names.BAMBOO_SHOOT, null);

		thatchStairs = registerBlock(new BlockTropicraftStairs(bundles.getDefaultState().withProperty(BlockBundle.VARIANT, TropicraftBundles.THATCH)), Names.BLOCK_THATCH_STAIRS);
		bambooStairs = registerBlock(new BlockTropicraftStairs(bundles.getDefaultState().withProperty(BlockBundle.VARIANT, TropicraftBundles.BAMBOO)), Names.BLOCK_BAMBOO_STAIRS);

		tropicsWater = registerBlockNoItem(new BlockTropicsWater(FluidRegistry.tropicsWater, Material.WATER), Names.TROPICS_WATER, false);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsWater, Names.TROPICS_WATER);

		leaves = registerMultiBlock(new BlockTropicraftLeaves(Names.LEAF_NAMES), ItemBlockTropicraft.class, "leaves", asList(Names.LEAF_NAMES));
		fruitLeaves = registerMultiBlock(new BlockFruitLeaves(Names.FRUIT_LEAF_NAMES), ItemBlockTropicraft.class, "leaves_fruit", asList(Names.FRUIT_LEAF_NAMES));

		bambooChest = registerBlock(new BlockBambooChest(), Names.BAMBOO_CHEST);
		
		saplings = registerMultiBlock(new BlockTropicsSapling(Names.SAPLING_NAMES), ItemBlockTropicraft.class, "sapling", asList(Names.SAPLING_NAMES));
		
		coconut = registerBlock(new BlockCoconut(), Names.COCONUT);

		//pineapple = registerMultiBlock(new BlockPineapple(Names.PINEAPPLE_NAMES), ItemBlockTropicraft.class, "pineapple", asList(Names.PINEAPPLE_NAMES));
		
		sands = registerMultiColoredBlock(new BlockTropicraftSands(Names.SAND_NAMES), ItemBlockTropicraft.class, "sand", asList(Names.SAND_NAMES));
		
		volcano = registerBlock(new BlockVolcano(), Names.VOLCANO);
	}

	public static void init() {

	}
	
	public static void clientProxyInit() {
		Tropicraft.proxy.registerColoredBlock(sands);
	}
	
	private static Block registerMultiColoredBlock(Block block, Class<? extends ItemBlock> clazz, String name, Object... itemCtorArgs) {
		return registerMultiBlock(block, clazz, name, itemCtorArgs);
	}

	private static <T> List<T> asList(T[] objects) {
		List<T> objList = new ArrayList<T>();
		Collections.addAll(objList, objects);

		return objList;
	}

	public static <T extends Block> T registerBlock(T block, ItemBlock itemBlock, String name, boolean registerDefaultVariant, CreativeTabs tab) {
		block.setUnlocalizedName(getNamePrefixed(name));
		block.setRegistryName(new ResourceLocation(Info.MODID, name));
		GameRegistry.register(block);
		block.setCreativeTab(tab);

		if (itemBlock != null) {
			itemBlock.setRegistryName(new ResourceLocation(Info.MODID, name));
			GameRegistry.register(itemBlock);
		}

		if (registerDefaultVariant) {
			registerBlockVariant(block, name, 0);
		}

		return block;
	}

	/**
	 * Register a block with metadata
	 * @param block Block being registered
	 * @param name Name of the image prefix
	 * @param names Names of the images
	 */
	private static Block registerMultiBlock(Block block, Class<? extends ItemBlock> clazz, String name, Object... itemCtorArgs) {
		try {
			// Some nice code borrowed from old FML and repurposed
			Class<?>[] ctorArgClasses = new Class<?>[itemCtorArgs.length + 1];
			ctorArgClasses[0] = Block.class;
			for (int idx = 1; idx < ctorArgClasses.length; idx++)
			{
				ctorArgClasses[idx] = itemCtorArgs[idx - 1].getClass();
			}
			Constructor<? extends ItemBlock> itemConstructor = clazz.getConstructor(ctorArgClasses);
			ItemBlock itemBlockInstance = itemConstructor.newInstance(ObjectArrays.concat(block, itemCtorArgs));

			block = registerBlock(block, itemBlockInstance, name, false, CreativeTabRegistry.tropicraftTab);

			// get the preset blocks variants
			ImmutableSet<IBlockState> presets = getBlockPresets(block);
			ITropicraftBlock tcBlock = (ITropicraftBlock)block;

			if (presets.isEmpty()) {
				// block has no sub-blocks to register
				registerBlockVariant(block, name, 0);
			} else {
				// register all the sub-blocks
				for (IBlockState state : presets) {
					String stateName = tcBlock.getStateName(state);
					int stateMeta = block.getMetaFromState(state);
					// System.out.println("Registering " + name + " with stateName " + stateName + " and meta " + stateMeta);
					registerBlockVariant(block, name, stateMeta, stateName);
				}
			}

			block.setCreativeTab(CreativeTabRegistry.tropicraftTab);

			return block;
		} catch (Exception e) {
			System.err.println("Tropicraft failed trying to register multi block: " + name);
			e.printStackTrace();
		}

		return null;
	}

	public static void registerBlockVariant(Block block, String stateName, int stateMeta) {
		Item item = Item.getItemFromBlock(block);
		Tropicraft.proxy.registerItemVariantModel(item, stateName, stateMeta);
	}

	/**
	 * Built especially for registering blocks with multiple variants
	 * @param block
	 * @param registryName
	 * @param stateMeta
	 * @param variantName
	 */
	public static void registerBlockVariant(Block block, String registryName, int stateMeta, String variantName) {
		Item item = Item.getItemFromBlock(block);
		Tropicraft.proxy.registerItemVariantModel(item, registryName, stateMeta, variantName);
	}

	public static <T extends Block> T registerBlock(T block, String name) {
		return registerBlock(block, new ItemBlock(block), name, true, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlock(T block, String name, boolean registerDefaultVariant) {
		return registerBlock(block, new ItemBlock(block), name, registerDefaultVariant, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlockNoItem(T block, String name, boolean registerDefaultVariant) {
		return registerBlock(block, null, name, registerDefaultVariant, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlock(T block, String name, CreativeTabs tab) {
		return registerBlock(block, new ItemBlock(block), name, true, tab);
	}

	public static void registerOreDictWildcard(String oreDictName, Block block) {
		OreDictionary.registerOre(oreDictName, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
	}

	// return all of the different 'preset' variants of a block
	// works by looping through all the different values of the properties specified in block.getProperties()
	// only works on blocks supporting ITropicraft - returns an empty set for vanilla blocks
	// Thanks to our friends at BoP for this awesome code
	public static ImmutableSet<IBlockState> getBlockPresets(Block block) {
		if (!(block instanceof ITropicraftBlock)) {return ImmutableSet.<IBlockState>of();}
		IBlockState defaultState = block.getDefaultState();
		if (defaultState == null) {defaultState = block.getBlockState().getBaseState();}
		return getStatesSet(defaultState, ((ITropicraftBlock)block).getProperties());        
	}    

	// returns a set of states, one for every possible combination of values from the provided properties
	public static ImmutableSet<IBlockState> getStatesSet(IBlockState baseState, IProperty... properties)
	{        
		Stack<IProperty> propStack = new Stack<IProperty>();
		List<IBlockState> states = new ArrayList<IBlockState>();
		for (IProperty prop : properties) {propStack.push(prop);}
		if (!propStack.isEmpty())
		{
			addStatesToList(baseState, states, propStack);
		}
		ImmutableSet<IBlockState> ret = ImmutableSet.copyOf(states);
		return ret;
	}

	// recursively add state values to a list
	private static void addStatesToList(IBlockState state, List<IBlockState> list, Stack<IProperty> stack)
	{    
		if (stack.empty())
		{
			list.add(state);
			return;
		}
		else
		{
			IProperty prop = stack.pop();        
			for (Object value : prop.getAllowedValues())
			{
				addStatesToList(state.withProperty(prop, (Comparable)value), list, stack);
			}
			stack.push(prop);
		}
	}
}