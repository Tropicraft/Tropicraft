package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class TCNoiseGen extends WorldGenerator {

	private final NoiseGeneratorPerlin noiseGen;
	private final int min, max;
	private final double noiseCutoff;

	protected TCNoiseGen(Random rand, int minPerChunk, int maxPerChunk, double minNoise) {
		this.noiseGen = new NoiseGeneratorPerlin(rand, 1);
		this.min = minPerChunk;
		this.max = maxPerChunk;
		this.noiseCutoff = minNoise;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		boolean ret = false;
		int toGenerate = rand.nextInt(max - min + 1) + min;
		for (int i = 0; i < toGenerate; i++) {
			int x = rand.nextInt(16);
			int z = rand.nextInt(16);
			BlockPos toPlace = worldIn.getTopSolidOrLiquidBlock(position.add(x, 0, z)).up(2);
			EnumActionResult res = EnumActionResult.PASS;
			while (res == EnumActionResult.PASS) {
				if (toPlace.getY() <= 0) {
					res = EnumActionResult.FAIL;
				} else {
					res = checkPlacement(worldIn, toPlace = toPlace.down());
				}
			}
			if (res == EnumActionResult.SUCCESS) {
				double noise = (noiseGen.getValue(toPlace.getX() + 0.5, toPlace.getZ() + 0.5) + 1) / 2;
				if (noise > noiseCutoff) {
					IBlockState state = getStateFromNoise((noise - noiseCutoff) * (1 / (1 - noiseCutoff)));

					setBlockAndNotifyAdequately(worldIn, toPlace, state);
					ret = true;
				}
			}
		}
		return ret;
	}

	/**
	 * Computes the position to place the block.
	 * 
	 * @param pos
	 *            The highest solid/liquid block in this column
	 * @return The position to generate a block.
	 */
	protected abstract EnumActionResult checkPlacement(World world, BlockPos pos);

	/**
	 * Computes the blockstate to place from a noise value.
	 * 
	 * @param noiseVal
	 *            The noise value, interpolated on 0..1. So if the noise cutoff for this generator is 0.5, a raw noise value of 0.75 would be passed as 0.5.
	 * @return The blockstate to place.
	 */
	protected abstract IBlockState getStateFromNoise(double noiseVal);
}
