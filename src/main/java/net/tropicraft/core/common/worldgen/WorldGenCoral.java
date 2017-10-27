package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockCoral;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenCoral extends TCNoiseGen {

	public WorldGenCoral(Random rand) {
		super(rand, 0, 8, 0);
	}

	@Override
	protected EnumActionResult checkPlacement(World world, BlockPos pos, Random rand) {
		if (((BlockCoral) BlockRegistry.coral).canBlockStay(world, pos)) {
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	protected IBlockState getStateFromNoise(double noiseVal) {
		int variant = (int) ((noiseVal * TropicraftCorals.VALUES.length) + 1);
		return BlockRegistry.coral.defaultForVariant(TropicraftCorals.byMetadata(variant));
	}
}
