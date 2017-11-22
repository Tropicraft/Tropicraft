package net.tropicraft.core.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
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
import net.tropicraft.core.common.block.BlockTropicraftFence;
import net.tropicraft.core.common.block.BlockTropicraftFlowerPot;
import net.tropicraft.core.common.block.BlockTropicraftLadder;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.block.BlockTropicraftOre;
import net.tropicraft.core.common.block.BlockTropicraftOreBlock;
import net.tropicraft.core.common.block.BlockTropicraftPlank;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.block.BlockTropicraftSlab;
import net.tropicraft.core.common.block.BlockTropicraftStairs;
import net.tropicraft.core.common.block.BlockTropicraftStairsFuzzy;
import net.tropicraft.core.common.block.BlockTropicsFlowers;
import net.tropicraft.core.common.block.BlockTropicsPortal;
import net.tropicraft.core.common.block.BlockTropicsSapling;
import net.tropicraft.core.common.block.BlockTropicsWater;
import net.tropicraft.core.common.block.BlockVolcano;
import net.tropicraft.core.common.block.scuba.BlockAirCompressor;
import net.tropicraft.core.common.enums.BlockHardnessValues;
import net.tropicraft.core.common.enums.ITropicraftVariant;
import net.tropicraft.core.common.enums.TropicraftBongos;
import net.tropicraft.core.common.enums.TropicraftBundles;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.common.enums.TropicraftFruitLeaves;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.common.enums.TropicraftOres;
import net.tropicraft.core.common.enums.TropicraftPlanks;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.common.enums.TropicraftSaplings;
import net.tropicraft.core.common.enums.TropicraftSlabs;
import net.tropicraft.core.common.itemblock.ItemBlockTropicraft;
import net.tropicraft.core.common.itemblock.ItemTropicraftSlab;
import net.tropicraft.core.registry.ItemRegistry.IBlockItemRegistrar;

@Mod.EventBusSubscriber(modid = Info.MODID)
public class BlockRegistry extends TropicraftRegistry {
    
    private static class SimpleItemCreator implements IBlockItemRegistrar {
        private final String name;
        private final boolean useBlock;
        
        public SimpleItemCreator(String name, boolean useBlock) {
            this.name = name;
            this.useBlock = useBlock;
        }
        
        @Override
        public Item getItem(Block block) {
            return new ItemBlockTropicraft(block);
        }
        
        @Override
        public void postRegister(Block block, Item item) {
            if (useBlock) {
                Tropicraft.proxy.registerBlockVariantModel(block.getDefaultState(), item, 0);
            } else {
                Tropicraft.proxy.registerItemVariantModel(item, name, 0, "inventory");
            }
        }
    }
    
    private static class StandardItemCreator implements IBlockItemRegistrar {
        protected final List<ITropicraftVariant> variants;
        
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

    public static BlockTropicraftEnumVariants<TropicraftOres> ore;
    public static BlockTropicraftEnumVariants<TropicraftOres> oreBlock;

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
    public static Block mahoganyStairs;
    public static Block thatchStairsFuzzy;

    /** Fluids */
    public static BlockTropicsWater tropicsWater;
    public static BlockTropicsPortal tropicsPortal, tropicsPortalTeleporter;
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
	public static Block airCompressor;
	
	public static Block flowerPot;
	public static Block bambooDoor;
	public static BlockTropicraftSlab slabs;
	public static BlockTropicraftSlab doubleSlabs;

	public static Block bongo;

	public static Block bambooFence;
	public static Block thatchFence;
	public static Block chunkFence;
	public static Block palmFence;
	public static Block mahoganyFence;

	public static BlockFenceGate bambooFenceGate;
	public static BlockFenceGate thatchFenceGate;
	public static BlockFenceGate chunkFenceGate;
	public static BlockFenceGate palmFenceGate;
	public static BlockFenceGate mahoganyFenceGate;

	public static Block bambooLadder;

	/**
	 * Register blocks in preInit
	 */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
		chunk = registerBlock(registry, new BlockChunkOHead(), Names.BLOCK_CHUNK_O_HEAD);
		ore = registerBlock(registry, new BlockTropicraftOre(), "ore", new MultiBlockItemCreator(TropicraftOres.VALUES));
		// FIXME ew
		oreBlock = registerBlock(registry, new BlockTropicraftOreBlock(), "oreblock", new MultiBlockItemCreator(TropicraftOres.ORES_WITH_BLOCKS) {
		    @Override
		    public Item getItem(Block block) {
	            return new ItemBlockTropicraft(block, variants.toArray(new ITropicraftVariant[variants.size()])) {
		            @Override
		            public String getUnlocalizedName(ItemStack itemstack) {
		                return super.getUnlocalizedName(itemstack).replaceAll(TropicraftOres.AZURITE.getTypeName(), "oreblock");
		            }
		        };
		    }
		});
		flowers = registerBlock(registry, new BlockTropicsFlowers(), "flower", new StandardItemCreator(TropicraftFlowers.VALUES));
		logs = registerBlock(registry, new BlockTropicraftLog(), "log", new MultiBlockItemCreator(TropicraftLogs.values()));
		coral = registerBlock(registry, new BlockCoral(), "coral", new StandardItemCreator(TropicraftCorals.VALUES));
		bundles = registerBlock(registry, new BlockBundle(Material.WOOD), "bundle", new StandardItemCreator(TropicraftBundles.values()));
		seaweed = registerBlockNoItem(registry, new BlockSeaweed(), "seaweed");

		slabs = new BlockTropicraftSlab(Material.WOOD, false);
		doubleSlabs = new BlockTropicraftSlab(Material.WOOD, true);
		
		IBlockItemRegistrar slabRegistrar = new MultiBlockItemCreator(TropicraftSlabs.VALUES) {
		    
		    @Override
		    public ItemTropicraftSlab getItem(Block block) {
		        return new ItemTropicraftSlab(block, slabs, doubleSlabs);
		    }
		};
		
		slabs = registerBlock(registry, slabs, "slab", slabRegistrar);
		doubleSlabs = registerBlock(registry, doubleSlabs, "double_slab", slabRegistrar);
		
		planks = registerBlock(registry, new BlockTropicraftPlank(Material.WOOD), "plank", new MultiBlockItemCreator(TropicraftPlanks.VALUES));
		bambooShoot = registerBlock(registry, new BlockBambooShoot(), Names.BAMBOO_SHOOT, (IBlockItemRegistrar) null);

		thatchStairs = registerBlock(registry, new BlockTropicraftStairs(bundles.defaultForVariant(TropicraftBundles.THATCH)), Names.BLOCK_THATCH_STAIRS, new SimpleItemCreator(Names.BLOCK_THATCH_STAIRS, true));
		bambooStairs = registerBlock(registry, new BlockTropicraftStairs(bundles.defaultForVariant(TropicraftBundles.BAMBOO)), Names.BLOCK_BAMBOO_STAIRS, new SimpleItemCreator(Names.BLOCK_BAMBOO_STAIRS, true));
		palmStairs = registerBlock(registry, new BlockTropicraftStairs(planks.defaultForVariant(TropicraftPlanks.PALM)), Names.BLOCK_PALM_STAIRS, new SimpleItemCreator(Names.BLOCK_PALM_STAIRS, true));
		chunkStairs = registerBlock(registry, new BlockTropicraftStairs(chunk.getDefaultState()), Names.BLOCK_CHUNK_O_HEAD_STAIRS, new SimpleItemCreator(Names.BLOCK_CHUNK_O_HEAD_STAIRS, true));
		mahoganyStairs = registerBlock(registry, new BlockTropicraftStairs(planks.defaultForVariant(TropicraftPlanks.MAHOGANY)), Names.BLOCK_MAHOGANY_STAIRS, new SimpleItemCreator(Names.BLOCK_MAHOGANY_STAIRS, true));
		thatchStairsFuzzy = registerBlock(registry, new BlockTropicraftStairsFuzzy(bundles.defaultForVariant(TropicraftBundles.THATCH)), Names.BLOCK_THATCH_STAIRS_FUZZY, new SimpleItemCreator(Names.BLOCK_THATCH_STAIRS_FUZZY, true));
		
		tropicsWater = registerBlockNoItem(registry, new BlockTropicsWater(FluidRegistry.tropicsWater, Material.WATER), Names.TROPICS_WATER);
		tropicsPortal = registerBlockNoItem(registry, new BlockTropicsPortal(FluidRegistry.tropicsPortal, Material.WATER, false), Names.TROPICS_PORTAL);
		tropicsPortalTeleporter = registerBlockNoItem(registry, new BlockTropicsPortal(FluidRegistry.tropicsPortal, Material.WATER, true), Names.TROPICS_PORTAL_TELEPORTER);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsWater, Names.TROPICS_WATER);
		Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsPortal, Names.TROPICS_PORTAL);
	    Tropicraft.proxy.registerFluidBlockRendering(BlockRegistry.tropicsPortalTeleporter, Names.TROPICS_PORTAL_TELEPORTER);
		portalWall = registerBlock(registry, new BlockPortalWall(), Names.PORTAL_WALL, new SimpleItemCreator(Names.PORTAL_WALL, true));

		leaves = registerBlock(registry, new BlockTropicraftLeaves(), "leaves", new MultiBlockItemCreator(TropicraftLeaves.VALUES));
		fruitLeaves = registerBlock(registry, new BlockFruitLeaves(), "leaves_fruit", new MultiBlockItemCreator(TropicraftFruitLeaves.VALUES));

		bambooChest = registerBlock(registry, new BlockBambooChest(), Names.BAMBOO_CHEST);
		
		saplings = registerBlock(registry, new BlockTropicsSapling(), "sapling", new StandardItemCreator(TropicraftSaplings.VALUES));
		
		coconut = registerBlock(registry, new BlockCoconut(), Names.COCONUT);

		pineapple = registerBlock(registry, new BlockPineapple(), "pineapple");
		iris = registerBlock(registry, new BlockIris(), "iris");
		coffeePlant = registerBlock(registry, new BlockCoffeeBush(), "coffee_bush", (IBlockItemRegistrar) null);
		
		sands = registerBlock(registry, new BlockTropicraftSands(), "sand", new MultiBlockItemCreator(TropicraftSands.VALUES));
		
		volcano = registerBlock(registry, new BlockVolcano(), Names.VOLCANO, (IBlockItemRegistrar) null);
		
		tikiTorch = registerBlock(registry, new BlockTikiTorch(), "tiki_torch");
		
		drinkMixer = registerBlock(registry, new BlockDrinkMixer(), Names.DRINK_MIXER);
		sifter = registerBlock(registry, new BlockSifter(), Names.SIFTER);
		airCompressor = registerBlock(registry, new BlockAirCompressor(), Names.AIR_COMPRESSOR);
		
		flowerPot = registerBlockNoItem(registry, new BlockTropicraftFlowerPot(), Names.FLOWER_POT);
		bambooDoor = registerBlockNoItem(registry, new BlockBambooDoor(), Names.BAMBOO_DOOR);

		bongo = registerBlock(registry, new BlockBongoDrum(), Names.BONGO, new MultiBlockItemCreator(TropicraftBongos.VALUES));

		bambooFenceGate = registerBlock(registry, (BlockFenceGate) new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0F).setResistance(5.0F), "bamboo_fence_gate");
		thatchFenceGate = registerBlock(registry, (BlockFenceGate) new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0F).setResistance(5.0F), "thatch_fence_gate");
		chunkFenceGate = registerBlock(registry, (BlockFenceGate) new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK).setHardness(2.0F).setResistance(30F), "chunk_fence_gate");
		palmFenceGate = registerBlock(registry, (BlockFenceGate) new BlockFenceGate(BlockPlanks.EnumType.SPRUCE).setHardness(2.0F).setResistance(5.0F), "palm_fence_gate");
		mahoganyFenceGate = registerBlock(registry, (BlockFenceGate) new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0F).setResistance(5.0F), "mahogany_fence_gate");

		bambooFence = registerBlock(registry, new BlockTropicraftFence(bambooFenceGate, Material.PLANTS, MapColor.SAND), "bamboo_fence");
		thatchFence = registerBlock(registry, new BlockTropicraftFence(thatchFenceGate, Material.PLANTS, MapColor.SAND), "thatch_fence");
		chunkFence = registerBlock(registry, new BlockTropicraftFence(chunkFenceGate, Material.PLANTS, MapColor.SAND), "chunk_fence");
		palmFence = registerBlock(registry, new BlockTropicraftFence(palmFenceGate, Material.PLANTS, MapColor.SAND), "palm_fence");
		mahoganyFence = registerBlock(registry, new BlockTropicraftFence(mahoganyFenceGate, Material.PLANTS, MapColor.SAND), "mahogany_fence");

		bambooLadder = registerBlock(registry, new BlockTropicraftLadder().setHardness(BlockHardnessValues.BAMBOO.hardness).setResistance(BlockHardnessValues.BAMBOO.resistance), Names.BAMBOO_LADDER);
	}

	public static void init() {

	}
	
	public static void clientProxyInit() {
		Tropicraft.proxy.registerColoredBlock(sands);
	}

	public static <T extends Block> T registerBlock(IForgeRegistry<Block> registry, T block, String name, IBlockItemRegistrar item, CreativeTabs tab) {
		block.setUnlocalizedName(getNamePrefixed(name));
		block.setRegistryName(new ResourceLocation(Info.MODID, name));
		registry.register(block);
		block.setCreativeTab(tab);

		if (item != null) {
		    ItemRegistry.addBlockItem(block, item);
		}

		return block;
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

	public static <T extends Block> T registerBlock(IForgeRegistry<Block> registry, T block, String name) {
		return registerBlock(registry, block, name, new SimpleItemCreator(name, false));
	}

	private static <T extends Block> T registerBlock(IForgeRegistry<Block> registry, T block, String name, IBlockItemRegistrar item) {
	    return registerBlock(registry, block, name, item, CreativeTabRegistry.tropicraftTab);
    }

    public static <T extends Block> T registerBlockNoItem(IForgeRegistry<Block> registry, T block, String name) {
        return registerBlock(registry, block, name, (IBlockItemRegistrar) null);
    }

	public static <T extends Block> T registerBlock(IForgeRegistry<Block> registry, T block, String name, CreativeTabs tab) {
		return registerBlock(registry, block, name, new SimpleItemCreator(name, false), tab);
	}

	public static void registerOreDictWildcard(String oreDictName, Block block) {
		OreDictionary.registerOre(oreDictName, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
	}
}