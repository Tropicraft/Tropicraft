package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenUpTree extends TCGenBase {

	private static final Block WOOD_BLOCK = TCBlockRegistry.logs;
	private static final int WOOD_META = 1;
	private static final Block LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
	private static final int LEAF_META = 1;
	
	public WorldGenUpTree(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(int i, int j, int k) {
		Block blockUnder = this.worldObj.getBlock(i, j - 1, k);
		if(blockUnder != Blocks.grass && blockUnder != Blocks.dirt) {
			return false;
		}
		
		int height = rand.nextInt(4) + 6;
		
		for(int y = j; y < j + height; y++) {
			Block block = this.worldObj.getBlock(i, y, k);
			if(block.getMaterial() == Material.leaves && !block.isOpaqueCube()) {
				return false;
			}
		}
		
		for(int y = j; y < j + height; y++) {
			this.worldObj.setBlock(i, y, k, WOOD_BLOCK, WOOD_META, 3);
			if(rand.nextInt(5) == 0) {
				int x = rand.nextInt(3) - 1 + i;
				int z = k;
				if(x - i == 0) {
					z += rand.nextBoolean() ? 1 : -1;
				}
				this.worldObj.setBlock(x, y, z, LEAF_BLOCK, LEAF_META, 3);
			}
			
			if(y == j + height - 1) {
				this.worldObj.setBlock(i + 1, y, k, WOOD_BLOCK, WOOD_META, 3);
				this.worldObj.setBlock(i - 1, y, k, WOOD_BLOCK, WOOD_META, 3);
				this.worldObj.setBlock(i, y, k + 1, WOOD_BLOCK, WOOD_META, 3);
				this.worldObj.setBlock(i, y, k - 1, WOOD_BLOCK, WOOD_META, 3);
			}
		}
		
		int radius = rand.nextInt(2) + 3;
		
		this.genCircle(i, j + height, k, radius, 0, LEAF_BLOCK, LEAF_META, false);
		this.genCircle(i, j + height + 1, k, radius + 2, radius, LEAF_BLOCK, LEAF_META, false);
		this.genCircle(i, j + height + 2, k, radius + 3, radius + 2, LEAF_BLOCK, LEAF_META, false);
		
		return true;
	}

}
