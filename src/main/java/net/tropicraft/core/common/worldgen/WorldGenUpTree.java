package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenUpTree extends TCGenBase {

	private static final IBlockState WOOD_STATE = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.MAHOGANY);
	private static final IBlockState LEAF_STATE = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.MAHOGANY);
	
	public WorldGenUpTree(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		Block blockUnder = TCGenUtils.getBlock(worldObj, pos.down());
		if (blockUnder != Blocks.GRASS && blockUnder != Blocks.DIRT) {
			return false;
		}
		
		int height = rand.nextInt(4) + 6;
		
		for (int y = j; y < j + height; y++) {
			IBlockState state = TCGenUtils.getBlockState(worldObj, i, y, k);
			if (state.getMaterial() == Material.LEAVES && !state.isOpaqueCube()) {
				return false;
			}
		}
		
		for (int y = j; y < j + height; y++) {
			TCGenUtils.setBlockState(worldObj, i, y, k, WOOD_STATE, blockGenNotifyFlag);
			if (rand.nextInt(5) == 0) {
				int x = rand.nextInt(3) - 1 + i;
				int z = k;
				if (x - i == 0) {
					z += rand.nextBoolean() ? 1 : -1;
				}
				TCGenUtils.setBlockState(worldObj, x, y, z, LEAF_STATE, blockGenNotifyFlag);
			}
			
			if (y == j + height - 1) {
				TCGenUtils.setBlockState(worldObj, i + 1, y, k, WOOD_STATE, blockGenNotifyFlag);
				TCGenUtils.setBlockState(worldObj, i - 1, y, k, WOOD_STATE, blockGenNotifyFlag);
				TCGenUtils.setBlockState(worldObj, i, y, k + 1, WOOD_STATE, blockGenNotifyFlag);
				TCGenUtils.setBlockState(worldObj, i, y, k - 1, WOOD_STATE, blockGenNotifyFlag);
			}
		}
		
		int radius = rand.nextInt(2) + 3;
		
		this.genCircle(i, j + height, k, radius, 0, LEAF_STATE, false);
		this.genCircle(i, j + height + 1, k, radius + 2, radius, LEAF_STATE, false);
		this.genCircle(i, j + height + 2, k, radius + 3, radius + 2, LEAF_STATE, false);
		
		return true;
	}

}
