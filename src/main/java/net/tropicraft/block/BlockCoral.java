package net.tropicraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockCoral extends BlockTropicraftMulti {

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
	
    /**
     * Indicate if a material is a normal solid opaque cube
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

	@Override
	public int damageDropped(int i) {
		return i & 7;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return super.canPlaceBlockAt(world, i, j, k) && world.getBlock(i, j, k) != this && canThisPlantGrowOnThisBlock(world.getBlock(i, j - 1, k)) 
				&& world.getBlock(i, j, k).getMaterial() == Material.water && world.getBlock(i, j + 1, k).getMaterial() == Material.water;
	}

	protected boolean canThisPlantGrowOnThisBlock(Block b) {
		return b == Blocks.grass || b == Blocks.dirt || b == Blocks.sand || b == TCBlockRegistry.purifiedSand
				|| b == TCBlockRegistry.mineralSands;
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

	protected void checkFlowerChange(World world, int i, int j, int k) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k) & 7, 0);
			world.setBlockToAir(i, j, k);
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return (int) (15.0F * BRIGHTNESS_NIGHT);
	}

	/**
	 * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
	 */
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return (world.getBlock(x, y, z).getMaterial().isLiquid() && 
				world.getBlock(x, y + 1, z).getMaterial().isLiquid()) &&
				canThisPlantGrowOnThisBlock(world.getBlock(x, y - 1, z));
	}
}
