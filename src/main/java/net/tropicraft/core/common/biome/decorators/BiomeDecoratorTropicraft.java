package net.tropicraft.core.common.biome.decorators;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;

import java.util.Random;

public class BiomeDecoratorTropicraft extends BiomeDecorator {

	public BiomeDecoratorTropicraft() {

	}

	/**
	 * Leave this for each tropics biome decorator to figure out
	 */
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) {
		if (this.decorating) {
			throw new RuntimeException("Already decorating");
		} else {
			this.chunkPos = pos;
			this.decorating = false;
		}
	}

	public int getTerrainHeightAt(World world, int x, int z) {
		for(int y = world.getHeight(new BlockPos(x, 0, z)).getY() + 1; y > 0; y--) {
			IBlockState blockstate = world.getBlockState(new BlockPos(x, y, z));
			if(blockstate.getMaterial() == Material.GRASS ||
			   blockstate.getMaterial() == Material.GROUND ||
			   blockstate.getMaterial() == Material.SAND) {
				return y + 1;
			}
		}
		return 0;
	}

	public final int randDecorationCoord(Random rand, int base, int variance) {
		// Offset by 8 to ensure coordinate is in center of chunks for decoration so that CCG is avoided
		return base + rand.nextInt(variance) + 8;
	}
}
