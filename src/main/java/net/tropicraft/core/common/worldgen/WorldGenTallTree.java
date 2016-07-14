package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenTallTree extends TCGenBase {

	private static final int VINE_CHANCE = 5;
	private static final int SMALL_LEAF_CHANCE = 3;
	private static final int SECOND_CANOPY_CHANCE = 3;

	private static final IBlockState WOOD_BLOCK = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.MAHOGANY);
	private static final IBlockState LEAF_BLOCK = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.MAHOGANY);

	public WorldGenTallTree(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(BlockPos pos) {	
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		Block blockUnder = getBlock(i, j - 1, k);
		if(blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
			return false;
		}
		blockUnder = getBlock(i + 1, j - 1, k);
		if(blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
			return false;
		}
		blockUnder = getBlock(i - 1, j - 1, k);
		if(blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
			return false;
		}
		blockUnder = getBlock(i, j - 1, k + 1);
		if(blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
			return false;
		}
		blockUnder = getBlock(i, j - 1, k - 1);
		if(blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
			return false;
		}

		int height = rand.nextInt(15) + 15;

		for(int y = j; y < j + height + 6; y++)
		{
			for(int x = i - 1; x <= i + 1; x++)
			{
				for(int z = k - 1; z <= k + 1; z++)
				{
					Block block = getBlock(x, y, z);
					if(block != Blocks.AIR && block != Blocks.TALLGRASS && block != LEAF_BLOCK) {
						return false;
					}
				}
			}
		}

		setBlockState(i, j, k, Blocks.DIRT.getDefaultState(), blockGenNotifyFlag);
		setBlockState(i - 1, j, k, Blocks.DIRT.getDefaultState(), blockGenNotifyFlag);
		setBlockState(i + 1, j, k, Blocks.DIRT.getDefaultState(), blockGenNotifyFlag);
		setBlockState(i, j, k - 1, Blocks.DIRT.getDefaultState(), blockGenNotifyFlag);
		setBlockState(i, j, k + 1, Blocks.DIRT.getDefaultState(), blockGenNotifyFlag);

		for(int y = j; y < j + height; y++) {
			setBlockState(i, y, k, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i - 1, y, k, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i + 1, y, k, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i, y, k - 1, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i, y, k + 1, WOOD_BLOCK, blockGenNotifyFlag);
			if(y - j > height / 2 && rand.nextInt(SMALL_LEAF_CHANCE) == 0) {
				int nx = rand.nextInt(3) - 1 + i;
				int nz = rand.nextInt(3) - 1 + k;

				genCircle(nx, y + 1, nz, 1, 0, LEAF_BLOCK, false);
				genCircle(nx, y, nz, 2, 1, LEAF_BLOCK, false);
				for(int x = nx - 3; x <= nx + 3; x++) {
					for(int z = nz - 3; z <= nz + 3; z++) {
						for(int y1 = y - 1; y1 <= y; y1++) {
							if(rand.nextInt(VINE_CHANCE) == 0) {
								genVines(x, y1, z);
							}
						}
					}
				}
			}
			if(y - j > height - (height / 4) && y - j < height - 3 && rand.nextInt(SECOND_CANOPY_CHANCE) == 0) {
				int nx = i + rand.nextInt(9) - 4;
				int nz = k + rand.nextInt(9) - 4;

				int leafSize = rand.nextInt(3) + 5;

				genCircle(nx, y + 3, nz, leafSize - 2, 0, LEAF_BLOCK, false);
				genCircle(nx, y + 2, nz, leafSize - 1, leafSize - 3, LEAF_BLOCK, false);
				genCircle(nx, y + 1, nz, leafSize, leafSize - 1, LEAF_BLOCK, false);

				placeBlockLine(new int[] { i, y - 2, k }, new int[] { nx, y + 2, nz }, WOOD_BLOCK);
				for(int x = nx - leafSize; x <= nx + leafSize; x++) {
					for(int z = nz - leafSize; z <= nz + leafSize; z++) {
						for(int y1 = y; y1 <= y + 2; y1++) {
							if(rand.nextInt(VINE_CHANCE) == 0) {
								genVines(x, y1, z);
							}
						}
					}
				}
			}
		}

		int leafSize = rand.nextInt(5) + 9;

		genCircle(i, j + height, k, leafSize - 2, 0, LEAF_BLOCK, false);
		genCircle(i, j + height - 1, k, leafSize - 1, leafSize - 4, LEAF_BLOCK, false);
		genCircle(i, j + height - 2, k, leafSize, leafSize - 1, LEAF_BLOCK, false);

		for(int x = i - leafSize; x <= i + leafSize; x++) {
			for(int z = k - leafSize; z <= k + leafSize; z++) {
				for(int y1 = j + height + 3; y1 <= j + height + 6; y1++) {
					if(rand.nextInt(VINE_CHANCE) == 0) {
						genVines(x, y1, z);
					}
				}
			}
		}

		return true;
	}

	private boolean genVines(int i, int j, int k) {
		int m = 2;

		do {
			if (m > 5) {
				return false;
			}

			if (Blocks.VINE.canPlaceBlockOnSide(worldObj, new BlockPos(i, j, k), EnumFacing.getHorizontal(m)) && getBlock(i, j, k) == Blocks.AIR) {
				setBlockState(i, j, k, Blocks.VINE.getDefaultState(), blockGenNotifyFlag);
				break;
			}

			m++;
		}
		while (true);

		int length = rand.nextInt(4) + 4;

		for(int y = j - 1; y > j - length; y--) {
			if(getBlock(i, y, k) == Blocks.AIR) {
				setBlockState(i, y, k, Blocks.VINE.getDefaultState(), blockGenNotifyFlag);        	
			} else {
				return true;
			}
		}

		return true;
	}

}