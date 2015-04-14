package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPineapple extends BlockTallFlowers implements IGrowable {

	/** Number of total ticks it takes for this pineapple to grow */
	private static final int TOTAL_GROW_TICKS = 7;

	/** 
	 * Constant value that holds the metadata value of the pineapple
	 * when it is fully grown
	 */
	private static final int FULL_GROWTH = TOTAL_GROW_TICKS + 1;

	public BlockPineapple(String[] names) {
		super(names);
		this.setBlockName(TCNames.pineapple);
		this.setBlockTextureName(TCNames.tallFlower);
		this.setCreativeTab(TCCreativeTabRegistry.tabFood);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return meta <= TOTAL_GROW_TICKS ? bottomIcon : topIcons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {		
		topIcons = new IIcon[names.length];

		for (int i = 0 ; i < names.length ; i++) {
			topIcons[i] = iconRegister.registerIcon(getActualName(getFormattedTextureName()) + "_" + TCNames.pineappleNames[i]);
		}

		bottomIcon = iconRegister.registerIcon(getActualName(getFormattedTextureName()) + "_" + TCNames.stem);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));
	}

	/**
	 * checks if the block can stay, if not drop as item
	 */
	protected void checkAndDropBlock(World p_149855_1_, int p_149855_2_, int p_149855_3_, int p_149855_4_) {
		// Overriding superclass because derp behavior
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_) {
		// Overriding superclass because derp behavior
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		checkFlowerChange(world, i, j, k);
		if (j > world.getHeight() - 2) {
			return;
		}

		// Current metadata
		int meta = world.getBlockMetadata(i, j, k);

		if (world.getBlock(i, j, k) == this && (meta < TOTAL_GROW_TICKS) && world.isAirBlock(i, j + 1, k)) {
			int growth = meta;
			if (growth >= TOTAL_GROW_TICKS - 1) {
				world.setBlock(i, j + 1, k, this, FULL_GROWTH, 3);
				world.setBlockMetadataWithNotify(i, j, k, TOTAL_GROW_TICKS, 3);
			} else {
				world.setBlockMetadataWithNotify(i, j, k, growth + 1, 3);
			}
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {

	}

	@Override
	public Item getItemDropped(int meta, Random rand, int unused) {
		return null;
	}

	protected void checkFlowerChange(World world, int i, int j, int k) {
		if (!world.isRemote && !canBlockStay(world, i, j, k)) {
			if (world.getBlockMetadata(i, j, k) == TOTAL_GROW_TICKS) {
				dropBlockAsItem(world, i, j, k, 0, 0);
			}
			world.setBlockToAir(i, j, k);
		}
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		boolean belowCheck = false;

		if (world.getBlock(i, j, k) == this && (world.getBlockMetadata(i, j, k) == FULL_GROWTH)) {
			belowCheck = (world.getBlock(i, j - 1, k) == this && world.getBlockMetadata(i, j - 1, k) == TOTAL_GROW_TICKS);
		} else {
			belowCheck = canThisPlantGrowOnThisBlock(world.getBlock(i, j - 1, k));
		}
		return belowCheck && (world.getFullBlockLightValue(i, j, k) >= 8 || world.canBlockSeeTheSky(i, j, k));
	}

	private boolean canThisPlantGrowOnThisBlock(Block b) {
		return b != null && (b.getMaterial() == Material.ground || b.getMaterial() == Material.grass);
	}

	/**
	 * 
	 * @return Tropicraft-mod formattted texture name/location
	 */
	protected String getFormattedTextureName() {
		return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(this.getTextureName()));
	}

	/**
	 * @return The unlocalized block name
	 */
	@Override
	public String getUnlocalizedName() {
		return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(TCNames.tallFlower));
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
	 * canFertilize
	 */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean var5) {
		return true;
	}

	/**
	 * shouldFertilize
	 */
	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) <= 7 && world.isAirBlock(x, y + 1, z);
	}

	/**
	 * fertilize
	 */
	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {
		world.setBlock(x, y + 1, z, this, 8, 3);
	}
}
