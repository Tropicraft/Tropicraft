package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.tropicraft.core.common.dimension.feature.config.FruitTreeConfig;

import java.util.Random;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class FruitTreeFeature extends Feature<FruitTreeConfig> {
	public FruitTreeFeature(Codec<FruitTreeConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, FruitTreeConfig config) {
		pos = pos.toImmutable();
		int height = rand.nextInt(3) + 4;

		if (goesBeyondWorldSize(world, pos.getY(), height)) {
			return false;
		}

		if (!isBBAvailable(world, pos, height)) {
			return false;
		}

		BlockState sapling = config.sapling;
		if (!sapling.isValidPosition(world, pos)) {
			return false;
		}

		setDirtAt(world, pos.down());

		for (int y = (pos.getY() - 3) + height; y <= pos.getY() + height; y++) {
			int presizeMod = y - (pos.getY() + height);
			int size = 1 - presizeMod / 2;
			for (int x = pos.getX() - size; x <= pos.getX() + size; x++) {
				int localX = x - pos.getX();
				for (int z = pos.getZ() - size; z <= pos.getZ() + size; z++) {
					int localZ = z - pos.getZ();
					if ((Math.abs(localX) != size || Math.abs(localZ) != size || rand.nextInt(2) != 0 && presizeMod != 0) && TreeFeature.isAirOrLeavesAt(world, new BlockPos(x, y, z))) {
						BlockPos leafPos = new BlockPos(x, y, z);
						if (rand.nextBoolean()) {
							// Set fruit-bearing leaves here
							setBlockState(world, leafPos, config.fruitLeaves);
						} else {
							// Set plain fruit tree leaves here
							setBlockState(world, leafPos, config.leaves);
						}
					}
				}
			}
		}

		// Tree stem
		for (int y = 0; y < height; y++) {
			BlockPos logPos = pos.up(y);
			if (TreeFeature.isReplaceableAt(world, logPos)) {
				setBlockState(world, logPos, config.wood);
			}
		}

		return true;
	}

	protected static boolean isDirt(IWorldGenerationBaseReader world, BlockPos pos) {
		return world.hasBlockState(pos, (state) -> {
			Block block = state.getBlock();
			return isDirt(block) && block != Blocks.GRASS_BLOCK && block != Blocks.MYCELIUM;
		});
	}

	protected void setDirt(IWorldGenerationReader world, BlockPos pos) {
		if (!isDirt(world, pos)) {
			setBlockState(world, pos, Blocks.DIRT.getDefaultState());
		}
	}

	protected void setDirtAt(IWorldGenerationReader reader, BlockPos pos) {
		setDirt(reader, pos);
	}
}
