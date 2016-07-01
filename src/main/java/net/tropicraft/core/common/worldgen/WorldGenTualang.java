package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenTualang extends TCGenBase {
	
	private static final IBlockState WOOD_BLOCK = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.MAHOGANY);
	private static final IBlockState LEAF_BLOCK = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.MAHOGANY);
	private int baseHeight;
	private int maxHeight;
	
	public WorldGenTualang(World world, Random random, int maxHeight, int baseHeight) {
		super(world, random);
		this.baseHeight = baseHeight;
		this.maxHeight = maxHeight;
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		int height = rand.nextInt(maxHeight - baseHeight) + baseHeight + j;
		int branches = rand.nextInt(3) + 3;
		
		if (height + 6 > 256) {
			return false;
		}
		
		Block blockUnder = getBlock(i, j - 1, k);
		if (blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS) {
			return false;
		}
		
		//For the trunk
		for (int x = i - 1; x <= i + 1; x++) {
			for (int z = k - 1; z <= k + 1; z++) {
				for (int y = j; y < j + height; y++) {
					Block block = getBlock(x, y, z);
					if (block.getDefaultState().isOpaqueCube()) {
						return false;
					}
				}
			}
		}
		
		//For the top
		for(int x = i - 9; x >= i + 9; x++) {
			for(int z = k - 9; z >= k + 9; z++) {
				for(int y = height; y < height + 6; y++) {
					Block block = getBlock(x, y, z);
					if (block.getDefaultState().isOpaqueCube()) {
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
		
		for(int y = j; y < height; y++) {
			setBlockState(i, y, k, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i - 1, y, k, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i + 1, y, k, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i, y, k - 1, WOOD_BLOCK, blockGenNotifyFlag);
			setBlockState(i, y, k + 1, WOOD_BLOCK, blockGenNotifyFlag);
		}
		
		for(int x = 0; x < branches; x++) {
			int branchHeight = rand.nextInt(4) + 2 + height;
			int bx = rand.nextInt(15) - 8 + i;
			int bz = rand.nextInt(15) - 8 + k;
			
			placeBlockLine( new int[] { i + sign((bx - i) / 2), height, k + sign((bz - k) / 2) }, new int[] { bx, branchHeight, bz }, WOOD_BLOCK);
			
			genCircle(bx, branchHeight, bz, 2, 1, LEAF_BLOCK, false);
			genCircle(bx, branchHeight + 1, bz, 3, 2, LEAF_BLOCK, false);
		}
		
		return true;
	}
	
	private int sign(int i) {
		return i == 0 ? 0 : i <= 0 ? -1 : 1;
	}
	
}