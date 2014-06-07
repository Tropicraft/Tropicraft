package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockCoral extends BlockTropicraftMulti implements IPlantable {

	/** Brightness value of coral blocks during the day */
	private static final float BRIGHTNESS_DAY = 0.3F;

	/** Brightness value of coral blocks during the night */
	private static final float BRIGHTNESS_NIGHT = 0.6F;

	public BlockCoral(String[] names) {
		super(names, Material.water);
		this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
		setLightLevel(0.3F);
		setHardness(0.0F);
		setTickRandomly(true);
		this.setBlockTextureName(TCNames.coral);
	}

	@Override
	public int damageDropped(int i) {
		return i & (names.length - 1);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return super.canPlaceBlockAt(world, i, j, k) && world.getBlock(i, j, k) != this && canThisPlantGrowOnThisBlock(world.getBlock(i, j - 1, k)) && world.getBlock(i, j, k).getMaterial() == Material.water
				&& world.getBlock(i, j + 1, k).getMaterial() == Material.water;
	}

	protected boolean canThisPlantGrowOnThisBlock(Block b) {
		return b == Blocks.grass || b == Blocks.dirt || b == Blocks.sand /*|| b == TCBlocks.purifiedSand*/;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block b) {
		super.onNeighborBlockChange(world, i, j, k, b);
		checkFlowerChange(world, i, j, k);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		checkFlowerChange(world, i, j, k);

		//  if(!world.isRemote)
		//TODO	   generateTropicalFish(world,i,j,k,random);
	}

	@Override
	public int getRenderType() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	protected final void checkFlowerChange(World world, int i, int j, int k) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k) & 7, 0);
			world.setBlockToAir(i, j, k);
		} else {
			// Checks if world day/night does not match coral day/night type
			int meta = world.getBlockMetadata(i, j, k);
			if (world.isDaytime() != isDayCoral(meta)) {
				int newMetadata = (meta & 7) | (isDayCoral(meta) ? 8 : 0);
				world.setBlockMetadataWithNotify(i, j, k, newMetadata, 3);
			}
		}
	}

	/**
	 * Check to see if the coral is "daytime" coral
	 * @param metadata Metadata value
	 * @return Whether the coral is daytime coral
	 */
	private boolean isDayCoral(int metadata) {
		return (metadata & 8) == 0;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		if (isDayCoral(world.getBlockMetadata(x, y, z))) {
			return (int) (15.0F * BRIGHTNESS_DAY);
		} else {
			return (int) (15.0F * BRIGHTNESS_NIGHT);
		}
	}

	/**
	 * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
	 */
	public boolean canBlockStay(World world, int x, int y, int z) {
		return (world.getBlock(x, y, z).getMaterial() == Material.water && 
				world.getBlock(x, y + 1, z).getMaterial() == Material.water) &&
				canThisPlantGrowOnThisBlock(world.getBlock(x, y - 1, z));
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Water;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

}
