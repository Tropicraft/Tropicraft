package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenSeaweed extends WorldGenerator {
	
	private static final int range = 6;
	private static final double maxDistanceSq = range * range + range * range; // square distance formula

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		boolean ret = false;
		if (rand.nextInt(40) == 0) {
			BlockPos center = position.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8);
			for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-8, 0, -8), center.add(8, 0, 8))) {
				double chance = Math.pow(MathHelper.clamp((maxDistanceSq - pos.distanceSq(center)) / maxDistanceSq, 0, 1), 2);
				if (rand.nextDouble() < chance) {
					BlockPos toPlace = worldIn.getTopSolidOrLiquidBlock(pos).down();
					if (worldIn.getBlockState(toPlace).getBlock() == Blocks.SAND && worldIn.getBlockState(toPlace.up()).getMaterial().isLiquid()) {
						setBlockAndNotifyAdequately(worldIn, toPlace, BlockRegistry.seaweed.getDefaultState());
						ret = true;
					}
				}
			}
		}
		return ret;
	}
}
