package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.dimension.feature.config.FruitTreeConfig;

import java.util.Random;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class FruitTreeFeature extends Feature<FruitTreeConfig> {
	public FruitTreeFeature(Codec<FruitTreeConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<FruitTreeConfig> context) {
		WorldGenLevel world = context.level();
		Random rand = context.random();
		BlockPos pos = context.origin();
		FruitTreeConfig config = context.config();

		pos = pos.immutable();
		int height = rand.nextInt(3) + 4;

		if (goesBeyondWorldSize(world, pos.getY(), height)) {
			return false;
		}

		if (!isBBAvailable(world, pos, height)) {
			return false;
		}

		BlockState sapling = config.sapling;
		if (!sapling.canSurvive(world, pos)) {
			return false;
		}

		setDirtAt(world, pos.below());

		for (int y = (pos.getY() - 3) + height; y <= pos.getY() + height; y++) {
			int presizeMod = y - (pos.getY() + height);
			int size = 1 - presizeMod / 2;
			for (int x = pos.getX() - size; x <= pos.getX() + size; x++) {
				int localX = x - pos.getX();
				for (int z = pos.getZ() - size; z <= pos.getZ() + size; z++) {
					int localZ = z - pos.getZ();
					if ((Math.abs(localX) != size || Math.abs(localZ) != size || rand.nextInt(2) != 0 && presizeMod != 0) && TreeFeature.isAirOrLeaves(world, new BlockPos(x, y, z))) {
						BlockPos leafPos = new BlockPos(x, y, z);
						if (rand.nextBoolean()) {
							// Set fruit-bearing leaves here
							setBlock(world, leafPos, config.fruitLeaves);
						} else {
							// Set plain fruit tree leaves here
							setBlock(world, leafPos, config.leaves);
						}
					}
				}
			}
		}

		// Tree stem
		for (int y = 0; y < height; y++) {
			BlockPos logPos = pos.above(y);
			if (TreeFeature.validTreePos(world, logPos)) {
				setBlock(world, logPos, config.wood);
			}
		}

		return true;
	}

	protected static boolean isDirt(LevelSimulatedReader world, BlockPos pos) {
		return world.isStateAtPosition(pos, (state) -> {
			Block block = state.getBlock();
			return isDirt(block.defaultBlockState()) && block != Blocks.GRASS_BLOCK && block != Blocks.MYCELIUM;
		});
	}

	protected void setDirt(LevelSimulatedRW world, BlockPos pos) {
		if (!isDirt(world, pos)) {
			setBlock(world, pos, Blocks.DIRT.defaultBlockState());
		}
	}

	protected void setDirtAt(LevelSimulatedRW reader, BlockPos pos) {
		setDirt(reader, pos);
	}
}
