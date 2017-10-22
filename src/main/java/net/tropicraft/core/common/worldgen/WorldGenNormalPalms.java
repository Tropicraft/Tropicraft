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
import static net.tropicraft.core.common.worldgen.TCGenUtils.setBlockState;

public class WorldGenNormalPalms extends TCGenBase {

	public WorldGenNormalPalms(World world, Random random) {
		super(world, random);
	}

	private static final IBlockState palmWood = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.PALM);
	private static final IBlockState palmLeaves = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.PALM);

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		byte height = (byte)(rand.nextInt(4) + 6);
		boolean flag = true;
		if (j < 1 || j + height + 1 > 128) {
			return false;
		}

		for (int l = j; l <= j + 1 + height; l++) {
			byte byte1 = 1;
			if (l == j) {
				byte1 = 0;
			}

			if (l >= (j + 1 + height) - 2) {
				byte1 = 2;
			}
			
			for (int k1 = i - byte1; k1 <= i + byte1 && flag; k1++) {
				for (int i2 = k - byte1; i2 <= k + byte1 && flag; i2++) {
					if (l >= 0 && l < 128) {
						BlockPos pos2 = new BlockPos(k1, l, i2);
						IBlockState j2 = worldObj.getBlockState(pos2);
						if (!worldObj.isAirBlock(pos2) && j2 != palmLeaves) {
							flag = false;
						}
					} else {
						flag = false;
					}
				}

			}

		}

		if (!flag) {
			return false;
		}

		IBlockState i1 = TCGenUtils.getBlockState(worldObj, i, j - 1, k);
		if (i1.getMaterial() != Material.SAND || j >= 128 - height - 1) {
			int ground = worldObj.getHeight(new BlockPos(i, 0, k)).getY();
			i1 = TCGenUtils.getBlockState(worldObj, i, ground - 1, k);
			if (i1.getMaterial() != Material.SAND || j >= 128 - height - 1) {
				return false;
			}
			j = ground;
		}
		
		setBlockState(worldObj, i, j + height + 2, k, palmLeaves);
		setBlockState(worldObj, i, j + height + 1, k + 1, palmLeaves);
		setBlockState(worldObj, i, j + height + 1, k + 2, palmLeaves);
		setBlockState(worldObj, i, j + height + 1, k + 3, palmLeaves);
		setBlockState(worldObj, i, j + height, k + 4, palmLeaves);
		setBlockState(worldObj, i + 1, j + height + 1, k, palmLeaves);
		setBlockState(worldObj, i + 2, j + height + 1, k, palmLeaves);
		setBlockState(worldObj, i + 3, j + height + 1, k, palmLeaves);
		setBlockState(worldObj, i + 4, j + height, k, palmLeaves);
		setBlockState(worldObj, i, j + height + 1, k - 1, palmLeaves);
		setBlockState(worldObj, i, j + height + 1, k - 2, palmLeaves);
		setBlockState(worldObj, i, j + height + 1, k - 3, palmLeaves);
		setBlockState(worldObj, i, j + height, k - 4, palmLeaves);
		setBlockState(worldObj, i - 1, j + height + 1, k, palmLeaves);
		setBlockState(worldObj, i - 1, j + height + 1, k - 1, palmLeaves);
		setBlockState(worldObj, i - 1, j + height + 1, k + 1, palmLeaves);
		setBlockState(worldObj, i + 1, j + height + 1, k - 1, palmLeaves);
		setBlockState(worldObj, i + 1, j + height + 1, k + 1, palmLeaves);
		setBlockState(worldObj, i - 2, j + height + 1, k, palmLeaves);
		setBlockState(worldObj, i - 3, j + height + 1, k, palmLeaves);
		setBlockState(worldObj, i - 4, j + height, k, palmLeaves);
		setBlockState(worldObj, i + 2, j + height + 1, k + 2, palmLeaves);
		setBlockState(worldObj, i + 2, j + height + 1, k - 2, palmLeaves);
		setBlockState(worldObj, i - 2, j + height + 1, k + 2, palmLeaves);
		setBlockState(worldObj, i - 2, j + height + 1, k - 2, palmLeaves);
		setBlockState(worldObj, i + 3, j + height, k + 3, palmLeaves);
		setBlockState(worldObj, i + 3, j + height, k - 3, palmLeaves);
		setBlockState(worldObj, i - 3, j + height, k + 3, palmLeaves);
		setBlockState(worldObj, i - 3, j + height, k - 3, palmLeaves);
		
		for (int j1 = 0; j1 < height + 4; j1++) {
			Block l1 = TCGenUtils.getBlock(worldObj, i, j + j1, k);
			if (!TCGenUtils.isAirBlock(worldObj, i, j + j1, k) && l1 != palmLeaves.getBlock()) {
				continue;
			}
			setBlockState(worldObj, i, (j + j1) - 2, k, palmWood);
			BlockPos pos3 = new BlockPos(i, (j + j1) - 2, k);
			BlockTropicraftLog.spawnCoconuts(worldObj, pos3, rand, 2);
			if (j1 <= height - 1 || j1 >= height + 2) {
				continue;
			}
		}

		return true;
	}
}
