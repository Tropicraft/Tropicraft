package net.tropicraft.core.common.worldgen;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSurfaceClump extends WorldGenerator {
	
	private final float chance; 
	
	private final int range;
	private final double maxDistanceSq;
	
	private final Predicate<IBlockState> bottomBlock, topBlock;
	private final Function<Random, IBlockState> stateFunc;
	
	private final boolean replaceBottom;

	public WorldGenSurfaceClump(float chance, int range, Predicate<IBlockState> bottomBlock, Predicate<IBlockState> topBlock, Function<Random, IBlockState> stateFunc, boolean replaceBottom) {
		this.chance = chance;
		this.range = range;
		this.maxDistanceSq = range * range + range * range;
		this.bottomBlock = bottomBlock;
		this.topBlock = topBlock;
		this.stateFunc = stateFunc;
		this.replaceBottom = replaceBottom;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		boolean ret = false;
		if (rand.nextFloat() < chance) {
			BlockPos center = position.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8);
			int range = rand.nextInt(this.range + 1);
			for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-range, 0, -range), center.add(range, 0, range))) {
				double chance = Math.pow(MathHelper.clamp((maxDistanceSq - pos.distanceSq(center)) / maxDistanceSq, 0, 1), 2);
				if (rand.nextDouble() < chance) {
					BlockPos toPlace = worldIn.getTopSolidOrLiquidBlock(pos);
					if (canPlaceBlock(worldIn, toPlace)) {
						setBlockAndNotifyAdequately(worldIn, replaceBottom ? toPlace.down() : toPlace, stateFunc.apply(rand));
						ret = true;
					}
				}
			}
		}
		return ret;
	}
	
	protected boolean canPlaceBlock(World world, BlockPos pos) {
		return bottomBlock.test(world.getBlockState(pos.down())) && topBlock.test(world.getBlockState(pos));
	}
}
