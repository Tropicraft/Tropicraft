package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTropicraftFlowers extends TCGenBase {

	private static final int FLOWER_TRIES = 35;
	
	private Block plantBlock;
	private int[] metadata;

	public WorldGenTropicraftFlowers(World world, Random rand, Block plantBlock, int[] metadata) {
		super(world, rand);
		this.plantBlock = plantBlock;
		this.metadata = metadata;
	}

    @Override
	public boolean generate(int i, int j, int k) {
		for(int l = 0; l < FLOWER_TRIES; l++) {
			int x = (i + rand.nextInt(8)) - rand.nextInt(8);
			int y = (j + rand.nextInt(4)) - rand.nextInt(4);
			int z = (k + rand.nextInt(8)) - rand.nextInt(8);
			if(worldObj.isAirBlock(x, y, z) && TCBlockRegistry.flowers.canBlockStay(this.worldObj, x, y, z)) {
				if (rand.nextInt(3) == 0) {
					worldObj.setBlock(x, y, z, plantBlock, metadata[rand.nextInt(metadata.length)], blockGenNotifyFlag);
				}
			}
		}

		return true;
	}
}
