package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTualang extends TCGenBase {
	
	private static final Block WOOD_BLOCK = TCBlockRegistry.logs;
	private static final int WOOD_META = 1;
	private static final Block LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
	private static final int LEAF_META = 1;
	private int baseHeight;
	private int maxHeight;
	
	public WorldGenTualang(World world, Random random, int baseHeight, int maxHeight) {
		super(world, random);
		this.baseHeight = baseHeight;
		this.maxHeight = maxHeight;
	}

	@Override
	public boolean generate(int i, int j, int k) {		
		int height = rand.nextInt(maxHeight - baseHeight) + baseHeight + j;
		int branches = rand.nextInt(3) + 3;
		
		if(height + 6 > 256) {
			return false;
		}
		
		Block blockUnder = worldObj.getBlock(i, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
			return false;
		}
		
		//For the trunk
		for(int x = i - 1; x <= i + 1; x++) {
			for(int z = k - 1; z <= k + 1; z++) {
				for(int y = j; y < j + height; y++) {
					Block block = worldObj.getBlock(x, y, z);
					if(block.isOpaqueCube()) {
						return false;
					}
				}
			}
		}
		
		//For the top
		for(int x = i - 9; x >= i + 9; x++) {
			for(int z = k - 9; z >= k + 9; z++) {
				for(int y = height; y < height + 6; y++) {
					Block block = worldObj.getBlock(x, y, z);
					if(block.isOpaqueCube()) {
						return false;
					}
				}
			}
		}

		worldObj.setBlock(i, j, k, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i - 1, j, k, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i + 1, j, k, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i, j, k - 1, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i, j, k + 1, Blocks.dirt, 0, blockGenNotifyFlag);
		
		for(int y = j; y < height; y++) {
			worldObj.setBlock(i, y, k, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i - 1, y, k, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i + 1, y, k, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i, y, k - 1, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i, y, k + 1, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
		}
		
		for(int x = 0; x < branches; x++)
		{
			int branchHeight = rand.nextInt(4) + 2 + height;
			int bx = rand.nextInt(15) - 8 + i;
			int bz = rand.nextInt(15) - 8 + k;
			
			placeBlockLine( new int[] { i + sign((bx - i) / 2), height, k + sign((bz - k) / 2) }, new int[] { bx, branchHeight, bz }, WOOD_BLOCK, 1);
			
			genCircle(bx, branchHeight, bz, 2, 1, LEAF_BLOCK, LEAF_META, false);
			genCircle(bx, branchHeight + 1, bz, 3, 2, LEAF_BLOCK, LEAF_META, false);
		}
		
		return true;
	}
	
	private int sign(int i) {
		return i == 0 ? 0 : i <= 0 ? -1 : 1;
	}
	
}