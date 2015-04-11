package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenBamboo extends TCGenBase {

	private static final int MIN_BAMBOO = 30;
	private static final int MAX_BAMBOO = 60;
	private static final int MIN_HEIGHT = 4;
	private static final int MAX_HEIGHT = 8;
	
	private static final Block BAMBOO_BLOCK = TCBlockRegistry.bambooChute;
	
	public WorldGenBamboo(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(int i, int j, int k) {
		j = this.getTerrainHeightAt(i, k);
		
		if(!worldObj.isAirBlock(i, j, k)) {
			return false;
		} else if((worldObj.getBlock(i + 1, j - 1, k).getMaterial() != Material.water && worldObj.getBlock(i - 1, j - 1, k).getMaterial() != Material.water &&
				worldObj.getBlock(i, j - 1, k + 1).getMaterial() != Material.water && worldObj.getBlock(i, j - 1, k - 1).getMaterial() != Material.water)) {
			return false;
		}
		
		int amount = rand.nextInt(MAX_BAMBOO - MIN_BAMBOO) + MIN_BAMBOO;
		int spread = rand.nextInt(3) - 1 + (int)(Math.sqrt(amount) / 2);
		
		for(int l = 0; l < amount; l++) {
			int x = i + rand.nextInt(spread) - rand.nextInt(spread);
			int z = k + rand.nextInt(spread) - rand.nextInt(spread);
			int y = this.getTerrainHeightAt(x, z);
			int height = rand.nextInt(MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT;
			for(int h = 0; h < height; h++) {
				if(worldObj.isAirBlock(x, y + h, z)/* && TropicraftBlocks.bambooChute.canBlockStay(world, x, y + h, z)*/) { //TODO: Can block stay code
                    worldObj.setBlock(x, y + h, z, BAMBOO_BLOCK, 0, blockGenNotifyFlag);
                } else {
                	break;
                }
			}
		}
		return true;
	}

}
