package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenSeaweed extends TCNoiseGen {

	public WorldGenSeaweed(Random rand) {
		super(rand, 100, 200, 0.9f);
	}

	@Override
	protected EnumActionResult checkPlacement(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() == Blocks.SAND && world.getBlockState(pos.up()).getMaterial().isLiquid()) {
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	protected IBlockState getStateFromNoise(double noiseVal) {
		return BlockRegistry.seaweed.getDefaultState();
	}
}
