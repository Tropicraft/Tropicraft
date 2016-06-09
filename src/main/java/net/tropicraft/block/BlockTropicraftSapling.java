package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.world.worldgen.TCGenBase;
import net.tropicraft.world.worldgen.WorldGenTallTree;
import net.tropicraft.world.worldgen.WorldGenTropicraftCurvedPalm;
import net.tropicraft.world.worldgen.WorldGenTropicraftFruitTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftLargePalmTrees;
import net.tropicraft.world.worldgen.WorldGenTropicraftNormalPalms;
import net.tropicraft.world.worldgen.WorldGenTualang;
import net.tropicraft.world.worldgen.WorldGenUpTree;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftSapling extends BlockSapling implements IGrowable {

	/** Names of the sub-blocks */
	protected String[] names;

	/** Array of icons associated with this item */
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public BlockTropicraftSapling(String[] names) {
		super();
		this.names = names;
		setTickRandomly(true);
		disableStats();
		this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
	}

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block block)
    {
        return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland
        		|| block == Blocks.sand || block == TCBlockRegistry.purifiedSand || block == TCBlockRegistry.mineralSands;
    }
	
	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, rand);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0) {
				this.markOrGrowMarked(world, x, y, z, rand);
			}
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * "Can Fertilize?"
	 */
	 public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
		 return true;
	 }

	 /**
	  * "Should Fertilize?"
	  */
	 public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
		 return (double)p_149852_1_.rand.nextFloat() < 0.45D;
	 }

	 /**
	  * "Fertilize"
	  */
	 public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_, int p_149853_5_) {
		 this.markOrGrowMarked(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
	 }

	 /**
	  * "growTree"
	  * @param world World instance
	  * @param x x coord
	  * @param y y coord
	  * @param z z coord
	  * @param rand Random instance
	  */
	 public void markOrGrowMarked(World world, int x, int y, int z, Random rand) {
		 // Change flag (and back) to make it show up when generated
		 TCGenBase.blockGenNotifyFlag = 3;
		 int l = world.getBlockMetadata(x, y, z);
	//	 if ((l & 8) == 0) {
/*			 world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
		 } else {*/
			 this.func_149878_d(world, x, y, z, rand);
	//	 }
		TCGenBase.blockGenNotifyFlag = TCGenBase.BLOCK_GEN_NOTIFY_FLAG_DEFAULT;
	 }

	 /**
	  * "Grow Tree"
	  */
	 public void func_149878_d(World world, int x, int y, int z, Random rand) {
         // the first 3 bits determine the sapling type
		 int type = world.getBlockMetadata(x, y, z) & 7;
		 WorldGenerator gen = null;

		 if (type == 0) {
			 int b = rand.nextInt(3);
			 if (b == 0) {
				 gen = new WorldGenTropicraftLargePalmTrees(false);
			 } else if (b == 1) {
				 gen = new WorldGenTropicraftCurvedPalm(world, rand);
			 } else if (b == 2) {
				 gen = new WorldGenTropicraftNormalPalms(false);
			 }
		 } else if (type == 1) {
			 gen = randomRainforestTreeGen(world);
		 } else {
			 gen = new WorldGenTropicraftFruitTrees(world, rand, type - 2);			 
		 }

		 if (gen != null) {
			 world.setBlockToAir(x, y, z);
			 if (!gen.generate(world, rand, x, y, z)) {
				 world.setBlock(x, y, z, this, type, 3);
			 }
		 }
	 }
	 
	 private WorldGenerator randomRainforestTreeGen(World world) {
			Random rand = new Random();
			int type = rand.nextInt(4);
			
			switch(type) {
			case 0:
				return new WorldGenTallTree(world, rand);
			case 1:
				return new WorldGenUpTree(world, rand);
			case 2:
				//return new WorldGenBentRainforestTree(world, rand, false);
			case 3:
				return new WorldGenTualang(world, rand, 18, 9);
			default:
				return new WorldGenTualang(world, rand, 25, 10);
			}
		}

	 /**
	  * Gets an icon index based on an item's damage value
	  */
	 @Override
	 @SideOnly(Side.CLIENT)
	 public IIcon getIcon(int id, int metadata) {
		 // the first 3 bits determine the sapling type
		 int type = metadata & 7;
		 if (type < 0 || type > (names.length - 1))
			 type = names.length - 1; // if out of range, orange sapling

		 return icons[type];
	 }

	 /**
	  * Determines the damage on the item the block drops. Used in cloth and wood.
	  */
	 @Override
	 public int damageDropped(int meta) {
        /*
            The 4th metadata bit is a growth flag, set by minecraft.
            Don't let it affect the item dropped.
         */
        return meta & 7;
	 }

	 /**
	  * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	  */
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		 for (int i = 0; i < names.length; i++)
			 list.add(new ItemStack(item, 1, i));        
	 }

	 /**
	  * @return The unlocalized block name
	  */
	 @Override
	 public String getUnlocalizedName() {
		 return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
	 }

	 /**
	  * Get the true name of the block
	  * @param unlocalizedName tile.%truename%
	  * @return The actual name of the block, rather than tile.%truename%
	  */
	 protected String getActualName(String unlocalizedName) {
		 return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
	 }

	 /**
	  * Register all icons here
	  * @param iconRegister Icon registry
	  */
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void registerBlockIcons(IIconRegister iconRegister) {
		 icons = new IIcon[names.length];

		 for (int i = 0 ; i < names.length ; i++) {
			 icons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[i]);
		 }
	 }
}
