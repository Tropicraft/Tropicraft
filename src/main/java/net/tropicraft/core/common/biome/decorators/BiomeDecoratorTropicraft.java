package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;

public class BiomeDecoratorTropicraft extends BiomeDecorator {

	public BiomeDecoratorTropicraft() {

	}

	/**
	 * Leave this for each tropics biome decorator to figure out
	 */
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
	{
		if (this.decorating)
		{
			throw new RuntimeException("Already decorating");
		}
		else
		{
			this.chunkPos = pos;
			this.decorating = false;
		}
	}

	public int getTerrainHeightAt(World world, int x, int z) {
		for(int y = world.getHeight(new BlockPos(x, 0, z)).getY() + 1; y > 0; y--) {
			Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			if(block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAND) {
				return y + 1;
			}
		}
		return 0;
	}


	public final int randCoord(Random rand, int base, int variance) {
		return base + rand.nextInt(variance);
	}
}
