package net.tropicraft.core.registry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
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
import net.tropicraft.core.common.enums.TropicraftBundles;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;
import net.tropicraft.core.common.enums.TropicraftPlanks;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.common.enums.TropicraftSlabs;
import net.tropicraft.core.common.itemblock.ItemBlockTropicraft;
import net.tropicraft.core.common.itemblock.ItemTropicraftSlab;
import net.tropicraft.core.registry.ItemRegistry.IBlockItemRegistrar;

public class BlockRegistry extends TropicraftRegistry {
    
    private static class StandardItemCreator implements IBlockItemRegistrar {
        private final List<String> names;
        
        public StandardItemCreator(String... names) {
            this(Lists.newArrayList(names));
        }
        
        public StandardItemCreator(IStringSerializable... names) {
            this(Arrays.stream(names).map(IStringSerializable::toString).collect(Collectors.toList()));
        }
        
        public StandardItemCreator(List<String> names) {
            this.names = names;
        }
        
        public StandardItemCreator withPrefix(String prefix) {
            return new StandardItemCreator(names) {
                @Override
                protected void registerVariant(Block block, Item item, String name, int meta) {
                    super.registerVariant(block, item, prefix + "_" + name, meta);
                }
            };
        }

        @Override
        public Item getItem(Block block) {
            return new ItemBlockTropicraft(block, names);
        }
        
        protected void registerVariant(Block block, Item item, String name, int meta) {
            Tropicraft.proxy.registerBlockVariantModels(block, item);
        }
        
        @Override
        public void postRegister(Block block, Item item) {
            for (int i = 0; i < names.size(); i++) {
                registerVariant(block, item, names.get(i), i);
            }
        }
    }

    public static Block chunk;

    public static BlockTropicraftEnumVariants<TropicraftOreBlocks> ore;
    public static BlockTropicraftEnumVariants<TropicraftOreBlocks> oreBlock;

	public static BlockTropicsFlowers flowers;
	public static Block logs;
	public static Block coral;
	public static Block seaweed;

    // purified sand AND mineral sands. Oh variants, what can't you do?
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
		ore = registerMultiBlock(new BlockTropicraftOre(), "ore", new StandardItemCreator(TropicraftOreBlocks.VALUES));
		oreBlock = registerMultiBlock(new BlockTropicraftOreBlock(), "oreblock", new StandardItemCreator(TropicraftOreBlocks.VALUES));
		flowers = registerMultiBlock(new BlockTropicsFlowers(), "flower", new StandardItemCreator(TropicraftFlowers.VALUES));
		logs = registerMultiBlock(new BlockTropicraftLog(Names.LOG_NAMES), "log", new StandardItemCreator(Names.LOG_NAMES));
		coral = registerMultiBlock(new BlockCoral(Names.CORAL_NAMES), "coral", new StandardItemCreator(Names.CORAL_NAMES));
		bundles = registerMultiBlock(new BlockBundle(Material.WOOD, Names.BUNDLE_NAMES), "bundle", new StandardItemCreator(Names.BUNDLE_NAMES));
		seaweed = registerBlockNoItem(new BlockSeaweed(), "seaweed", false);

		slabs = new BlockTropicraftSlab(Material.WOOD, false);
		doubleSlabs = new BlockTropicraftSlab(Material.WOOD, true);
		
		IBlockItemRegistrar slabRegistrar = new StandardItemCreator(TropicraftSlabs.VALUES) {
		    
		    @Override
		    public ItemTropicraftSlab getItem(Block block) {
		        return new ItemTropicraftSlab(block, slabs, doubleSlabs);
		    }
		};
		
		slabs = registerMultiBlock(slabs, "slab", slabRegistrar);
		doubleSlabs = registerMultiBlock(doubleSlabs, "double_slab", slabRegistrar);
		
		planks = registerMultiBlock(new BlockTropicraftPlank(Material.WOOD, Names.LOG_NAMES), "plank", new StandardItemCreator(Names.LOG_NAMES));
		bambooShoot = registerBlock(new BlockBambooShoot(), Names.BAMBOO_SHOOT, null);

		thatchStairs = registerBlock(new BlockTropicraftStairs(bundles.getDefaultState().withProperty(BlockBundle.VARIANT, TropicraftBundles.THATCH)), Names.BLOCK_THATCH_STAIRS);
		bambooStairs = registerBlock(new BlockTropicraftStairs(bundles.getDefaultState().withProperty(BlockBundle.VARIANT, TropicraftBundles.BAMBOO)), Names.BLOCK_BAMBOO_STAIRS);
		palmStairs = registerBlock(new BlockTropicraftStairs(planks.getDefaultState().withProperty(BlockTropicraftPlank.VARIANT, TropicraftPlanks.PALM)), Names.BLOCK_PALM_STAIRS);
		chunkStairs = registerBlock(new BlockTropicraftStairs(chunk.getDefaultState()), Names.BLOCK_CHUNK_O_HEAD_STAIRS);
		
		tropicsWater = registerBlockNoItem(new BlockTropicsWater(FluidRegistry.tropicsWater, Material.WATER), Names.TROPICS_WATER, false);
		tropicsPortal = registerBlockNoItem(new BlockTropicsPortal(FluidRegistry.tropicsPortal, Material.WATER), Names.TROPICS_PORTAL, false);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsWater, Names.TROPICS_WATER);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsPortal, Names.TROPICS_PORTAL);
		portalWall = registerBlock(new BlockPortalWall(), Names.PORTAL_WALL);

		leaves = registerMultiBlock(new BlockTropicraftLeaves(Names.LEAF_NAMES), "leaves", new StandardItemCreator(Names.LEAF_NAMES));
		fruitLeaves = registerMultiBlock(new BlockFruitLeaves(Names.FRUIT_LEAF_NAMES), "leaves_fruit", new StandardItemCreator(Names.FRUIT_LEAF_NAMES));

		bambooChest = registerBlock(new BlockBambooChest(), Names.BAMBOO_CHEST);
		
		saplings = registerMultiBlock(new BlockTropicsSapling(Names.SAPLING_NAMES), "sapling", new StandardItemCreator(Names.SAPLING_NAMES).withPrefix("sapling"));
		
		coconut = registerBlock(new BlockCoconut(), Names.COCONUT);

		pineapple = registerMultiBlock(new BlockPineapple(Names.TALL_PLANT_NAMES), "pineapple", new StandardItemCreator(Names.TALL_PLANT_NAMES));
		iris = registerMultiBlock(new BlockIris(Names.TALL_PLANT_NAMES), "iris", new StandardItemCreator(Names.TALL_PLANT_NAMES));
		coffeePlant = registerMultiBlock(new BlockCoffeeBush(), "coffee_bush");
		
		// TODO refactor this whole class so things like this are possible, because wtf
		sands = new BlockTropicraftSands().setRegistryName("sand").setUnlocalizedName(Info.MODID + ".sand").setCreativeTab(CreativeTabRegistry.tropicraftTab);
		GameRegistry.register(sands);
		GameRegistry.register(new ItemBlockTropicraft(sands, Lists.newArrayList(Arrays.stream(TropicraftSands.values()).map(IStringSerializable::getName).toArray(String[]::new))).setRegistryName(sands.getRegistryName()));
		for (TropicraftSands sand : TropicraftSands.values()) {
		    Tropicraft.proxy.registerItemVariantModel(Item.getItemFromBlock(sands), "sand", sand.ordinal(), "underwater=false,variant=" + sand.getName());
		}
		
		volcano = registerBlock(new BlockVolcano(), Names.VOLCANO);
		
		tikiTorch = registerMultiBlock(new BlockTikiTorch(), "tiki_torch", new StandardItemCreator("upper", "middle", "lower"));
		
		drinkMixer = registerBlock(new BlockDrinkMixer(), Names.DRINK_MIXER);
		sifter = registerBlock(new BlockSifter(), Names.SIFTER);
		flowerPot = registerBlockNoItem(new BlockTropicraftFlowerPot(), Names.FLOWER_POT, false);
		bambooDoor = registerBlockNoItem(new BlockBambooDoor(), Names.BAMBOO_DOOR, false);

		bongo = registerMultiBlock(new BlockBongoDrum(), Names.BONGO, new StandardItemCreator(Names.BONGO_NAMES).withPrefix(Names.BONGO));
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
		return registerBlock(block, name, new StandardItemCreator(name), true, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlock(T block, String name, boolean registerDefaultVariant) {
		return registerBlock(block, name, new StandardItemCreator(name), registerDefaultVariant, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlockNoItem(T block, String name, boolean registerDefaultVariant) {
		return registerBlock(block, name, null, registerDefaultVariant, CreativeTabRegistry.tropicraftTab);
	}

	public static <T extends Block> T registerBlock(T block, String name, CreativeTabs tab) {
		return registerBlock(block, name, new StandardItemCreator(name), true, tab);
	}

	public static void registerOreDictWildcard(String oreDictName, Block block) {
		OreDictionary.registerOre(oreDictName, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
	}
}