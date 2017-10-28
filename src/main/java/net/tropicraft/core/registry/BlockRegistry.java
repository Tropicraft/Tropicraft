package net.tropicraft.core.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.tropicraft.Info;
import net.tropicraft.Names;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.BlockBambooChest;
import net.tropicraft.core.common.block.BlockBambooDoor;
import net.tropicraft.core.common.block.BlockBambooShoot;
import net.tropicraft.core.common.block.BlockBongoDrum;
import net.tropicraft.core.common.block.BlockBundle;
import net.tropicraft.core.common.block.BlockChunkOHead;
import net.tropicraft.core.common.block.BlockCoconut;
import net.tropicraft.core.common.block.BlockCoffeeBush;
import net.tropicraft.core.common.block.BlockCoral;
import net.tropicraft.core.common.block.BlockDrinkMixer;
import net.tropicraft.core.common.block.BlockFruitLeaves;
import net.tropicraft.core.common.block.BlockIris;
import net.tropicraft.core.common.block.BlockPineapple;
import net.tropicraft.core.common.block.BlockPortalWall;
import net.tropicraft.core.common.block.BlockSeaweed;
import net.tropicraft.core.common.block.BlockSifter;
import net.tropicraft.core.common.block.BlockTikiTorch;
import net.tropicraft.core.common.block.BlockTropicraftEnumVariants;
import net.tropicraft.core.common.block.BlockTropicraftFlowerPot;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.block.BlockTropicraftOre;
import net.tropicraft.core.common.block.BlockTropicraftOreBlock;
import net.tropicraft.core.common.block.BlockTropicraftPlank;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.block.BlockTropicraftSlab;
import net.tropicraft.core.common.block.BlockTropicraftStairs;
import net.tropicraft.core.common.block.BlockTropicsFlowers;
import net.tropicraft.core.common.block.BlockTropicsPortal;
import net.tropicraft.core.common.block.BlockTropicsSapling;
import net.tropicraft.core.common.block.BlockTropicsWater;
import net.tropicraft.core.common.block.BlockVolcano;
import net.tropicraft.core.common.block.ITropicraftBlock;
import net.tropicraft.core.common.enums.ITropicraftVariant;
import net.tropicraft.core.common.enums.TropicraftBongos;
import net.tropicraft.core.common.enums.TropicraftBundles;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.common.enums.TropicraftFruitLeaves;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;
import net.tropicraft.core.common.enums.TropicraftPlanks;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.common.enums.TropicraftSaplings;
import net.tropicraft.core.common.enums.TropicraftSlabs;
import net.tropicraft.core.common.enums.TropicraftTallPlants;
import net.tropicraft.core.common.itemblock.ItemBlockTropicraft;
import net.tropicraft.core.common.itemblock.ItemTropicraftSlab;
import net.tropicraft.core.registry.ItemRegistry.IBlockItemRegistrar;

public class BlockRegistry extends TropicraftRegistry {
    
    private static class SimpleItemCreator implements IBlockItemRegistrar {
        private final String name;
        
        public SimpleItemCreator(String name) {
            this.name = name;
        }
        
        @Override
        public Item getItem(Block block) {
            return new ItemBlockTropicraft(block);
        }
        
        @Override
        public void postRegister(Block block, Item item) {
            Tropicraft.proxy.registerItemVariantModel(item, name, 0, "inventory");            
        }
    }
    
    private static class StandardItemCreator implements IBlockItemRegistrar {
        private final List<ITropicraftVariant> variants;
        
        public StandardItemCreator(ITropicraftVariant... variants) {
            this.variants = Lists.newArrayList(variants);
        }

        @Override
        public Item getItem(Block block) {
            return new ItemBlockTropicraft(block, variants.toArray(new ITropicraftVariant[variants.size()]));
        }

        private void registerVariant(Block block, Item item, String name, int meta) {
            Tropicraft.proxy.registerItemVariantModel(item, name, meta, "inventory");
        }
        
        @Override
        public void postRegister(Block block, Item item) {
            for (int i = 0; i < variants.size(); i++) {
                registerVariant(block, item, variants.get(i).getSimpleName() + "_" + variants.get(i).getTypeName(), i);
            }
        }
    }
    
    private static class MultiBlockItemCreator extends StandardItemCreator {
        
        public MultiBlockItemCreator(ITropicraftVariant... names) {
            super(names);
        }

        @Override
        public void postRegister(Block block, Item item) {
            Tropicraft.proxy.registerBlockVariantModels(block, item);
        }
    }

    public static Block chunk;

    public static BlockTropicraftEnumVariants<TropicraftOreBlocks> ore;
    public static BlockTropicraftEnumVariants<TropicraftOreBlocks> oreBlock;

	public static BlockTropicsFlowers flowers;
	public static Block logs;
	public static BlockTropicraftEnumVariants<TropicraftCorals> coral;
	public static Block seaweed;

    // purified sand AND mineral sands. Oh variants, what can't you do?
    public static Block sands;

    /** Thatch and bamboo bundles */
    public static BlockTropicraftEnumVariants<TropicraftBundles> bundles;

    /** Log planks */
    public static BlockTropicraftEnumVariants<TropicraftPlanks> planks;

    /** Bamboo chute (plant) */
    public static BlockBambooShoot bambooShoot;

    /** Stairs */
    public static Block chunkStairs;
    public static Block thatchStairs;
    public static Block bambooStairs;
    public static Block palmStairs;

    /** Fluids */
    public static BlockTropicsWater tropicsWater;
    public static BlockTropicsPortal tropicsPortal;
    public static BlockPortalWall portalWall;

	/** Leaves */
	public static Block leaves;
	public static Block fruitLeaves;

	public static Block bambooChest;
	
	public static Block saplings;
	
	public static Block coconut;
	
	public static Block pineapple;
	public static Block iris;
	public static BlockCoffeeBush coffeePlant;
	
	public static Block volcano;
	
	public static Block tikiTorch;
	
	public static Block drinkMixer;
	public static Block sifter;
	public static Block flowerPot;
	public static Block bambooDoor;
	public static BlockTropicraftSlab slabs;
	public static BlockTropicraftSlab doubleSlabs;

	public static Block bongo;

	/**
	 * Register blocks in preInit
	 */
	public static void preInit() {
		chunk = registerBlock(new BlockChunkOHead(), Names.BLOCK_CHUNK_O_HEAD);
		ore = registerMultiBlock(new BlockTropicraftOre(), "ore", new MultiBlockItemCreator(TropicraftOreBlocks.VALUES));
		oreBlock = registerMultiBlock(new BlockTropicraftOreBlock(), "oreblock", new MultiBlockItemCreator(TropicraftOreBlocks.VALUES));
		flowers = registerMultiBlock(new BlockTropicsFlowers(), "flower", new MultiBlockItemCreator(TropicraftFlowers.VALUES));
		logs = registerMultiBlock(new BlockTropicraftLog(), "log", new MultiBlockItemCreator(TropicraftLogs.values()));
		coral = registerMultiBlock(new BlockCoral(), "coral", new MultiBlockItemCreator(TropicraftCorals.VALUES));
		bundles = registerMultiBlock(new BlockBundle(Material.WOOD), "bundle", new StandardItemCreator(TropicraftBundles.values()));
		seaweed = registerBlockNoItem(new BlockSeaweed(), "seaweed", false);

		slabs = new BlockTropicraftSlab(Material.WOOD, false);
		doubleSlabs = new BlockTropicraftSlab(Material.WOOD, true);
		
		IBlockItemRegistrar slabRegistrar = new MultiBlockItemCreator(TropicraftSlabs.VALUES) {
		    
		    @Override
		    public ItemTropicraftSlab getItem(Block block) {
		        return new ItemTropicraftSlab(block, slabs, doubleSlabs);
		    }
		};
		
		slabs = registerMultiBlock(slabs, "slab", slabRegistrar);
		doubleSlabs = registerMultiBlock(doubleSlabs, "double_slab", slabRegistrar);
		
		planks = registerMultiBlock(new BlockTropicraftPlank(Material.WOOD), "plank", new MultiBlockItemCreator(TropicraftPlanks.VALUES));
		bambooShoot = registerBlock(new BlockBambooShoot(), Names.BAMBOO_SHOOT, null);

		thatchStairs = registerBlock(new BlockTropicraftStairs(bundles.defaultForVariant(TropicraftBundles.THATCH)), Names.BLOCK_THATCH_STAIRS);
		bambooStairs = registerBlock(new BlockTropicraftStairs(bundles.defaultForVariant(TropicraftBundles.BAMBOO)), Names.BLOCK_BAMBOO_STAIRS);
		palmStairs = registerBlock(new BlockTropicraftStairs(planks.defaultForVariant(TropicraftPlanks.PALM)), Names.BLOCK_PALM_STAIRS);
		chunkStairs = registerBlock(new BlockTropicraftStairs(chunk.getDefaultState()), Names.BLOCK_CHUNK_O_HEAD_STAIRS);
		
		tropicsWater = registerBlockNoItem(new BlockTropicsWater(FluidRegistry.tropicsWater, Material.WATER), Names.TROPICS_WATER, false);
		tropicsPortal = registerBlockNoItem(new BlockTropicsPortal(FluidRegistry.tropicsPortal, Material.WATER), Names.TROPICS_PORTAL, false);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsWater, Names.TROPICS_WATER);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsPortal, Names.TROPICS_PORTAL);
		portalWall = registerBlock(new BlockPortalWall(), Names.PORTAL_WALL);

		leaves = registerMultiBlock(new BlockTropicraftLeaves(), "leaves", new MultiBlockItemCreator(TropicraftLeaves.VALUES));
		fruitLeaves = registerMultiBlock(new BlockFruitLeaves(), "leaves_fruit", new MultiBlockItemCreator(TropicraftFruitLeaves.VALUES));

		bambooChest = registerBlock(new BlockBambooChest(), Names.BAMBOO_CHEST);
		
		saplings = registerMultiBlock(new BlockTropicsSapling(), "sapling", new MultiBlockItemCreator(TropicraftSaplings.VALUES));
		
		coconut = registerBlock(new BlockCoconut(), Names.COCONUT);

		pineapple = registerMultiBlock(new BlockPineapple(), "pineapple", new MultiBlockItemCreator(TropicraftTallPlants.values()));
		iris = registerMultiBlock(new BlockIris(), "iris", new MultiBlockItemCreator(TropicraftTallPlants.values()));
		coffeePlant = registerMultiBlock(new BlockCoffeeBush(), "coffee_bush");
		
		// TODO refactor this whole class so things like this are possible, because wtf
		sands = registerMultiBlock(new BlockTropicraftSands(), "sand", new MultiBlockItemCreator(TropicraftSands.VALUES));
		
		volcano = registerBlock(new BlockVolcano(), Names.VOLCANO);
		
		tikiTorch = registerBlock(new BlockTikiTorch(), "tiki_torch");
		
		drinkMixer = registerBlock(new BlockDrinkMixer(), Names.DRINK_MIXER);
		sifter = registerBlock(new BlockSifter(), Names.SIFTER);
		flowerPot = registerBlockNoItem(new BlockTropicraftFlowerPot(), Names.FLOWER_POT, false);
		bambooDoor = registerBlockNoItem(new BlockBambooDoor(), Names.BAMBOO_DOOR, false);

		bongo = registerMultiBlock(new BlockBongoDrum(), Names.BONGO, new MultiBlockItemCreator(TropicraftBongos.VALUES));
	}

	public static void init() {

	}
	
	public static void clientProxyInit() {
		Tropicraft.proxy.registerColoredBlock(sands);
	}

	public static <T extends Block> T registerBlock(T block, String name, IBlockItemRegistrar item, boolean registerDefaultVariant, CreativeTabs tab) {
		block.setUnlocalizedName(getNamePrefixed(name));
		block.setRegistryName(new ResourceLocation(Info.MODID, name));
		GameRegistry.register(block);
		block.setCreativeTab(tab);

		if (item != null) {
		    ItemRegistry.addBlockItem(block, item);
		}

		if (registerDefaultVariant) {
			registerBlockVariant(block, name, 0);
		}

		return block;
	}
	
	private static <T extends Block & ITropicraftBlock> T registerMultiBlock(T block, String name) {
	    return registerMultiBlock(block, name, null);
	}

	/**
	 * Register a block with metadata
	 * @param block Block being registered
	 * @param name Name of the image prefix
	 * @param names Names of the images
	 */
	private static <T extends Block & ITropicraftBlock> T registerMultiBlock(T block, String name, IBlockItemRegistrar item) {
	    return registerBlock(block, name, item, false, CreativeTabRegistry.tropicraftTab);
//
//        block = registerBlock(block, name, item, false, CreativeTabRegistry.tropicraftTab);
//
//        // get the preset blocks variants
//        ImmutableList<IBlockState> presets = block.getBlockState().getValidStates();
//
//        if (presets.isEmpty()) {
//            // block has no sub-blocks to register
//        } else {
//            // register all the sub-blocks
//            for (IBlockState state : presets) {
//                String stateName = block.getStateName(state);
//                int stateMeta = block.getMetaFromState(state);
//                // System.out.println("Registering " + name + " with stateName " + stateName + " and meta " + stateMeta);
////                registerBlockVariant(block, stateName, stateMeta, null);
//            }
//        }
//
//        block.setCreativeTab(CreativeTabRegistry.tropicraftTab);
//
//        return block;
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
		return registerBlock(block, name, new SimpleItemCreator(name), true, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlock(T block, String name, boolean registerDefaultVariant) {
		return registerBlock(block, name, new SimpleItemCreator(name), registerDefaultVariant, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlockNoItem(T block, String name, boolean registerDefaultVariant) {
		return registerBlock(block, name, null, registerDefaultVariant, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlock(T block, String name, CreativeTabs tab) {
		return registerBlock(block, name, new SimpleItemCreator(name), true, tab);
	}

	public static void registerOreDictWildcard(String oreDictName, Block block) {
		OreDictionary.registerOre(oreDictName, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
	}
}