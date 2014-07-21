package net.tropicraft.world.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

import java.util.Random;

public class WorldGenTropicraftFruitTrees extends TCGenBase {

	private static final Block WOOD_BLOCK = Blocks.log;
	private static final Block LEAF_BLOCK = TCBlockRegistry.fruitLeaves;
	
	int treeType;

	public WorldGenTropicraftFruitTrees(World world, Random rand, int i) {
		super(world, rand);
		treeType = i;
	}

    @Override
	public boolean generate(int i, int j, int k) {
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
						Block block = worldObj.getBlock(x, y, z);
						if (block != Blocks.air && block != LEAF_BLOCK) {
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
		Block blockUnder = worldObj.getBlock(i, j - 1, k);
		if (blockUnder != Blocks.grass && blockUnder != Blocks.dirt || j >= worldObj.getHeight() - height - 1) {
			return false;
		}

		worldObj.setBlock(i, j - 1, k, Blocks.dirt);

		for (int y = (j - 3) + height; y <= j + height; y++) {
			int presizeMod = y - (j + height);
			int size = 1 - presizeMod / 2;
			for (int x = i - size; x <= i + size; x++) {
				int localX = x - i;
				for (int z = k - size; z <= k + size; z++) {
					int localZ = z - k;
					if ((Math.abs(localX) != size || Math.abs(localZ) != size || rand.nextInt(2) != 0 && presizeMod != 0) && !worldObj.getBlock(x, y, z).isOpaqueCube()) {
						if (rand.nextBoolean()) {
							// Set fruit-bearing leaves here
							worldObj.setBlock(x, y, z, LEAF_BLOCK, treeType, 3);
						} else {
							// Set plain fruit tree leaves here
							worldObj.setBlock(x, y, z, LEAF_BLOCK, 1, 3); //TODO Change to normal leaf
						}
					}
				}
			}
		}

		for (int y = 0; y < height; y++) {
			Block k2 = worldObj.getBlock(i, j + y, k);
			if (k2 == Blocks.air || k2.getMaterial() == Material.leaves) {
				worldObj.setBlock(i, j + y, k, WOOD_BLOCK, 0, 3);
			}
		}

		return true;
	}
}
