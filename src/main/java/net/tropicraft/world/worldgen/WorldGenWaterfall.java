package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenWaterfall extends TCDirectionalGen {

	private static final Block WATER_BLOCK = TCBlockRegistry.tropicsWater;
	
	public WorldGenWaterfall(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(int i, int j, int k) {
		if(this.worldObj.getBlock(i, j, k) == Blocks.stone) {
			int size = rand.nextInt(4) + 3;
			if(this.worldObj.getBlock(i + 1, j, k) == Blocks.air) {
				int dir = this.worldObj.getBlock(i, j, k + 1) == Blocks.stone ? 1 : -1;
				size *= dir;
				for(int x = 0; x < size; x += dir) {
					this.worldObj.setBlock(i, j, k + x, WATER_BLOCK);
					if(this.worldObj.getBlock(i + 1, j, k + x + dir) != Blocks.air || this.worldObj.getBlock(i + x + dir, j, k) != Blocks.stone) {
						break;
					}
				}
			}
			
			if(this.worldObj.getBlock(i - 1, j, k) == Blocks.air) {
				int dir = this.worldObj.getBlock(i, j, k + 1) == Blocks.stone ? 1 : -1;
				size *= dir;
				for(int x = 0; x < size; x += dir) {
					this.worldObj.setBlock(i, j, k + x, WATER_BLOCK);
					if(this.worldObj.getBlock(i - 1, j, k + x + dir) != Blocks.air || this.worldObj.getBlock(i + x + dir, j, k) != Blocks.stone) {
						break;
					}
				}
			}
			
			if(this.worldObj.getBlock(i, j, k + 1) == Blocks.air) {
				int dir = this.worldObj.getBlock(i + 1, j, k) == Blocks.stone ? 1 : -1;
				size *= dir;
				for(int x = 0; x < size; x += dir) {
					this.worldObj.setBlock(i + x, j, k, WATER_BLOCK);
					if(this.worldObj.getBlock(i + x + dir, j, k + 1) != Blocks.air || this.worldObj.getBlock(i + x + dir, j, k) != Blocks.stone) {
						break;
					}
				}
			}
			
			if(this.worldObj.getBlock(i, j, k - 1) == Blocks.air) {
				int dir = this.worldObj.getBlock(i + 1, j, k) == Blocks.stone ? 1 : -1;
				size *= dir;
				for(int x = 0; x < size; x += dir) {
					this.worldObj.setBlock(i + x, j, k, WATER_BLOCK);
					if(this.worldObj.getBlock(i + x + dir, j, k + 1) != Blocks.air || this.worldObj.getBlock(i + x + dir, j, k) != Blocks.stone) {
						break;
					}
				}
			}
		}
		
		return true;
	}

}
