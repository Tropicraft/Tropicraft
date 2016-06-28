package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenBamboo extends TCGenBase {

	private static final int MIN_BAMBOO = 60;
	private static final int MAX_BAMBOO = 120;
	private static final int MIN_HEIGHT = 4;
	private static final int MAX_HEIGHT = 11;
	
	private static final Block BAMBOO_BLOCK = BlockRegistry.bambooShoot;
	
	public WorldGenBamboo(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		j = this.getTerrainHeightAt(i, k);
		
		if(!worldObj.isAirBlock(pos)) {
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
				BlockPos bpos = new BlockPos(x, y + h, z);
				if(isAirBlock(bpos) && BlockRegistry.bambooShoot.canBlockStay(worldObj, bpos)) {
                    worldObj.setBlockState(pos, BAMBOO_BLOCK.getDefaultState(), blockGenNotifyFlag);
                } else {
                	break;
                }
			}
		}
		return true;
	}

}