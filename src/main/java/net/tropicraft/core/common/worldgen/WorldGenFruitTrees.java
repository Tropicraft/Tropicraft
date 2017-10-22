package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockFruitLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftFruitLeaves;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenFruitTrees extends TCGenBase {

	private final IBlockState WOOD_BLOCK = Blocks.LOG.getDefaultState();
	private final IBlockState REGULAR_LEAF_BLOCK = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.FRUIT);
	private final IBlockState FRUIT_LEAF_BLOCK;

	int treeType;

	public WorldGenFruitTrees(World world, Random rand, int type) {
		super(world, rand);
	    treeType = type;
		FRUIT_LEAF_BLOCK = BlockRegistry.fruitLeaves.getDefaultState().withProperty(BlockFruitLeaves.VARIANT, TropicraftFruitLeaves.byMetadata(type));
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		int height = rand.nextInt(3) + 4;
		boolean canGenerate = true;

		if (j < 1 || j + height + 1 > worldObj.getHeight()) {
			return false;
		}

		for (int y = j; y <= j + 1 + height; y++) {
			int size = 1;
			if (y == j) {
				size = 0;
			}

			if (y >= (j + 1 + height) - 2) {
				size = 2;
			}

			for (int x = i - size; x <= i + size && canGenerate; x++) {
				for (int z = k - size; z <= k + size && canGenerate; z++) {
					if (y >= 0 && y < worldObj.getHeight()) {
						Block block = TCGenUtils.getBlock(worldObj, x, y, z);
						if (TCGenUtils.isBlockInList(block, Blocks.AIR, FRUIT_LEAF_BLOCK.getBlock())) {
							canGenerate = false;
						}
					} else {
						canGenerate = false;
					}
				}
			}
		}

		if (!canGenerate) {
			return false;
		}

		Block blockUnder = TCGenUtils.getBlock(worldObj, i, j - 1, k);
		if (blockUnder != Blocks.GRASS && blockUnder != Blocks.DIRT || j >= worldObj.getHeight() - height - 1) {
			return false;
		}

		TCGenUtils.setBlockState(worldObj, i, j - 1, k, Blocks.DIRT.getDefaultState(), blockGenNotifyFlag);

		for (int y = (j - 3) + height; y <= j + height; y++) {
			int presizeMod = y - (j + height);
			int size = 1 - presizeMod / 2;
			for (int x = i - size; x <= i + size; x++) {
				int localX = x - i;
				for (int z = k - size; z <= k + size; z++) {
					int localZ = z - k;
					if ((Math.abs(localX) != size || Math.abs(localZ) != size || rand.nextInt(2) != 0 && presizeMod != 0) && !TCGenUtils.getBlockState(worldObj, x, y, z).isOpaqueCube()) {
						if (rand.nextBoolean()) {
							// Set fruit-bearing leaves here
							TCGenUtils.setBlockState(worldObj, x, y, z, FRUIT_LEAF_BLOCK, blockGenNotifyFlag);
						} else {
							// Set plain fruit tree leaves here
						    TCGenUtils.setBlockState(worldObj, x, y, z, REGULAR_LEAF_BLOCK, blockGenNotifyFlag);
						}
					}
				}
			}
		}

		// Tree stem
		for (int y = 0; y < height; y++) {
			Block k2 = TCGenUtils.getBlock(worldObj, i, j + y, k);
			if (k2 == Blocks.AIR || k2.getDefaultState().getMaterial() == Material.LEAVES) {
				TCGenUtils.setBlockState(worldObj, i, j + y, k, WOOD_BLOCK, blockGenNotifyFlag);
			}
		}

		return true;
	}
}
