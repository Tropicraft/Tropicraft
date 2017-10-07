package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenSeaweed extends WorldGenerator {

    public WorldGenSeaweed() {}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		for (int i1 = 0; i1 < 12; i1++) {
			BlockPos pos2 = pos.add(random.nextInt(16), 0, random.nextInt(16));
			pos2 = world.getTopSolidOrLiquidBlock(pos2);
			IBlockState state = world.getBlockState(pos2);
			if (state.getMaterial().isLiquid()) {
				while ((state = world.getBlockState((pos2 = pos2.down()))).getMaterial().isLiquid());
				if (state.getBlock() == Blocks.SAND) {
					setBlockAndNotifyAdequately(world, pos2, BlockRegistry.seaweed.getDefaultState());
				}
			}
		}

		return true;
	}
}
