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

public class WorldGenLargePalmTrees extends TCGenBase {
	
	private static final IBlockState woodState = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.PALM);
	private static final IBlockState leafState = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.PALM);
	
	public WorldGenLargePalmTrees(World world, Random rand) {
		super(world, rand);
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		int height = rand.nextInt(7) + 7;
		boolean canGen = true;

		// Sanity check
		if (j < 1 || j + height + 1 > TCGenBase.MAX_CHUNK_HEIGHT) {
			return false;
		}

		for (int l = j; l <= j + 1 + height; l++) {
			byte byte1 = 1;

			if(l == j) {
				byte1 = 0;
			}

			if(l >= (j + 1 + height) - 2) {
				byte1 = 2;
			}

			for(int j1 = i - byte1; j1 <= i + byte1 && canGen; j1++) {
				for(int k1 = k - byte1; k1 <= k + byte1 && canGen; k1++) {
					if(l >= 0 && l < TCGenBase.MAX_CHUNK_HEIGHT) {
						Block l1 = getBlock(j1, l, k1);
						if(l1 != Blocks.AIR && l1 != BlockRegistry.leaves) {
							canGen = false;
						}
					} else
					{
						canGen = false;
					}
				}

			}

		}

		if(!canGen) {
			return false;
		}

		// Make sure we're still not going off the top of the map
		// Also make sure it's sand below the block
		Material matBelow = getMaterial(i, j - 1, k);
		if(matBelow != Material.SAND || j >= TCGenBase.MAX_CHUNK_HEIGHT - height - 1) {
			int ground = getHeight(i, k);
			matBelow = getMaterial(i, ground - 1, k);

			if(matBelow != Material.SAND || j >= TCGenBase.MAX_CHUNK_HEIGHT - height - 1) {
				return false;
			}
			j = ground;
		}
		
		for(int y = 0; y <= height; y++) {
			setBlockState(i, (j + y), k, woodState);
		}
		
		// Wheeee, auto-generated code!
		setBlockState(i + 0, j + height + 1, k + -7, leafState);
		setBlockState(i + -1, j + height + 1, k + -6, leafState);
		setBlockState(i + 1, j + height + 1, k + -6, leafState);
		setBlockState(i + -5, j + height + 1, k + -5, leafState);
		setBlockState(i + 5, j + height + 1, k + -5, leafState);
		setBlockState(i + -6, j + height + 1, k + -1, leafState);
		setBlockState(i + 0, j + height + 1, k + -1, woodState);
		setBlockState(i + 6, j + height + 1, k + -1, leafState);
		setBlockState(i + -7, j + height + 1, k + 0, leafState);
		setBlockState(i + -1, j + height + 1, k + 0, woodState);
		setBlockState(i + 0, j + height + 1, k + 0, woodState);
		setBlockState(i + 1, j + height + 1, k + 0, woodState);
		setBlockState(i + 7, j + height + 1, k + 0, leafState);
		setBlockState(i + -6, j + height + 1, k + 1, leafState);
		setBlockState(i + 0, j + height + 1, k + 1, woodState);
		setBlockState(i + 6, j + height + 1, k + 1, leafState);
		setBlockState(i + -5, j + height + 1, k + 5, leafState);
		setBlockState(i + 5, j + height + 1, k + 5, leafState);
		setBlockState(i + -1, j + height + 1, k + 6, leafState);
		setBlockState(i + 1, j + height + 1, k + 6, leafState);
		setBlockState(i + 0, j + height + 1, k + 7, leafState);
		setBlockState(i + 0, j + height + 2, k + -6, leafState);
		setBlockState(i + -4, j + height + 2, k + -5, leafState);
		setBlockState(i + -1, j + height + 2, k + -5, leafState);
		setBlockState(i + 1, j + height + 2, k + -5, leafState);
		setBlockState(i + 4, j + height + 2, k + -5, leafState);
		setBlockState(i + -5, j + height + 2, k + -4, leafState);
		setBlockState(i + -3, j + height + 2, k + -4, leafState);
		setBlockState(i + -1, j + height + 2, k + -4, leafState);
		setBlockState(i + 1, j + height + 2, k + -4, leafState);
		setBlockState(i + 3, j + height + 2, k + -4, leafState);
		setBlockState(i + 5, j + height + 2, k + -4, leafState);
		setBlockState(i + -4, j + height + 2, k + -3, leafState);
		setBlockState(i + -2, j + height + 2, k + -3, leafState);
		setBlockState(i + -1, j + height + 2, k + -3, leafState);
		setBlockState(i + 1, j + height + 2, k + -3, leafState);
		setBlockState(i + 2, j + height + 2, k + -3, leafState);
		setBlockState(i + 4, j + height + 2, k + -3, leafState);
		setBlockState(i + -3, j + height + 2, k + -2, leafState);
		setBlockState(i + -1, j + height + 2, k + -2, leafState);
		setBlockState(i + 1, j + height + 2, k + -2, leafState);
		setBlockState(i + 3, j + height + 2, k + -2, leafState);
		setBlockState(i + -5, j + height + 2, k + -1, leafState);
		setBlockState(i + -4, j + height + 2, k + -1, leafState);
		setBlockState(i + -3, j + height + 2, k + -1, leafState);
		setBlockState(i + -2, j + height + 2, k + -1, leafState);
		setBlockState(i + -1, j + height + 2, k + -1, leafState);
		setBlockState(i + 0, j + height + 2, k + -1, leafState);
		setBlockState(i + 1, j + height + 2, k + -1, leafState);
		setBlockState(i + 2, j + height + 2, k + -1, leafState);
		setBlockState(i + 3, j + height + 2, k + -1, leafState);
		setBlockState(i + 4, j + height + 2, k + -1, leafState);
		setBlockState(i + 5, j + height + 2, k + -1, leafState);
		setBlockState(i + -6, j + height + 2, k + 0, leafState);
		setBlockState(i + -1, j + height + 2, k + 0, leafState);
		setBlockState(i + 0, j + height + 2, k + 0, leafState);
		setBlockState(i + 1, j + height + 2, k + 0, leafState);
		setBlockState(i + 6, j + height + 2, k + 0, leafState);
		setBlockState(i + -5, j + height + 2, k + 1, leafState);
		setBlockState(i + -4, j + height + 2, k + 1, leafState);
		setBlockState(i + -3, j + height + 2, k + 1, leafState);
		setBlockState(i + -2, j + height + 2, k + 1, leafState);
		setBlockState(i + -1, j + height + 2, k + 1, leafState);
		setBlockState(i + 0, j + height + 2, k + 1, leafState);
		setBlockState(i + 1, j + height + 2, k + 1, leafState);
		setBlockState(i + 2, j + height + 2, k + 1, leafState);
		setBlockState(i + 3, j + height + 2, k + 1, leafState);
		setBlockState(i + 4, j + height + 2, k + 1, leafState);
		setBlockState(i + 5, j + height + 2, k + 1, leafState);
		setBlockState(i + -3, j + height + 2, k + 2, leafState);
		setBlockState(i + -1, j + height + 2, k + 2, leafState);
		setBlockState(i + 1, j + height + 2, k + 2, leafState);
		setBlockState(i + 3, j + height + 2, k + 2, leafState);
		setBlockState(i + -4, j + height + 2, k + 3, leafState);
		setBlockState(i + -2, j + height + 2, k + 3, leafState);
		setBlockState(i + -1, j + height + 2, k + 3, leafState);
		setBlockState(i + 1, j + height + 2, k + 3, leafState);
		setBlockState(i + 2, j + height + 2, k + 3, leafState);
		setBlockState(i + 4, j + height + 2, k + 3, leafState);
		setBlockState(i + -5, j + height + 2, k + 4, leafState);
		setBlockState(i + -3, j + height + 2, k + 4, leafState);
		setBlockState(i + -1, j + height + 2, k + 4, leafState);
		setBlockState(i + 1, j + height + 2, k + 4, leafState);
		setBlockState(i + 3, j + height + 2, k + 4, leafState);
		setBlockState(i + 5, j + height + 2, k + 4, leafState);
		setBlockState(i + -4, j + height + 2, k + 5, leafState);
		setBlockState(i + -1, j + height + 2, k + 5, leafState);
		setBlockState(i + 1, j + height + 2, k + 5, leafState);
		setBlockState(i + 4, j + height + 2, k + 5, leafState);
		setBlockState(i + 0, j + height + 2, k + 6, leafState);
		setBlockState(i + 0, j + height + 3, k + -5, leafState);
		setBlockState(i + -4, j + height + 3, k + -4, leafState);
		setBlockState(i + 0, j + height + 3, k + -4, leafState);
		setBlockState(i + 4, j + height + 3, k + -4, leafState);
		setBlockState(i + -3, j + height + 3, k + -3, leafState);
		setBlockState(i + 0, j + height + 3, k + -3, leafState);
		setBlockState(i + 3, j + height + 3, k + -3, leafState);
		setBlockState(i + -2, j + height + 3, k + -2, leafState);
		setBlockState(i + 0, j + height + 3, k + -2, leafState);
		setBlockState(i + 2, j + height + 3, k + -2, leafState);
		setBlockState(i + -1, j + height + 3, k + -1, leafState);
		setBlockState(i + 0, j + height + 3, k + -1, leafState);
		setBlockState(i + 1, j + height + 3, k + -1, leafState);
		setBlockState(i + -5, j + height + 3, k + 0, leafState);
		setBlockState(i + -4, j + height + 3, k + 0, leafState);
		setBlockState(i + -3, j + height + 3, k + 0, leafState);
		setBlockState(i + -2, j + height + 3, k + 0, leafState);
		setBlockState(i + -1, j + height + 3, k + 0, leafState);
		setBlockState(i + 1, j + height + 3, k + 0, leafState);
		setBlockState(i + 2, j + height + 3, k + 0, leafState);
		setBlockState(i + 3, j + height + 3, k + 0, leafState);
		setBlockState(i + 4, j + height + 3, k + 0, leafState);
		setBlockState(i + 5, j + height + 3, k + 0, leafState);
		setBlockState(i + -1, j + height + 3, k + 1, leafState);
		setBlockState(i + 0, j + height + 3, k + 1, leafState);
		setBlockState(i + 1, j + height + 3, k + 1, leafState);
		setBlockState(i + -2, j + height + 3, k + 2, leafState);
		setBlockState(i + 0, j + height + 3, k + 2, leafState);
		setBlockState(i + 2, j + height + 3, k + 2, leafState);
		setBlockState(i + -3, j + height + 3, k + 3, leafState);
		setBlockState(i + 0, j + height + 3, k + 3, leafState);
		setBlockState(i + 3, j + height + 3, k + 3, leafState);
		setBlockState(i + -4, j + height + 3, k + 4, leafState);
		setBlockState(i + 0, j + height + 3, k + 4, leafState);
		setBlockState(i + 4, j + height + 3, k + 4, leafState);
		setBlockState(i + 0, j + height + 3, k + 5, leafState);
		
		for(int y = 0; y <= height; y++) {
			// TODO: Move this to a utils class?
			BlockTropicraftLog.spawnCoconuts(worldObj, new BlockPos(i, j + y, k), rand, 2);
		}
		return true;
	}
}